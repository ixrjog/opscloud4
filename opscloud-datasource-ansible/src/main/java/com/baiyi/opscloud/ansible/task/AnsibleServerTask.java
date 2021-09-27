package com.baiyi.opscloud.ansible.task;

import com.baiyi.opscloud.ansible.TaskStatus;
import com.baiyi.opscloud.ansible.exception.TaskTimeoutException;
import com.baiyi.opscloud.ansible.executor.TaskExecutor;
import com.baiyi.opscloud.ansible.executor.TaskExecutorBuilder;
import com.baiyi.opscloud.ansible.recorder.TaskLogStorehouse;
import com.baiyi.opscloud.common.base.AnsibleResult;
import com.baiyi.opscloud.common.base.ServerTaskStatusEnum;
import com.baiyi.opscloud.common.base.ServerTaskStopType;
import com.baiyi.opscloud.common.util.TimeUtil;
import com.baiyi.opscloud.domain.generator.opscloud.ServerTaskMember;
import com.baiyi.opscloud.service.task.ServerTaskMemberService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Date;

/**
 * @Author baiyi
 * @Date 2021/9/22 3:07 下午
 * @Version 1.0
 */
@Slf4j
public class AnsibleServerTask implements Runnable {

    public static final Long MAX_TIMEOUT = TimeUtil.minuteTime * 100;

    private ServerTaskMember serverTaskMember;

    private CommandLine commandLine;

    private String taskUuid; // 任务uuid

    private ServerTaskMemberService serverTaskMemberService;

    private TaskLogStorehouse taskLogStorehouse;

    public interface ExitValues {
        int OK = 0;
        int ERROR = 1;
    }

    public AnsibleServerTask(String taskUuid, ServerTaskMember serverTaskMember,
                             CommandLine commandLine,
                             ServerTaskMemberService serverTaskMemberService,
                             TaskLogStorehouse taskLogStorehouse) {
        this.taskUuid = taskUuid;
        this.serverTaskMember = serverTaskMember;
        this.commandLine = commandLine;
        this.serverTaskMemberService = serverTaskMemberService;
        this.taskLogStorehouse = taskLogStorehouse;
    }

    private void watching(TaskExecutor taskExecutor, DefaultExecuteResultHandler resultHandler) throws InterruptedException {
        // 启动时间
        Long startTaskTime = new Date().getTime();
        while (true) {
            resultHandler.waitFor(TimeUtil.secondTime); // 等待执行
            // 执行日志
            taskLogStorehouse.recorderLog(taskUuid, serverTaskMember.getId(), taskExecutor);
            // 任务结束
            if (resultHandler.hasResult()) {
                TaskStatus taskStatus = TaskStatus.builder()
                        .exitValue(resultHandler.getExitValue())
                        .taskResult(AnsibleResult.getName(resultHandler.getExitValue()))
                        .stopType(resultHandler.getExitValue() == ExitValues.OK ? ServerTaskStopType.COMPLETE_STOP.getType() : -1)
                        .build();
                save(taskExecutor, taskStatus);
                return;
            } else {
                // 判断任务是否需要终止或超时
                if (TimeUtil.checkTimeout(startTaskTime, MAX_TIMEOUT)) {
                    taskExecutor.killedProcess(); // kill
                    taskLogStorehouse.recorderLog(taskUuid, serverTaskMember.getId(), taskExecutor);
                    throw new TaskTimeoutException();
                }
            }
        }
    }

    @Override
    public void run() {
        try {
            final DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
            TaskExecutor taskExecutor = TaskExecutorBuilder.build(MAX_TIMEOUT); // 任务执行器
            taskExecutor.execute(commandLine, resultHandler);
            resultHandler.waitFor(TimeUtil.secondTime * 5);
            // 修改任务状态
            if (serverTaskMember.getTaskStatus().equals(ServerTaskStatusEnum.QUEUE.name())) {
                serverTaskMember.setStartTime(new Date());
                serverTaskMember.setTaskStatus(ServerTaskStatusEnum.EXECUTING.name());
                serverTaskMemberService.update(serverTaskMember);
                log.info("任务启动信息! taskUuid = {} , serverTaskMemberId = {} , taskStatus = {}", taskUuid, serverTaskMember.getId(), serverTaskMember.getTaskStatus());
            }
            watching(taskExecutor, resultHandler);
        } catch (TaskTimeoutException e) {
            e.printStackTrace();
            TaskStatus taskStatus = TaskStatus.builder()
                    .stopType(ServerTaskStopType.TIMEOUT_STOP.getType())
                    .taskResult("TIMEOUT")
                    .build();
            save(taskStatus);
        } catch (ExecuteException e) {
            e.printStackTrace();
            TaskStatus taskStatus = TaskStatus.builder()
                    .stopType(ServerTaskStopType.ERROR_STOP.getType())
                    .exitValue(e.getExitValue())
                    .taskResult(AnsibleResult.FAILED.getName())
                    .build();
            save(taskStatus);
        } catch (InterruptedException | IOException e) {
            // 日志流转码错误 暂不处理
            e.printStackTrace();
        }
    }

    /**
     * 最后记录
     *
     * @param taskExecutor
     * @param taskStatus
     */
    private void save(TaskExecutor taskExecutor, TaskStatus taskStatus) {
        taskLogStorehouse.recorderLog(taskUuid, serverTaskMember.getId(), taskExecutor);
        save(taskStatus);
    }

    private void save(TaskStatus taskStatus) {
        serverTaskMember.setFinalized(taskStatus.getFinalized());
        serverTaskMember.setExitValue(taskStatus.getExitValue());
        serverTaskMember.setStopType(taskStatus.getStopType());
        serverTaskMember.setTaskStatus(taskStatus.getTaskStatus());
        serverTaskMember.setEndTime(new Date());
        serverTaskMember.setOutputMsg(taskLogStorehouse.buildOutputLogPath(taskUuid, serverTaskMember.getId()));
        serverTaskMember.setErrorMsg(taskLogStorehouse.buildErrorLogPath(taskUuid, serverTaskMember.getId()));
        if (!StringUtils.isEmpty(taskStatus.getTaskResult()))
            serverTaskMember.setTaskResult(taskStatus.getTaskResult());
        serverTaskMemberService.update(serverTaskMember);
    }

}
