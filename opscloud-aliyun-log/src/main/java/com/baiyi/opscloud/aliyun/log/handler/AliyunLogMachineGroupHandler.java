package com.baiyi.opscloud.aliyun.log.handler;

import com.aliyun.openservices.log.common.MachineGroup;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.request.*;
import com.baiyi.opscloud.aliyun.log.base.BaseAliyunLog;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.vo.cloud.AliyunLogMemberVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/13 11:47 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class AliyunLogMachineGroupHandler extends BaseAliyunLog {

    public static final String MACHINE_IDENTIFY_TYPE = "ip";

    private static final boolean ADD_MACHINE_GROUP = true;

    private static final boolean UPDATE_MACHINE_GROUP = false;

    public MachineGroup getMachineGroup(String project, String groupName) {
        GetMachineGroupRequest req = new GetMachineGroupRequest(project, groupName);
        try {
            return client.GetMachineGroup(req).GetMachineGroup();
        } catch (LogException lg) {
            log.error("阿里云日志服务查询MachineGroup错误! , {}", lg.GetErrorMessage());
        }
        return null;
    }

    public List<String> getMachineGroups(String project, String groupName) {
        int offset = 0;
        ListMachineGroupRequest req = new ListMachineGroupRequest(project, groupName, offset, QUERY_SIZE);
        List<String> machineGroups = new ArrayList<>();
        try {
            machineGroups = client.ListMachineGroup(req).GetMachineGroups();
        } catch (LogException lg) {

        }
        return machineGroups;
    }

    public BusinessWrapper<Boolean> updateMachineGroup(AliyunLogMemberVO.LogMember logMember) {
        MachineGroup machineGroup = new MachineGroup(logMember.getServerGroupName(), MACHINE_IDENTIFY_TYPE, logMember.getMachineList());
        machineGroup.SetGroupTopic(StringUtils.isEmpty(logMember.getTopic()) ? logMember.getServerGroupName() : logMember.getTopic());
        return saveMachineGroup(logMember, machineGroup, UPDATE_MACHINE_GROUP);
    }

    public BusinessWrapper<Boolean> addMachineGroup(AliyunLogMemberVO.LogMember logMember) {
        MachineGroup machineGroup = new MachineGroup(logMember.getServerGroupName(), MACHINE_IDENTIFY_TYPE, logMember.getMachineList());
        machineGroup.SetGroupTopic(StringUtils.isEmpty(logMember.getTopic()) ? logMember.getServerGroupName() : logMember.getTopic());
        return saveMachineGroup(logMember, machineGroup, ADD_MACHINE_GROUP);
    }

    private BusinessWrapper<Boolean> saveMachineGroup(AliyunLogMemberVO.LogMember logMember, MachineGroup machineGroup, boolean action) {
        try {
            if (action) {
                CreateMachineGroupRequest req = new CreateMachineGroupRequest(logMember.getLog().getProject(), machineGroup);
                client.CreateMachineGroup(req);
            } else {
                UpdateMachineGroupRequest req = new UpdateMachineGroupRequest(logMember.getLog().getProject(), machineGroup);
                client.UpdateMachineGroup(req);
            }
            applyConfigToMachineGroup(logMember);
            return BusinessWrapper.SUCCESS;
        } catch (LogException lg) {
            return new BusinessWrapper(10000,lg.GetErrorMessage());
        }
    }

    /**
     * 将配置应用到机器组
     *
     * @param logMember
     * @return
     */
    private boolean applyConfigToMachineGroup(AliyunLogMemberVO.LogMember logMember) {
        ApplyConfigToMachineGroupRequest req = new ApplyConfigToMachineGroupRequest(logMember.getLog().getProject(), logMember.getServerGroup().getName(), logMember.getLog().getConfig());
        try {
            client.ApplyConfigToMachineGroup(req);
            return true;
        } catch (LogException lg) {
            lg.printStackTrace();
            return false;
        }
    }

}
