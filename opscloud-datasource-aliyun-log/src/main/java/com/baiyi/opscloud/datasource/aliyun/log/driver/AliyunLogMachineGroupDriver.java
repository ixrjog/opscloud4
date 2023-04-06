package com.baiyi.opscloud.datasource.aliyun.log.driver;

import com.baiyi.opscloud.datasource.aliyun.log.driver.base.AbstractAliyunLogDriver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2020/6/13 11:47 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class AliyunLogMachineGroupDriver extends AbstractAliyunLogDriver {

    public static final String MACHINE_IDENTIFY_TYPE = "ip";

//    public MachineGroup getMachineGroup(AliyunConfig.Aliyun aliyun, String project, String groupName) {
//        GetMachineGroupRequest req = new GetMachineGroupRequest(project, groupName);
//        try {
//            Client client = buildClient(aliyun);
//            return client.GetMachineGroup(req).GetMachineGroup();
//        } catch (LogException lg) {
//            log.error("阿里云日志服务查询MachineGroup错误: {}", lg.GetErrorMessage());
//        }
//        return null;
//    }

//    public List<String> getMachineGroups(AliyunConfig.Aliyun aliyun, String project, String groupName) {
//        int offset = 0;
//        ListMachineGroupRequest req = new ListMachineGroupRequest(project, groupName, offset, QUERY_SIZE);
//        List<String> machineGroups = new ArrayList<>();
//        try {
//            Client client = buildClient(aliyun);
//            machineGroups = client.ListMachineGroup(req).GetMachineGroups();
//        } catch (LogException lg) {
//            log.error("阿里云日志服务查询MachineGroup错误: {}", lg.GetErrorMessage());
//        }
//        return machineGroups;
//    }

//    public void updateMachineGroup(AliyunConfig.Aliyun aliyun, AliyunLogMemberVO.LogMember logMember) {
//        MachineGroup machineGroup = new MachineGroup(logMember.getServerGroupName(), MACHINE_IDENTIFY_TYPE, logMember.getMachineList());
//        machineGroup.SetGroupTopic(StringUtils.isEmpty(logMember.getTopic()) ? logMember.getServerGroupName() : logMember.getTopic());
//        try {
//            Client client = buildClient(aliyun);
//            UpdateMachineGroupRequest req = new UpdateMachineGroupRequest(logMember.getLog().getProject(), machineGroup);
//            client.UpdateMachineGroup(req);
//        } catch (LogException lg) {
//            log.error("阿里云日志服务更新MachineGroup错误: {}", lg.GetErrorMessage());
//        }
//    }

//    public void createMachineGroup(AliyunConfig.Aliyun aliyun, AliyunLogMemberVO.LogMember logMember) {
//        MachineGroup machineGroup = new MachineGroup(logMember.getServerGroupName(), MACHINE_IDENTIFY_TYPE, logMember.getMachineList());
//        machineGroup.SetGroupTopic(StringUtils.isEmpty(logMember.getTopic()) ? logMember.getServerGroupName() : logMember.getTopic());
//        try {
//            Client client = buildClient(aliyun);
//            CreateMachineGroupRequest req = new CreateMachineGroupRequest(logMember.getLog().getProject(), machineGroup);
//            client.CreateMachineGroup(req);
//        } catch (LogException lg) {
//            log.error("阿里云日志服务创建MachineGroup错误: {}", lg.GetErrorMessage());
//        }
//    }

    /**
     * 将配置应用到机器组
     *
     * @param logMember
     * @return
     */
//    private void applyConfigToMachineGroup(AliyunConfig.Aliyun aliyun, AliyunLogMemberVO.LogMember logMember) {
//        ApplyConfigToMachineGroupRequest req = new ApplyConfigToMachineGroupRequest(logMember.getLog().getProject(), logMember.getServerGroup().getName(), logMember.getLog().getConfig());
//        try {
//            Client client = buildClient(aliyun);
//            client.ApplyConfigToMachineGroup(req);
//        } catch (LogException lg) {
//            log.error(lg.getMessage());
//        }
//    }

}
