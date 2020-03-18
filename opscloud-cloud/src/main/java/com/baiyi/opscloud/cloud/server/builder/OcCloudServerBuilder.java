package com.baiyi.opscloud.cloud.server.builder;

import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.baiyi.opscloud.aliyun.ecs.base.ECSDisk;
import com.baiyi.opscloud.cloud.server.instance.AliyunECSInstance;
import com.baiyi.opscloud.cloud.server.instance.AwsEC2Instance;
import com.baiyi.opscloud.cloud.server.instance.ZabbixHostInstance;
import com.baiyi.opscloud.cloud.server.util.AwsUtils;
import com.baiyi.opscloud.common.base.CloudServerType;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.TimeUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudServer;
import com.baiyi.opscloud.vmware.vcsa.instance.ESXiInstance;
import com.baiyi.opscloud.vmware.vcsa.instance.VMInstance;
import com.baiyi.opscloud.zabbix.entry.ZabbixHostInterface;
import com.google.common.base.Joiner;
import com.vmware.vim25.*;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @Author baiyi
 * @Date 2019/11/27 4:30 PM
 * @Version 1.0
 */
public class OcCloudServerBuilder {

    public static final String ECS_SYSTEM_DISK_TYPE = "system";
    public static final String ECS_DATA_DISK_TYPE = "data";


    public static OcCloudServer build(VMInstance instance, String zone) {
        OcCloudServerBO ocCloudserverBO = OcCloudServerBO.builder()
                .instanceName(instance.getConfigInfoName())
                .serverName(instance.getConfigInfoName())
                .zone(zone)
                .instanceId(instance.getConfigInfoInstanceUuid())
                .cpu(instance.getConfigInfoHardwareNumCPU())
                .memory(instance.getConfigInfoHardwareMemoryMB())
                .comment(instance.getConfigInfoAnnotation())
                .imageId(instance.getConfigInfoGuestFullName())
                .instanceType("VirtualMachine")
                .cloudServerType(CloudServerType.VM.getType())
                .build();
        try {
            if (instance.getVmSummary() != null) {
                VirtualMachineSummary vmSummary = instance.getVmSummary();
                if (!StringUtils.isEmpty(vmSummary.getGuest().getIpAddress()))
                    ocCloudserverBO.setPrivateIp(vmSummary.getGuest().getIpAddress());
            }
        } catch (Exception e) {
        }

        // Disk只记录总容量
        VirtualMachineStorageInfo vmStorageInfo = instance.getVmStorageInfo();
        for (VirtualMachineUsageOnDatastore vmUsageOnDatastore : vmStorageInfo.getPerDatastoreUsage()) {
            // committed 用量/  uncommitted 限制
            int size = (int) (vmUsageOnDatastore.getUncommitted() / 1024 / 1024 / 1024);
            if (ocCloudserverBO.getSystemDiskSize() == 0) {
                ocCloudserverBO.setSystemDiskSize(size);
            } else {
                ocCloudserverBO.setDataDiskSize(ocCloudserverBO.getDataDiskSize() + size);
            }
        }
        return covert(ocCloudserverBO);
    }


    /**
     * VMware VCSA ESXi
     *
     * @param esxiInstance
     * @param zone
     * @return
     */
    public static OcCloudServer build(ESXiInstance esxiInstance, String zone) {
        OcCloudServerBO ocCloudServerBO = OcCloudServerBO.builder()
                .instanceName(esxiInstance.getHostSummary().config.name)
                .serverName(esxiInstance.getHostSummary().config.name)
                .zone(zone)
                .instanceId(esxiInstance.getHostHardwareInfo().systemInfo.uuid)
                .cpu((int) esxiInstance.getHostHardwareInfo().cpuInfo.numCpuCores)
                .memory((int) (esxiInstance.getHostHardwareInfo().memorySize / 1024 / 1024))
                .comment(Joiner.on(" ").join(esxiInstance.getHostHardwareInfo().systemInfo.vendor, esxiInstance.getHostHardwareInfo().systemInfo.model))
                .imageId(esxiInstance.getHostSummary().getConfig().getProduct().getFullName())
                .instanceType("ESXiHostSystem")
                .cloudServerType(CloudServerType.ESXI.getType())
                .instanceDetail(esxiInstance.getHostHardwareInfo().systemInfo.uuid)
                .build();

        if (!esxiInstance.getDatastoreSummaryList().isEmpty()) {
            long capacityTotal = 0;
            for (DatastoreSummary datastoreSummary : esxiInstance.getDatastoreSummaryList())
                capacityTotal += datastoreSummary.getCapacity();
            ocCloudServerBO.setSystemDiskSize((int) (capacityTotal / 1024 / 1024 / 1024));
        }
        // 查询管理IP
        try {
            for (VirtualNicManagerNetConfig virtualNicManagerNetConfig : esxiInstance.getHostConfigInfo().getVirtualNicManagerInfo().netConfig)
                for (HostVirtualNic hostVirtualNic : virtualNicManagerNetConfig.getCandidateVnic())
                    if (!StringUtils.isEmpty(hostVirtualNic.getSpec().ip.ipAddress))
                        ocCloudServerBO.setPrivateIp(hostVirtualNic.getSpec().ip.ipAddress);
        } catch (Exception ignored) {
        }
        return covert(ocCloudServerBO);
    }


    /**
     * EC2
     *
     * @param awsEC2Instance
     * @param instanceDetail
     * @return
     */
    public static OcCloudServer build(AwsEC2Instance awsEC2Instance, String instanceDetail) {
        com.amazonaws.services.ec2.model.Instance instance = awsEC2Instance.getInstance();
        Map<String, Integer> volumeSizeMap = AwsUtils.getEC2VolumeSizeMap(awsEC2Instance.getVolumeList());
        OcCloudServerBO ocCloudServerBO = OcCloudServerBO.builder()
                .systemDiskSize(volumeSizeMap.get("systemDiskSize"))
                .dataDiskSize(volumeSizeMap.get("dataDiskSize"))
                .createdTime(instance.getLaunchTime())
                .vpcId(instance.getVpcId())
                .instanceName(AwsUtils.getEC2InstanceName(instance))
                .instanceType(instance.getInstanceType())
                .zone(instance.getPlacement().getAvailabilityZone())
                .instanceId(instance.getInstanceId())
                .cpu(instance.getCpuOptions().getCoreCount() * instance.getCpuOptions().getThreadsPerCore())
                .memory(awsEC2Instance.getInstanceType().acqMemory())
                .privateIp(instance.getPrivateIpAddress())
                .publicIp(instance.getPublicIpAddress())
                .imageId(instance.getImageId())
                .cloudServerType(CloudServerType.EC2.getType())
                .instanceDetail(instanceDetail)
                .build();
        return covert(ocCloudServerBO);
    }

    /**
     * ECS
     *
     * @param aliyunECSInstance
     * @param instanceDetail
     * @return
     */
    public static OcCloudServer build(AliyunECSInstance aliyunECSInstance, String instanceDetail) {
        DescribeInstancesResponse.Instance instance = aliyunECSInstance.getInstance();
        List<ECSDisk> diskList = aliyunECSInstance.getDiskList();

        String privateIp;
        if (instance.getInstanceNetworkType().equals("vpc")) {
            privateIp = instance.getVpcAttributes().getPrivateIpAddress().get(0);
        } else {
            privateIp = instance.getInnerIpAddress().get(0);
        }
        String publicIp = "";
        if (instance.getPublicIpAddress().size() != 0) {
            publicIp = instance.getPublicIpAddress().get(0);
        }
        // 弹性IP
        if (!StringUtils.isEmpty(instance.getEipAddress().getIpAddress())) {
            publicIp = instance.getEipAddress().getIpAddress();
        }
        String vpcId = "";
        if (instance.getVpcAttributes() != null)
            vpcId = instance.getVpcAttributes().getVpcId();
        OcCloudServerBO ocCloudServerBO = OcCloudServerBO.builder()
                .instanceType(instance.getInstanceType())
                .instanceName(instance.getInstanceName())
                .instanceId(instance.getInstanceId())
                .serverName(instance.getHostName())
                .instanceDetail(instanceDetail)
                .cloudServerType(CloudServerType.ECS.getType())
                .publicIp(publicIp)
                .privateIp(privateIp)
                .cpu(instance.getCpu())
                .memory(instance.getMemory())
                .instanceDetail(instanceDetail)
                .chargeType(instance.getInstanceChargeType())
                .createdTime(TimeUtils.acqGmtDate(instance.getCreationTime()))
                .vpcId(vpcId)
                .comment(instance.getDescription())
                .zone(instance.getZoneId())
                .imageId(instance.getImageId())
                .renewalStatus(aliyunECSInstance.getRenewalStatus())
                .build();
        if (instance.getInstanceChargeType().equalsIgnoreCase("PrePaid") && !StringUtils.isEmpty(instance.getExpiredTime()))
            ocCloudServerBO.setExpiredTime(TimeUtils.acqGmtDate(instance.getExpiredTime()));
        try {
            for (ECSDisk disk : diskList) {
                if (disk.getType().equals(ECS_SYSTEM_DISK_TYPE)) {
                    ocCloudServerBO.setSystemDiskSize(disk.getSize());
                    continue;
                }
                if (disk.getType().equals(ECS_DATA_DISK_TYPE)) {
                    ocCloudServerBO.setDataDiskSize(disk.getSize());
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return covert(ocCloudServerBO);
    }

    /**
     * Zabbix Host
     *
     * @param hostInstance
     * @return
     */
    public static OcCloudServer build(ZabbixHostInstance hostInstance, String instanceDetail, String zone) {
        String privateIp = "";
        if (!CollectionUtils.isEmpty(hostInstance.getInterfaceList()))
            for (ZabbixHostInterface hostInterface : hostInstance.getInterfaceList()) {
                if (hostInterface.getType().equals("1") && !StringUtils.isEmpty(hostInterface.getIp())) {
                    privateIp = hostInterface.getIp();
                    break;
                }
            }
        OcCloudServerBO ocCloudserverBO = OcCloudServerBO.builder()
                .instanceType("ZabbixHost")
                .instanceName(hostInstance.getHost().getName())
                .instanceId(hostInstance.getHost().getHostid())
                .serverName(hostInstance.getHost().getName())
                .instanceDetail(hostInstance.getHost().getHostid())
                .cloudServerType(CloudServerType.ZH.getType())
                .privateIp(privateIp)
                .instanceDetail(instanceDetail)
                .createdTime(new Date())
                .zone(zone)
                .build();
        return covert(ocCloudserverBO);
    }

    private static OcCloudServer covert(OcCloudServerBO ocCloudserverBO){
        return BeanCopierUtils.copyProperties(ocCloudserverBO, OcCloudServer.class);
    }

//    public static CloudServerDO buildCloudServerDO(ServerDO serverDO, int serverStatus, int cloudServerType) {
//        CloudServerDO cloudServerDO = new CloudServerDO();
//        cloudServerDO.setServerId(serverDO.getId());
//        cloudServerDO.setServerName(serverDO.acqServerName());
//        cloudServerDO.setServerStatus(serverStatus);
//        cloudServerDO.setPrivateIp(serverDO.getInsideIp());
//        if (!StringUtils.isEmpty(serverDO.getPublicIp()))
//            cloudServerDO.setPublicIp(serverDO.getPublicIp());
//        if (!StringUtils.isEmpty(serverDO.getArea()))
//            cloudServerDO.setZone(serverDO.getArea());
//        cloudServerDO.setCloudServerType(cloudServerType);
//        return cloudServerDO;
//    }
}
