package com.baiyi.opscloud.cloud.log.impl;

import com.aliyun.openservices.log.common.MachineGroup;
import com.aliyun.openservices.log.common.Project;
import com.baiyi.opscloud.aliyun.log.handler.AliyunLogHandler;
import com.baiyi.opscloud.aliyun.log.handler.AliyunLogMachineGroupHandler;
import com.baiyi.opscloud.cloud.log.AliyunLogCenter;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.vo.cloud.AliyunLogMemberVO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/13 1:43 下午
 * @Version 1.0
 */
@Component
public class AliyunLogCenterImpl implements AliyunLogCenter {

    @Resource
    private AliyunLogHandler aliyunLogHandler;

    @Resource
    private AliyunLogMachineGroupHandler aliyunLogMachineGroupHandler;

    @Override
    public List<Project> getProjects(String project) {
        return aliyunLogHandler.getProjects(project);
    }

    @Override
    public List<String> getLogStores(String projectName) {
        return aliyunLogHandler.getLogStores(projectName);
    }

    @Override
    public List<String> getConfigs(String projectName, String logstoreName) {
        return aliyunLogHandler.getConfigs(projectName, logstoreName);
    }

    @Override
    public BusinessWrapper<Boolean> pushMachineGroup(AliyunLogMemberVO.LogMember logMember) {
        MachineGroup machineGroup = aliyunLogMachineGroupHandler.getMachineGroup(logMember.getLog().getProject(), logMember.getServerGroupName());
        if (machineGroup == null) {
            return aliyunLogMachineGroupHandler.addMachineGroup(logMember);
        } else {
            return aliyunLogMachineGroupHandler.updateMachineGroup(logMember);
        }
    }
}
