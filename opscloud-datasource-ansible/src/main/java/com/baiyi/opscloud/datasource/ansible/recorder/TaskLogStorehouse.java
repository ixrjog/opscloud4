package com.baiyi.opscloud.datasource.ansible.recorder;

import com.baiyi.opscloud.datasource.ansible.executor.TaskExecutor;
import com.baiyi.opscloud.common.configuration.properties.OpscloudConfigurationProperties;
import com.baiyi.opscloud.common.util.IOUtil;
import com.baiyi.opscloud.domain.generator.opscloud.ServerTaskMember;
import com.google.common.base.Joiner;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

/**
 * 日志路径 opscloud-data/logs/serverTask/${taskUuid}/${serverTaskMemberId}
 *
 * @Author baiyi
 * @Date 2021/9/22 5:52 下午
 * @Version 1.0
 */
@Component
public class TaskLogStorehouse {

    @Resource
    private OpscloudConfigurationProperties opscloudConfig;

    public interface Logs {
        String OUTPUT_LOG = "task_output.log";
        String ERROR_LOG = "task_error.log";
    }

    /**
     * 记录日志(追加写入)
     *
     * @param serverTaskMember
     * @param taskExecutor
     */
    public void recorderLog(String taskUuid, ServerTaskMember serverTaskMember, TaskExecutor taskExecutor) {
        // 追加写入output日志
        IOUtil.appendFile(taskExecutor.getOutputMsg(), buildOutputLogPath(taskUuid, serverTaskMember));
        // 追加写入error日志
        IOUtil.appendFile(taskExecutor.getErrorMsg(), buildErrorLogPath(taskUuid, serverTaskMember));
    }

    public String buildOutputLogPath(String taskUuid, ServerTaskMember serverTaskMember) {
        return Joiner.on("/").join(buildBaseLogPath(taskUuid, serverTaskMember), Logs.OUTPUT_LOG);
    }

    public String buildErrorLogPath(String taskUuid, ServerTaskMember serverTaskMember) {
        return Joiner.on("/").join(buildBaseLogPath(taskUuid, serverTaskMember), Logs.ERROR_LOG);
    }

    private String buildBaseLogPath(String taskUuid, ServerTaskMember serverTaskMember) {
        return Joiner.on("/").join(opscloudConfig.getServerTaskLogPath(), taskUuid, serverTaskMember.getServerName());
    }

}
