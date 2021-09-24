package com.baiyi.opscloud.ansible.recorder;

import com.baiyi.opscloud.ansible.executor.TaskExecutor;
import com.baiyi.opscloud.common.config.OpscloudConfig;
import com.baiyi.opscloud.common.util.IOUtil;
import com.google.common.base.Joiner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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
    private OpscloudConfig opscloudConfig;

    public interface Logs {
        String OUTPUT_LOG = "task_output.log";
        String ERROR_LOG = "task_error.log";
    }

    /**
     * 记录日志(追加写入)
     *
     * @param serverTaskMemberId
     * @param taskExecutor
     */
    public void recorderLog(String taskUuid, int serverTaskMemberId, TaskExecutor taskExecutor) {
        // 追加写入output日志
        IOUtil.appendFile(taskExecutor.getOutputMsg(), buildOutputLogPath(taskUuid, serverTaskMemberId));
        // 追加写入error日志
        IOUtil.appendFile(taskExecutor.getErrorMsg(), buildErrorLogPath(taskUuid, serverTaskMemberId));
    }

    public String buildOutputLogPath(String taskUuid, int serverTaskMemberId) {
        return Joiner.on("/").join(buildBaseLogPath(taskUuid, serverTaskMemberId), Logs.OUTPUT_LOG);
    }

    public String buildErrorLogPath(String taskUuid, int serverTaskMemberId) {
        return Joiner.on("/").join(buildBaseLogPath(taskUuid, serverTaskMemberId), Logs.ERROR_LOG);
    }

    private String buildBaseLogPath(String taskUuid, int serverTaskMemberId) {
        return Joiner.on("/").join(opscloudConfig.getServerTaskLogPath(), taskUuid, serverTaskMemberId);
    }


}
