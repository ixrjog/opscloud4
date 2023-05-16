package com.baiyi.opscloud.datasource.ansible.task;

import com.baiyi.opscloud.common.base.AnsibleResult;
import com.baiyi.opscloud.common.base.ServerTaskStatusEnum;
import com.baiyi.opscloud.common.base.ServerTaskStopType;
import com.baiyi.opscloud.common.util.NewTimeUtil;
import com.baiyi.opscloud.datasource.ansible.TaskStatus;
import com.baiyi.opscloud.datasource.ansible.exception.TaskTimeoutException;
import com.baiyi.opscloud.datasource.ansible.executor.TaskExecutor;
import com.baiyi.opscloud.datasource.ansible.executor.TaskExecutorBuilder;
import com.baiyi.opscloud.datasource.ansible.recorder.TaskLogStorehouse;
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

    public static final Long MAX_TIMEOUT = NewTimeUtil.MINUTE_TIME * 100;

    private final ServerTaskMember serverTaskMember;

    private final CommandLine commandLine;

    /**
     * 任务UUID
     */
    private final String taskUuid;

    private final ServerTaskMemberService serverTaskMemberService;

    private final TaskLogStorehouse taskLogStorehouse;

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
        long startTaskTime = System.currentTimeMillis();
        while (true) {
            // 等待执行
            resultHandler.waitFor(NewTimeUtil.SECOND_TIME);
            // 执行日志
            taskLogStorehouse.recorderLog(taskUuid, serverTaskMember, taskExecutor);
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
                if (NewTimeUtil.isTimeout(startTaskTime, MAX_TIMEOUT)) {
                    log.warn("Ansible task timeout: startTaskTime={}, maxTimeout={}", startTaskTime, MAX_TIMEOUT);
                    taskExecutor.killedProcess(); // kill
                    taskLogStorehouse.recorderLog(taskUuid, serverTaskMember, taskExecutor);
                    throw new TaskTimeoutException();
                }
            }
        }
    }

    @Override
    public void run() {
        try {
            final DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
            // 任务执行器
            TaskExecutor taskExecutor = TaskExecutorBuilder.build(MAX_TIMEOUT);
            taskExecutor.execute(commandLine, resultHandler);
            resultHandler.waitFor(NewTimeUtil.SECOND_TIME * 5);
            // 修改任务状态
            if (serverTaskMember.getTaskStatus().equals(ServerTaskStatusEnum.QUEUE.name())) {
                serverTaskMember.setStartTime(new Date());
                serverTaskMember.setTaskStatus(ServerTaskStatusEnum.EXECUTING.name());
                serverTaskMemberService.update(serverTaskMember);
                log.info("任务启动: taskUuid={}, serverTaskMemberId={}, taskStatus={}", taskUuid, serverTaskMember.getId(), serverTaskMember.getTaskStatus());
            }
            watching(taskExecutor, resultHandler);
        } catch (TaskTimeoutException e) {
            TaskStatus taskStatus = TaskStatus.builder()
                    .stopType(ServerTaskStopType.TIMEOUT_STOP.getType())
                    .taskResult("TIMEOUT")
                    .build();
            save(taskStatus);
        } catch (ExecuteException e) {
            log.error(e.getMessage());
            TaskStatus taskStatus = TaskStatus.builder()
                    .stopType(ServerTaskStopType.ERROR_STOP.getType())
                    .exitValue(e.getExitValue())
                    .taskResult(AnsibleResult.FAILED.getName())
                    .build();
            save(taskStatus);
        } catch (InterruptedException | IOException e) {
            // 日志流转码错误 暂不处理
            log.warn(e.getMessage());
        }
    }

    /**
     * 最后记录
     *
     * @param taskExecutor
     * @param taskStatus
     */
    private void save(TaskExecutor taskExecutor, TaskStatus taskStatus) {
        taskLogStorehouse.recorderLog(taskUuid, serverTaskMember, taskExecutor);
        save(taskStatus);
    }

    private void save(TaskStatus taskStatus) {
        serverTaskMember.setFinalized(taskStatus.getFinalized());
        serverTaskMember.setExitValue(taskStatus.getExitValue());
        serverTaskMember.setStopType(taskStatus.getStopType());
        serverTaskMember.setTaskStatus(taskStatus.getTaskStatus());
        serverTaskMember.setEndTime(new Date());
        if (!StringUtils.isEmpty(taskStatus.getTaskResult())) {
            serverTaskMember.setTaskResult(taskStatus.getTaskResult());
        }
        serverTaskMemberService.update(serverTaskMember);
    }

}
