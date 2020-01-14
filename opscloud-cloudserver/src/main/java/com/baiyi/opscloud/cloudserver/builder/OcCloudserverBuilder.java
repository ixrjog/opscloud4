package com.baiyi.opscloud.cloudserver.builder;

import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.baiyi.opscloud.aliyun.ecs.base.ECSDisk;
import com.baiyi.opscloud.cloudserver.base.CloudserverType;
import com.baiyi.opscloud.cloudserver.instance.AliyunECSInstance;
import com.baiyi.opscloud.cloudserver.instance.AwsEC2Instance;
import com.baiyi.opscloud.cloudserver.instance.ZabbixHostInstance;
import com.baiyi.opscloud.cloudserver.util.AwsUtils;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.TimeUtils;
import com.baiyi.opscloud.domain.generator.OcCloudserver;
import com.baiyi.opscloud.zabbix.entry.ZabbixHostInterface;
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
public class OcCloudserverBuilder {

    public static final String ECS_SYSTEM_DISK_TYPE = "system";
    public static final String ECS_DATA_DISK_TYPE = "data";


    /**
     * EC2
     * @param awsEC2Instance
     * @param instanceDetail
     * @return
     */
    public static OcCloudserver buildOcCloudserver(AwsEC2Instance awsEC2Instance, String instanceDetail) {
        com.amazonaws.services.ec2.model.Instance instance = awsEC2Instance.getInstance();
        Map<String, Integer> volumeSizeMap = AwsUtils.getEC2VolumeSizeMap(awsEC2Instance.getVolumeList());
        OcCloudserverBO ocCloudserverBO = OcCloudserverBO.builder()
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
                .cloudserverType(CloudserverType.EC2.getType())
                .instanceDetail(instanceDetail)
                .build();
        return BeanCopierUtils.copyProperties(ocCloudserverBO, OcCloudserver.class);
    }

    /**
     * ECS
     *
     * @param aliyunECSInstance
     * @param instanceDetail
     * @return
     */
    public static OcCloudserver buildOcCloudserver(AliyunECSInstance aliyunECSInstance, String instanceDetail) {
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
        OcCloudserverBO ocCloudserverBO = OcCloudserverBO.builder()
                .instanceType(instance.getInstanceType())
                .instanceName(instance.getInstanceName())
                .instanceId(instance.getInstanceId())
                .serverName(instance.getHostName())
                .instanceDetail(instanceDetail)
                .cloudserverType(CloudserverType.ECS.getType())
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
            ocCloudserverBO.setExpiredTime(TimeUtils.acqGmtDate(instance.getExpiredTime()));
        try {
            for (ECSDisk disk : diskList) {
                if (disk.getType().equals(ECS_SYSTEM_DISK_TYPE)) {
                    ocCloudserverBO.setSystemDiskSize(disk.getSize());
                    continue;
                }
                if (disk.getType().equals(ECS_DATA_DISK_TYPE)) {
                    ocCloudserverBO.setDataDiskSize(disk.getSize());
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return BeanCopierUtils.copyProperties(ocCloudserverBO, OcCloudserver.class);
    }

    /**
     * Zabbix Host
     *
     * @param hostInstance
     * @return
     */
    public static OcCloudserver buildOcCloudserver(ZabbixHostInstance hostInstance, String instanceDetail, String zone) {
        String privateIp = "";
        if (!CollectionUtils.isEmpty(hostInstance.getInterfaceList()))
            for (ZabbixHostInterface hostInterface : hostInstance.getInterfaceList()) {
                if (hostInterface.getType().equals("1") && !StringUtils.isEmpty(hostInterface.getIp())) {
                    privateIp = hostInterface.getIp();
                    break;
                }
            }
        OcCloudserverBO ocCloudserverBO = OcCloudserverBO.builder()
                .instanceType("ZabbixHost")
                .instanceName(hostInstance.getHost().getName())
                .instanceId(hostInstance.getHost().getHostid())
                .serverName(hostInstance.getHost().getName())
                .instanceDetail(hostInstance.getHost().getHostid())
                .cloudserverType(CloudserverType.ZH.getType())
                .privateIp(privateIp)
                .instanceDetail(instanceDetail)
                .createdTime(new Date())
                .zone(zone)
                .build();
        return BeanCopierUtils.copyProperties(ocCloudserverBO, OcCloudserver.class);
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
