package com.baiyi.opscloud.handler;

import com.aliyuncs.ecs.model.v20140526.CreateInstanceRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.baiyi.opscloud.aliyun.ecs.AliyunInstance;
import com.baiyi.opscloud.bo.CloudInstanceTaskMemberBO;
import com.baiyi.opscloud.bo.CreateCloudInstanceBO;
import com.baiyi.opscloud.builder.CreateInstanceRequestBuilder;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.OcCloudInstanceTaskMember;
import com.baiyi.opscloud.domain.generator.opscloud.OcEnv;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroup;
import com.baiyi.opscloud.service.cloud.OcCloudInstanceTaskMemberService;
import com.baiyi.opscloud.service.cloud.OcCloudInstanceTaskService;
import com.baiyi.opscloud.service.cloud.OcCloudVpcVswitchService;
import com.baiyi.opscloud.service.env.OcEnvService;
import com.baiyi.opscloud.service.server.OcServerGroupService;
import com.baiyi.opscloud.service.server.OcServerService;
import com.google.common.base.Joiner;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.baiyi.opscloud.facade.impl.CloudInstanceTaskFacadeImpl.*;

/**
 * @Author baiyi
 * @Date 2020/3/30 6:23 下午
 * @Version 1.0
 */
@Component
public class CreateInstanceTaskHandler {

    @Resource
    private OcServerGroupService ocServerGroupService;

    @Resource
    private OcServerService ocServerService;

    @Resource
    private OcEnvService ocEnvService;

    @Resource
    private AliyunInstance aliyunInstance;

    @Resource
    private OcCloudInstanceTaskService ocCloudInstanceTaskService;

    @Resource
    private OcCloudInstanceTaskMemberService ocCloudInstanceTaskMemberService;

    @Resource
    private OcCloudVpcVswitchService ocCloudVpcVswitchService;

    public boolean createInstanceHandler(int taskId, CreateCloudInstanceBO createCloudInstanceBO, int maxSerialNumber, int seq, String vswitchId) {
        String regionId = createCloudInstanceBO.getCloudInstanceTemplate().getRegionId();
        String hostname = getHostname(maxSerialNumber, seq, createCloudInstanceBO);
        CreateInstanceRequest createInstanceRequest = CreateInstanceRequestBuilder.build(createCloudInstanceBO, vswitchId, hostname);
        BusinessWrapper wrapper = aliyunInstance.getCreateInstanceResponse(regionId, createInstanceRequest);
        String instanceId = "";
        if (wrapper.isSuccess()) {
            instanceId = (String) wrapper.getBody();
        } else {
            String errCode = (String) wrapper.getBody();
            // SDK超时需要触发实例查询确保不重复开通
            if (errCode.equals("SDK.ServerUnreachable")) {
                try {
                    // retry
                    DescribeInstancesResponse.Instance instance = aliyunInstance.getStoppedInstance(regionId, hostname);
                    instanceId = instance.getInstanceId();
                } catch (Exception e) {
                }
            }
        }
        if (StringUtils.isEmpty(instanceId)) return false;
        CloudInstanceTaskMemberBO cloudInstanceTaskMemberBO = CloudInstanceTaskMemberBO.builder()
                .taskId(taskId)
                .instanceId(instanceId)
                .seq(seq)
                .regionId(createCloudInstanceBO.getCloudInstanceTemplate().getRegionId())
                .hostname(hostname)
                .taskStatus(TASK_STATUS_CREATE_INSTANCE)
                .zoneId(ocCloudVpcVswitchService.queryOcCloudVpcVswitchByVswitchId(vswitchId).getZoneId())
                .build();
        OcCloudInstanceTaskMember ocCloudInstanceTaskMember = BeanCopierUtils.copyProperties(cloudInstanceTaskMemberBO, OcCloudInstanceTaskMember.class);
        ocCloudInstanceTaskMemberService.addOcCloudInstanceTaskMember(ocCloudInstanceTaskMember);
        return true;
    }

    public void allocatePublicIpAddressHandler(List<OcCloudInstanceTaskMember> memberList, boolean isAllocatePublicIpAddress) {
        for (OcCloudInstanceTaskMember member : memberList) {
            String publicIp = "";
            if (isAllocatePublicIpAddress) {
                try {
                    publicIp = aliyunInstance.allocateInstancePublicIp(member.getRegionId(), member.getInstanceId());
                    if (!StringUtils.isEmpty(publicIp)) {
                        member.setTaskStatus(TASK_STATUS_ALLOCATE_PUBLIC_IP_ADDRESS);
                        member.setPublicIp(publicIp);
                        ocCloudInstanceTaskMemberService.updateOcCloudInstanceTaskMember(member);
                    }
                } catch (Exception e) {
                }
            } else {
                member.setTaskStatus(TASK_STATUS_ALLOCATE_PUBLIC_IP_ADDRESS);
                ocCloudInstanceTaskMemberService.updateOcCloudInstanceTaskMember(member);
            }
        }
    }

    public void startInstanceHandler(List<OcCloudInstanceTaskMember> memberList) {
        for (OcCloudInstanceTaskMember member : memberList) {
            try {
                if (aliyunInstance.startInstance(member.getRegionId(), member.getInstanceId())) {
                    member.setTaskStatus(TASK_STATUS_STARTING);
                    ocCloudInstanceTaskMemberService.updateOcCloudInstanceTaskMember(member);
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * 查询实例启动状态
     *
     * @param memberList
     */
    public void describeInstanceStatusHandler(String regionId, List<OcCloudInstanceTaskMember> memberList) {
        if (memberList.isEmpty()) return;
        List<String> instanceIds = memberList.stream().map(e -> e.getInstanceId()).collect(Collectors.toList());
        Map<String, OcCloudInstanceTaskMember> memberMap = getOcCloudInstanceTaskMemberMap(memberList);
        List<DescribeInstancesResponse.Instance> instanceList = aliyunInstance.getInstanceList(regionId, instanceIds);
        for (DescribeInstancesResponse.Instance instance : instanceList) {
            if (!memberMap.containsKey(instance.getInstanceId())) continue;
            OcCloudInstanceTaskMember member = memberMap.get(instance.getInstanceId());
            if (instance.getStatus().equalsIgnoreCase("Running")) {
                member.setTaskStatus(TASK_STATUS_RUNNING);
                ocCloudInstanceTaskMemberService.updateOcCloudInstanceTaskMember(member);
            }
            if (StringUtils.isEmpty(member.getPrivateIp())) {
                String privateIp;
                if (instance.getInstanceNetworkType().equals("vpc")) {
                    privateIp = instance.getVpcAttributes().getPrivateIpAddress().get(0);
                } else {
                    privateIp = instance.getInnerIpAddress().get(0);
                }
                member.setPrivateIp(privateIp);
                ocCloudInstanceTaskMemberService.updateOcCloudInstanceTaskMember(member);
            }
        }
    }

    private Map<String, OcCloudInstanceTaskMember> getOcCloudInstanceTaskMemberMap(List<OcCloudInstanceTaskMember> memberList) {
        return memberList.stream().collect(Collectors.toMap(OcCloudInstanceTaskMember::getInstanceId, a -> a, (k1, k2) -> k1));
    }

    /**
     * 获取实例的主机名
     *
     * @param maxSerialNumber
     * @param seq
     * @param createCloudInstanceBO
     * @return
     */
    private String getHostname(int maxSerialNumber, int seq, CreateCloudInstanceBO createCloudInstanceBO) {
        int serialNumber = maxSerialNumber + seq;
        String serverName;
        if (!StringUtils.isEmpty(createCloudInstanceBO.getCreateCloudInstance().getServerName())) {
            serverName = createCloudInstanceBO.getCreateCloudInstance().getServerName();
        } else {
            OcServerGroup ocServerGroup = ocServerGroupService.queryOcServerGroupById(createCloudInstanceBO.getCreateCloudInstance().getServerGroupId());
            serverName = ocServerGroup.getName().replace("group_", "");
        }
        OcEnv ocEnv = ocEnvService.queryOcEnvByType(createCloudInstanceBO.getCreateCloudInstance().getEnvType());
        return Joiner.on("-").skipNulls().join(serverName, !ocEnv.getEnvName().equals("prod") ? ocEnv.getEnvName() : null, serialNumber);
    }

}
