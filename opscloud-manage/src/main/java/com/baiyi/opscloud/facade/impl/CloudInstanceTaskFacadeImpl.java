package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.bo.CreateCloudInstanceBO;
import com.baiyi.opscloud.bo.ServerBO;
import com.baiyi.opscloud.common.base.CloudInstanceTaskPhase;
import com.baiyi.opscloud.common.base.CloudInstanceTaskStatus;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.TimeUtils;
import com.baiyi.opscloud.decorator.cloud.CloudInstanceTaskDecorator;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.vo.cloud.CloudInstanceTaskVO;
import com.baiyi.opscloud.domain.vo.cloud.CloudInstanceTemplateVO;
import com.baiyi.opscloud.facade.CloudInstanceTaskFacade;
import com.baiyi.opscloud.handler.CreateInstanceTaskHandler;
import com.baiyi.opscloud.service.cloud.OcCloudInstanceTaskMemberService;
import com.baiyi.opscloud.service.cloud.OcCloudInstanceTaskService;
import com.baiyi.opscloud.service.cloud.OcCloudVpcVswitchService;
import com.baiyi.opscloud.service.env.OcEnvService;
import com.baiyi.opscloud.service.server.OcServerGroupService;
import com.baiyi.opscloud.service.server.OcServerService;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.baiyi.opscloud.common.base.Global.ASYNC_POOL_TASK_EXECUTOR;

/**
 * @Author baiyi
 * @Date 2020/3/30 11:40 上午
 * @Version 1.0
 */
@Service
public class CloudInstanceTaskFacadeImpl implements CloudInstanceTaskFacade {

    @Resource
    private OcCloudInstanceTaskService ocCloudInstanceTaskService;

    @Resource
    private OcCloudInstanceTaskMemberService ocCloudInstanceTaskMemberService;

    @Resource
    private OcCloudVpcVswitchService ocCloudVpcVswitchService;

    @Resource
    private OcServerService ocServerService;

    @Resource
    private CreateInstanceTaskHandler createInstanceTaskHandler;

    @Resource
    private CloudInstanceTaskDecorator cloudInstanceTaskDecorator;

    @Resource
    private OcServerGroupService ocServerGroupService;

    @Resource
    private OcEnvService ocEnvService;

    public static final int TASK_TIMEOUT_MINUTE = 5;

    @Override
    @Async(value = ASYNC_POOL_TASK_EXECUTOR)
    public void doCreateInstanceTask(OcCloudInstanceTask ocCloudInstanceTask, CreateCloudInstanceBO createCloudInstanceBO) {
        List<String> vswitchIds = getVswitchIdList(createCloudInstanceBO);
        int taskId = ocCloudInstanceTask.getId();
        String regionId = createCloudInstanceBO.getCloudInstanceTemplate().getRegionId();
        // 当前可用区下的虚拟交换机地址池ip不足
        if (vswitchIds.isEmpty() || vswitchIds.size() < createCloudInstanceBO.getCreateCloudInstance().getCreateSize()) {
            saveCreateInstanceTaskError(ocCloudInstanceTask, "当前可用区下的虚拟交换机地址池ip不足");
            return;
        }
        Date taskStartDate = new Date();
        int maxSerialNumber = ocServerService.queryOcServerMaxSerialNumber(createCloudInstanceBO.getCreateCloudInstance().getServerGroupId()
                , createCloudInstanceBO.getCreateCloudInstance().getEnvType());

        // 任务总时长限制 每实例5分钟上限  （10 + (n-1)*2 )
        boolean isTaskFinalized = false;

        while (!isTaskFinalized) {
            int taskMemberSize = ocCloudInstanceTaskMemberService.countOcCloudInstanceTaskMemberByTaskId(taskId);
            // 创建实例
            if (taskMemberSize < createCloudInstanceBO.getCreateCloudInstance().getCreateSize()) {
                // 执行创建实例
                int seq = taskMemberSize + 1;
                String vswitchId = vswitchIds.get(0);
                int serialNumber = maxSerialNumber + seq;
                ServerBO serverBO = ServerBO.builder()
                        .name(getServerName(createCloudInstanceBO))
                        .serverGroupId(createCloudInstanceBO.getCreateCloudInstance().getServerGroupId())
                        .loginType(createCloudInstanceBO.getCreateCloudInstance().getLoginType())
                        .loginUser(createCloudInstanceBO.getCreateCloudInstance().getLoginUser())
                        .envType(createCloudInstanceBO.getCreateCloudInstance().getEnvType())
                        .serverType(createCloudInstanceBO.getCloudInstanceTemplate().getCloudType())
                        .serialNumber(serialNumber)
                        .hostname(getHostname(serialNumber, createCloudInstanceBO))
                        .seq(seq)
                        .vswitchId(vswitchId)
                        .build();
                if (createInstanceTaskHandler.createInstanceHandler(taskId, createCloudInstanceBO, serverBO))
                    vswitchIds.remove(0);
            }
            // 分配公网
            createInstanceTaskHandler.allocatePublicIpAddressHandler(queryTaskMember(taskId, CloudInstanceTaskPhase.CREATE_INSTANCE.getPhase()
            ), createCloudInstanceBO.getCreateCloudInstance().getAllocatePublicIpAddress());
            // 启动实例
            createInstanceTaskHandler.startInstanceHandler(queryTaskMember(taskId,
                    CloudInstanceTaskPhase.ALLOCATE_PUBLIC_IP_ADDRESS.getPhase()));
            // 查询实例是否正常运行
            createInstanceTaskHandler.describeInstanceStatusHandler(regionId, queryTaskMember(taskId,
                    CloudInstanceTaskPhase.STARTING.getPhase()));
            // 录入实例信息到云服务器表
            createInstanceTaskHandler.recordInstanceCloudServerHandler(regionId, queryTaskMember(taskId,
                    CloudInstanceTaskPhase.RUNNING.getPhase()));
            // 录入服务器信息
            createInstanceTaskHandler.recordInstanceServerHandler(queryTaskMember(taskId,
                    CloudInstanceTaskPhase.CLOUD_SERVER_RECORDED.getPhase()));
            // 任务结束
            createInstanceTaskHandler.taskFinalizedHandler(queryTaskMember(taskId,
                    CloudInstanceTaskPhase.SERVER_RECORDED.getPhase()));
            // 校验任务是否完成
            isTaskFinalized = checkTaskCompleted(ocCloudInstanceTask, createCloudInstanceBO);
            // 校验任务是否超时
            if (!isTaskFinalized)
                isTaskFinalized = checkTaskTimeout(ocCloudInstanceTask, taskStartDate);
        }
    }

    // COMPLETED
    private boolean checkTaskCompleted(OcCloudInstanceTask ocCloudInstanceTask, CreateCloudInstanceBO createCloudInstanceBO) {
        List<OcCloudInstanceTaskMember> memberList = queryTaskMember(ocCloudInstanceTask.getId(),
                CloudInstanceTaskPhase.FINALIZED.getPhase());
        if (memberList.size() == createCloudInstanceBO.getCreateCloudInstance().getCreateSize()) {
            ocCloudInstanceTask.setTaskPhase(CloudInstanceTaskPhase.FINALIZED.getPhase());
            ocCloudInstanceTask.setTaskStatus(CloudInstanceTaskStatus.COMPLETED.getStatus());
            ocCloudInstanceTaskService.updateOcCloudInstanceTask(ocCloudInstanceTask);
            return true;
        }
        return false;
    }

    private boolean checkTaskTimeout(OcCloudInstanceTask ocCloudInstanceTask, Date taskStartDate) {
        if (TimeUtils.calculateDateAgoMinute(taskStartDate) >= TASK_TIMEOUT_MINUTE) {
            saveCreateInstanceTaskError(ocCloudInstanceTask, "任务超时: > " + TASK_TIMEOUT_MINUTE + "分钟");
            return true;
        }
        return false;
    }

    private List<OcCloudInstanceTaskMember> queryTaskMember(int taskId, String taskPhase) {
        return ocCloudInstanceTaskMemberService.queryOcCloudInstanceTaskMemberByTaskIdAndPhase(taskId, taskPhase);
    }

    // task错误保存
    private void saveCreateInstanceTaskError(OcCloudInstanceTask ocCloudInstanceTask, String errorMsg) {
        ocCloudInstanceTask.setTaskPhase("FINALIZED");
        ocCloudInstanceTask.setTaskStatus("ERROR");
        ocCloudInstanceTask.setErrorMsg(errorMsg);
        ocCloudInstanceTaskService.updateOcCloudInstanceTask(ocCloudInstanceTask);
    }

    private boolean include(String vswitchId, List<CloudInstanceTemplateVO.VSwitch> vswitchs) {
        for (CloudInstanceTemplateVO.VSwitch vsw : vswitchs) {
            if (vswitchId.equals(vsw.getVswitchId())) return true;
        }
        return false;
    }

    // 生成指定长度的轮询虚拟交换机列表，用于创建实例
    private List<String> getVswitchIdList(CreateCloudInstanceBO createCloudInstanceBO) {
        int size = createCloudInstanceBO.getCreateCloudInstance().getCreateSize();
        List<String> vswitchIds = Lists.newArrayList();
        List<OcCloudVpcVswitch> vswitchList;
        // 自动
        if (createCloudInstanceBO.getCreateCloudInstance().getZonePattern().equalsIgnoreCase("auto")) {
            vswitchList = getWswitchListByAuto(createCloudInstanceBO.getCloudInstanceTemplate());
        } else {
            vswitchList = queryVswitchByVpcIdAndVswitchIds(createCloudInstanceBO.getCloudInstanceTemplate().getVpcId(), createCloudInstanceBO.getCreateCloudInstance().getVswitchIds());
        }

        if(CollectionUtils.isEmpty(vswitchList)) return vswitchIds;

        while (vswitchIds.size() < size) {
            for (OcCloudVpcVswitch ocCloudVpcVswitch : vswitchList) {
                // 预留可用ip
                if (ocCloudVpcVswitch.getAvailableIpAddressCount() <= 10) {
                    vswitchList.remove(ocCloudVpcVswitch);
                    break;
                } else {
                    ocCloudVpcVswitch.setAvailableIpAddressCount(ocCloudVpcVswitch.getAvailableIpAddressCount() - 1);
                    vswitchIds.add(ocCloudVpcVswitch.getVswitchId());
                }
                if (vswitchIds.size() >= size) break;
            }
        }
        return vswitchIds;
    }

    private List<OcCloudVpcVswitch> getWswitchListByAuto(CloudInstanceTemplateVO.CloudInstanceTemplate cloudInstanceTemplate) {
        return queryVswitchByVpcIdAndZoneIds(cloudInstanceTemplate.getVpcId(), cloudInstanceTemplate.getInstanceZones())
                .stream().filter(e ->
                        include(e.getVswitchId(), cloudInstanceTemplate.getInstanceTemplate().getVswitchs())
                ).collect(Collectors.toList());
    }


    /**
     * 查询可用的虚拟交换机列表
     *
     * @param vpcId
     * @param instanceZones
     * @return
     */
    private List<OcCloudVpcVswitch> queryVswitchByVpcIdAndZoneIds(String vpcId, List<CloudInstanceTemplateVO.InstanceZone> instanceZones) {
        List<String> zoneIds = Lists.newArrayList();
        for (CloudInstanceTemplateVO.InstanceZone instanceZone : instanceZones) {
            if (!instanceZone.isActive()) continue;
            zoneIds.add(instanceZone.getZoneId());
        }
        return ocCloudVpcVswitchService.queryOcCloudVpcVswitchByVpcIdAndZoneIds(vpcId, zoneIds);
    }

    /**
     * 按虚拟交换机id查询
     *
     * @param vpcId
     * @param vswitchIds
     * @return
     */
    private List<OcCloudVpcVswitch> queryVswitchByVpcIdAndVswitchIds(String vpcId, List<String> vswitchIds) {
        List<OcCloudVpcVswitch> vswitches = Lists.newArrayList();
        for (String vswitchId : vswitchIds) {
            OcCloudVpcVswitch ocCloudVpcVswitch = ocCloudVpcVswitchService.queryOcCloudVpcVswitchByVswitchId(vswitchId);
            if (ocCloudVpcVswitch != null && vpcId.equals(ocCloudVpcVswitch.getVpcId()))
                vswitches.add(ocCloudVpcVswitch);
        }
        return vswitches;
    }

    @Override
    public CloudInstanceTaskVO.CloudInstanceTask queryCloudInstanceTask(int taskId) {
        OcCloudInstanceTask ocCloudInstanceTask = ocCloudInstanceTaskService.queryOcCloudInstanceTaskById(taskId);
        return getCloudInstanceTask(ocCloudInstanceTask);
    }

    @Override
    public CloudInstanceTaskVO.CloudInstanceTask queryLastCloudInstanceTask(int templateId) {
        OcCloudInstanceTask ocCloudInstanceTask = ocCloudInstanceTaskService.queryLastOcCloudInstanceTaskByTemplateId(templateId);
        return getCloudInstanceTask(ocCloudInstanceTask);
    }

    private CloudInstanceTaskVO.CloudInstanceTask getCloudInstanceTask(OcCloudInstanceTask ocCloudInstanceTask) {
        if (ocCloudInstanceTask == null)
            return new CloudInstanceTaskVO.CloudInstanceTask();
        CloudInstanceTaskVO.CloudInstanceTask cloudInstanceTask = BeanCopierUtils.copyProperties(ocCloudInstanceTask, CloudInstanceTaskVO.CloudInstanceTask.class);
        return cloudInstanceTaskDecorator.decorator(cloudInstanceTask);
    }


    private String getServerName(CreateCloudInstanceBO createCloudInstanceBO) {
        String serverName;
        if (!StringUtils.isEmpty(createCloudInstanceBO.getCreateCloudInstance().getServerName())) {
            serverName = createCloudInstanceBO.getCreateCloudInstance().getServerName();
        } else {
            OcServerGroup ocServerGroup = ocServerGroupService.queryOcServerGroupById(createCloudInstanceBO.getCreateCloudInstance().getServerGroupId());
            serverName = ocServerGroup.getName().replace("group_", "");
        }
        return serverName;
    }

    /**
     * 获取实例的主机名
     *
     * @param serialNumber
     * @param createCloudInstanceBO
     * @return
     */
    private String getHostname(int serialNumber, CreateCloudInstanceBO createCloudInstanceBO) {
        String serverName = getServerName(createCloudInstanceBO);
        OcEnv ocEnv = ocEnvService.queryOcEnvByType(createCloudInstanceBO.getCreateCloudInstance().getEnvType());
        return Joiner.on("-").skipNulls().join(serverName, !ocEnv.getEnvName().equals("prod") ? ocEnv.getEnvName() : null, serialNumber);
    }

}
