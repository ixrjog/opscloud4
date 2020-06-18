package com.baiyi.opscloud.ansible.handler;

import com.baiyi.opscloud.ansible.bo.MemberExecutorLogBO;
import com.baiyi.opscloud.ansible.config.AnsibleConfig;
import com.baiyi.opscloud.ansible.executor.ExecutorEngine;
import com.baiyi.opscloud.common.base.ServerTaskStopType;
import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerTaskMember;
import com.google.common.base.Joiner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/4/17 5:53 下午
 * @Version 1.0
 */
@Component
public class TaskLogRecorder {

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private AnsibleConfig ansibleConfig;

    // 日志缓存路径
    private String getTaskMemberLogKey(int memberId) {
        return Joiner.on("_").join("taskLogRecorder", "memberId", memberId);
    }

    private String getAbortTaskMemberKey(int memberId) {
        return Joiner.on("_").join("abortTaskMember", "memberId", memberId);
    }

    private String getAbortTaskKey(int taskId) {
        return Joiner.on("_").join("abortTask", "taskId", taskId);
    }

    public void recorderLog(int memberId, ExecutorEngine executorEngine) {
        MemberExecutorLogBO log = new MemberExecutorLogBO();
        log.setMemberId(memberId);
        log.setOutputMsg(executorEngine.getOutputMsg());
        log.setErrorMsg(executorEngine.getErrorMsg());
        redisUtil.set(getTaskMemberLogKey(memberId), log, AnsibleExecutorHandler.MAX_TIMEOUT);
    }

    public MemberExecutorLogBO getLog(int memberId) {
        String key = getTaskMemberLogKey(memberId);
        if (redisUtil.hasKey(key)) {
            return (MemberExecutorLogBO) redisUtil.get(key);
        } else {
            return null;
        }
    }

    public void clearLog(int memberId) {
        redisUtil.del(getTaskMemberLogKey(memberId));
    }

    public String getOutputLogPath(OcServerTaskMember member) {
        return Joiner.on("/").join(ansibleConfig.getPlaybookLogPath(member.getTaskId()), member.getId() + "_output.log");
    }

    public String getErrorLogPath(OcServerTaskMember member) {
        return Joiner.on("/").join(ansibleConfig.getPlaybookLogPath(member.getTaskId()), member.getId() + "_error.log");
    }

    public void abortTask(int taskId) {
        String key = getAbortTaskKey(taskId);
        redisUtil.set(key, ServerTaskStopType.SERVER_TASK_STOP.getType(), 5 * 60);
    }

    public int getAbortTask(int taskId) {
        String key = getAbortTaskKey(taskId);
        if (redisUtil.hasKey(key)) {
            return (int) redisUtil.get(key);
        } else {
            return 0;
        }
    }


    public void abortTaskMember(int memberId, int stopType) {
        String key = getAbortTaskMemberKey(memberId);
        redisUtil.set(key, stopType, 5 * 60);
    }

    public int getAbortTaskMember(int memberId) {
        String key = getAbortTaskMemberKey(memberId);
        if (redisUtil.hasKey(key)) {
            return (int) redisUtil.get(key);
        } else {
            return 0;
        }
    }

}
