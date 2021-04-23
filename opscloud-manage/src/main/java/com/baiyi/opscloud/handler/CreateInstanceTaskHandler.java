package com.baiyi.opscloud.handler;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.ecs.model.v20140526.CreateInstanceRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.baiyi.opscloud.aliyun.ecs.AliyunInstance;
import com.baiyi.opscloud.bo.CloudInstanceTaskMemberBO;
import com.baiyi.opscloud.bo.CreateCloudInstanceBO;
import com.baiyi.opscloud.bo.ServerBO;
import com.baiyi.opscloud.builder.CreateInstanceRequestBuilder;
import com.baiyi.opscloud.cloud.server.ICloudServer;
import com.baiyi.opscloud.cloud.server.factory.CloudServerFactory;
import com.baiyi.opscloud.common.base.CloudInstanceTaskPhase;
import com.baiyi.opscloud.common.base.CloudInstanceTaskStatus;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudInstanceTaskMember;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudServer;
import com.baiyi.opscloud.domain.vo.server.ServerVO;
import com.baiyi.opscloud.facade.ServerFacade;
import com.baiyi.opscloud.service.cloud.OcCloudInstanceTaskMemberService;
import com.baiyi.opscloud.service.cloud.OcCloudServerService;
import com.baiyi.opscloud.service.cloud.OcCloudVpcVswitchService;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/3/30 6:23 下午
 * @Version 1.0
 */
@Component
public class CreateInstanceTaskHandler {

    @Resource
    private OcCloudServerService ocCloudServerService;

    @Resource
    private AliyunInstance aliyunInstance;

    @Resource
    private OcCloudInstanceTaskMemberService ocCloudInstanceTaskMemberService;

    @Resource
    private OcCloudVpcVswitchService ocCloudVpcVswitchService;

    @Resource
    private ServerFacade serverFacade;

    public boolean createInstanceHandler(int taskId, CreateCloudInstanceBO createCloudInstanceBO, ServerBO serverBO) {
        String regionId = createCloudInstanceBO.getCloudInstanceTemplate().getRegionId();
        String hostname = serverBO.getHostname();
        CreateInstanceRequest createInstanceRequest = CreateInstanceRequestBuilder.build(createCloudInstanceBO, serverBO);
        BusinessWrapper wrapper = aliyunInstance.getCreateInstanceResponse(regionId, createInstanceRequest);
        String instanceId = "";
        if (wrapper.isSuccess()) {
            instanceId = (String) wrapper.getBody();
        } else {
            String errCode = (String) wrapper.getBody();
            // SDK超时需要触发实例查询确保不重复开通
            if (errCode == null || errCode.equals("SDK.ServerUnreachable")) {
                try {
                    // retry
                    DescribeInstancesResponse.Instance instance = aliyunInstance.getStoppedInstance(regionId, hostname);
                    instanceId = instance.getInstanceId();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (StringUtils.isEmpty(instanceId)) return false;
        CloudInstanceTaskMemberBO cloudInstanceTaskMemberBO = CloudInstanceTaskMemberBO.builder()
                .taskId(taskId)
                .instanceId(instanceId)
                .seq(serverBO.getSeq())
                .regionId(createCloudInstanceBO.getCloudInstanceTemplate().getRegionId())
                .hostname(hostname)
                .taskPhase(CloudInstanceTaskPhase.CREATE_INSTANCE.getPhase())
                .detail(JSON.toJSONString(serverBO))
                .zoneId(ocCloudVpcVswitchService.queryOcCloudVpcVswitchByVswitchId(serverBO.getVswitchId()).getZoneId())
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
                        member.setTaskPhase(CloudInstanceTaskPhase.ALLOCATE_PUBLIC_IP_ADDRESS.getPhase());
                        member.setPublicIp(publicIp);
                        ocCloudInstanceTaskMemberService.updateOcCloudInstanceTaskMember(member);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                member.setTaskPhase(CloudInstanceTaskPhase.ALLOCATE_PUBLIC_IP_ADDRESS.getPhase());
                ocCloudInstanceTaskMemberService.updateOcCloudInstanceTaskMember(member);
            }
        }
    }

    public void startInstanceHandler(List<OcCloudInstanceTaskMember> memberList) {
        for (OcCloudInstanceTaskMember member : memberList) {
            try {
                if (aliyunInstance.startInstance(member.getRegionId(), member.getInstanceId())) {
                    member.setTaskPhase(CloudInstanceTaskPhase.STARTING.getPhase());
                    ocCloudInstanceTaskMemberService.updateOcCloudInstanceTaskMember(member);
                }
            } catch (Exception e) {
                e.printStackTrace();
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
        List<String> instanceIds = memberList.stream().map(OcCloudInstanceTaskMember::getInstanceId).collect(Collectors.toList());
        Map<String, OcCloudInstanceTaskMember> memberMap = getOcCloudInstanceTaskMemberMap(memberList);
        List<DescribeInstancesResponse.Instance> instanceList = aliyunInstance.getInstanceList(regionId, instanceIds);
        for (DescribeInstancesResponse.Instance instance : instanceList) {
            if (!memberMap.containsKey(instance.getInstanceId())) continue;
            OcCloudInstanceTaskMember member = memberMap.get(instance.getInstanceId());
            if (instance.getStatus().equalsIgnoreCase("Running")) {
                member.setTaskPhase(CloudInstanceTaskPhase.RUNNING.getPhase());
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

    /**
     * 录入云服务器表
     *
     * @param regionId
     * @param memberList
     */
    public void recordInstanceCloudServerHandler(String regionId, List<OcCloudInstanceTaskMember> memberList) {
        if (memberList.isEmpty()) return;
        ICloudServer iCloudServer = CloudServerFactory.getCloudServerByKey("AliyunECSCloudServer");
        for (OcCloudInstanceTaskMember member : memberList) {
            iCloudServer.record(regionId, member.getInstanceId());
            OcCloudServer ocCloudServer = ocCloudServerService.queryOcCloudServerByInstanceId(member.getInstanceId());
            if (ocCloudServer == null) continue;
            member.setTaskPhase(CloudInstanceTaskPhase.CLOUD_SERVER_RECORDED.getPhase());
            ocCloudInstanceTaskMemberService.updateOcCloudInstanceTaskMember(member);
        }
    }

    /**
     * 录入服务器表（此操作会触发服务器数据工厂同步多平台数据）
     *
     * @param memberList
     */
    public void recordInstanceServerHandler(List<OcCloudInstanceTaskMember> memberList) {
        if (memberList.isEmpty()) return;
        for (OcCloudInstanceTaskMember member : memberList) {
            ServerVO.Server server = new GsonBuilder().create().fromJson(member.getDetail(), ServerVO.Server.class);
            server.setPrivateIp(member.getPrivateIp());
            if (!StringUtils.isEmpty(member.getPublicIp()))
                server.setPublicIp(member.getPublicIp());
            OcCloudServer ocCloudServer = ocCloudServerService.queryOcCloudServerByInstanceId(member.getInstanceId());
            server.setCloudServerId(ocCloudServer.getId());
            // 录入服务器
            BusinessWrapper wrapper = serverFacade.addServer(server);
            if (wrapper.isSuccess()) {
                member.setTaskPhase(CloudInstanceTaskPhase.SERVER_RECORDED.getPhase());
            } else {
                member.setTaskPhase(CloudInstanceTaskPhase.FINALIZED.getPhase());
                member.setTaskStatus(CloudInstanceTaskStatus.UNSTABLE.getStatus());
                member.setErrorMsg(wrapper.getBody().toString());
            }
            ocCloudInstanceTaskMemberService.updateOcCloudInstanceTaskMember(member);
        }
    }


    public void taskFinalizedHandler(List<OcCloudInstanceTaskMember> memberList) {
        if (memberList.isEmpty()) return;
        for (OcCloudInstanceTaskMember member : memberList) {
            // TODO 前置校验
            // 结束子任务
            member.setTaskPhase(CloudInstanceTaskPhase.FINALIZED.getPhase());
            member.setTaskStatus(CloudInstanceTaskStatus.COMPLETED.getStatus());
            ocCloudInstanceTaskMemberService.updateOcCloudInstanceTaskMember(member);
        }
    }

    private Map<String, OcCloudInstanceTaskMember> getOcCloudInstanceTaskMemberMap(List<OcCloudInstanceTaskMember> memberList) {
        return memberList.stream().collect(Collectors.toMap(OcCloudInstanceTaskMember::getInstanceId, a -> a, (k1, k2) -> k1));
    }


}
