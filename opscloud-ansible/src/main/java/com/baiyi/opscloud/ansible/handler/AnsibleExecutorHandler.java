package com.baiyi.opscloud.ansible.handler;

import com.baiyi.opscloud.ansible.bo.MemberExecutorLogBO;
import com.baiyi.opscloud.ansible.bo.TaskResult;
import com.baiyi.opscloud.ansible.bo.TaskStatusBO;
import com.baiyi.opscloud.ansible.builder.ExecutorEngineBuilder;
import com.baiyi.opscloud.ansible.exception.TaskLogExceededLimit;
import com.baiyi.opscloud.ansible.exception.TaskStopException;
import com.baiyi.opscloud.ansible.exception.TaskTimeoutException;
import com.baiyi.opscloud.ansible.executor.ExecutorEngine;
import com.baiyi.opscloud.common.base.AnsibleResult;
import com.baiyi.opscloud.common.base.ServerTaskStatus;
import com.baiyi.opscloud.common.base.ServerTaskStopType;
import com.baiyi.opscloud.common.util.IOUtils;
import com.baiyi.opscloud.common.util.TimeUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerTaskMember;
import com.baiyi.opscloud.service.server.OcServerTaskMemberService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

import static com.baiyi.opscloud.common.base.Global.ASYNC_POOL_TASK_EXECUTOR;

/**
 * @Author baiyi
 * @Date 2020/4/6 6:23 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class AnsibleExecutorHandler {

    // 100 分钟
    public static final Long MAX_TIMEOUT = 6000000L;

    @Resource
    private OcServerTaskMemberService ocServerTaskMemberService;

    @Resource
    private TaskLogRecorder taskLogRecorder;

    /**
     * 512KB
     **/
    public static final int MAX_LOG_LENGTH = 512 * 1024;

    /**
     *  public static final String RESULT_UNREACHABLE = "UNREACHABLE";
     *  public static final String RESULT_SUCCESSFUL= "SUCCESSFUL";
     */

    /**
     * 阻塞方式运行
     *
     * @param commandLine
     * @param timeout
     * @return
     */
    public TaskResult executor(CommandLine commandLine, Long timeout) {
        if (timeout == 0)
            timeout = MAX_TIMEOUT;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ByteArrayOutputStream errorStream = new ByteArrayOutputStream();

            //CommandLine commandline = CommandLine.parse(command);

            DefaultExecutor exec = new DefaultExecutor();
            //创建监控时间X秒，超过X秒则中断执行
            ExecuteWatchdog watchdog = new ExecuteWatchdog(timeout);
            exec.setWatchdog(watchdog);
            exec.setExitValues(null);
            PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream, errorStream);
            exec.setStreamHandler(streamHandler);
            int exitCode = exec.execute(commandLine);
//            String out = outputStream.toString("utf8");
//            String error = errorStream.toString("utf8");
//            return out + error;
            return TaskResult.builder()
                    .exitValue(exitCode)
                    .outputStream(outputStream)
                    .errorStream(errorStream)
                    .build();
        } catch (Exception e) {
            TaskResult taskResult = TaskResult.FAILED;
            taskResult.setExceptionMsg(e.getMessage());
            return taskResult;
        }
    }

    @Async(value = ASYNC_POOL_TASK_EXECUTOR)
    public void executorTest(Long timeout) {
        if (timeout == 0)
            timeout = MAX_TIMEOUT;
        try {
            final CommandLine commandLine = CommandLine.parse("ping www.baidu.com");
            final ExecuteWatchdog watchdog = new ExecuteWatchdog(Integer.MAX_VALUE);
            final DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
            DefaultExecutor executor = new DefaultExecutor();

            executor.setWatchdog(watchdog);
            executor.execute(commandLine, resultHandler);

            while (true) {
                Thread.sleep(10000);//等进程执行一会，再终止它
                System.out.println("--> Watchdog is watching ? " + watchdog.isWatching());
                //watchdog.destroyProcess();//终止进程
                //System.out.println("--> destroyProcess done.");
                //System.out.println("--> Watchdog is watching ? " + watchdog.isWatching());
                System.out.println("--> Watchdog should have killed the process : " + watchdog.killedProcess());
                System.out.println("--> wait result is : " + resultHandler.hasResult());
                System.out.println("--> exit value is : " + resultHandler.getExitValue());
                System.out.println("--> exception is : " + resultHandler.getException());

                resultHandler.waitFor(1000);//等待5秒。下面加上上面的几个System.out，看看进程状态是什么。
            }

        } catch (Exception e) {
        }
    }

    /**
     * https://blog.csdn.net/doublesin/article/details/79082113
     *
     * @param timeout
     * @return
     */
    @Async(value = ASYNC_POOL_TASK_EXECUTOR)
    public void executorRecorder(OcServerTaskMember member, CommandLine commandLine, Long timeout) {
        if (timeout == 0)
            timeout = MAX_TIMEOUT;
        try {
            final DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
            ExecutorEngine executorEngine = ExecutorEngineBuilder.build(timeout);
            executorEngine.execute(commandLine, resultHandler);
            resultHandler.waitFor(1000);
            // 启动时间
            Long startTaskTime = new Date().getTime();

            // 判断任务是否执行
            if (member.getTaskStatus().equals(ServerTaskStatus.QUEUE.getStatus())) {
                resultHandler.waitFor(500);
                if (executorEngine.isWatching()) {
                    log.info("任务启动成功! id = {} ; isWatching = {} ; taskStatus = {}", member.getId(), executorEngine.isWatching(), member.getTaskStatus());
                    member.setTaskStatus(ServerTaskStatus.EXECUTING.getStatus());
                    ocServerTaskMemberService.updateOcServerTaskMember(member);
                } else {
                    log.info("任务启动失败! id = {} ; isWatching = {} ; taskStatus = {}", member.getId(), executorEngine.isWatching(), member.getTaskStatus());
                    member.setExitValue(1);
                    member.setFinalized(1);
                    member.setTaskStatus(ServerTaskStatus.FINALIZED.getStatus());
                    ocServerTaskMemberService.updateOcServerTaskMember(member);
                    return;
                }
            }

            while (true) {
                resultHandler.waitFor(500);
                // 执行日志写入redis
                taskLogRecorder.recorderLog(member.getId(), executorEngine);
                // 任务结束
                if (resultHandler.hasResult()) {
                    TaskStatusBO taskStatus = TaskStatusBO.builder()
                            .exitValue(resultHandler.getExitValue())
                            .tastResult(AnsibleResult.getName(resultHandler.getExitValue()))
                            .stopType(resultHandler.getExitValue() == 0 ? ServerTaskStopType.COMPLETE_STOP.getType() : -1)
                            .build();
                    saveServerTaskMember(member, taskStatus);
                    return;
                } else {
                    // 判断任务是否需要终止或超时
                    if (TimeUtils.checkTimeout(startTaskTime, timeout)) {
                        executorEngine.killedProcess();
                        taskLogRecorder.recorderLog(member.getId(), executorEngine);
                        throw new TaskTimeoutException();
                    }
                    // 判断任务是否终止( 缓存标志位获取 )
                    if (taskLogRecorder.getAbortTaskMember(member.getId()) != 0) {
                        executorEngine.killedProcess();
                        taskLogRecorder.recorderLog(member.getId(), executorEngine);
                        throw new TaskStopException();
                    }
                    // 日志长度超过阈值
                    if (member.getOutputMsg().length() >= MAX_LOG_LENGTH) {
                        executorEngine.killedProcess();
                        taskLogRecorder.recorderLog(member.getId(), executorEngine);
                        throw new TaskLogExceededLimit();
                    }
                }
            }
        } catch (TaskTimeoutException e) {
            TaskStatusBO taskStatus = TaskStatusBO.builder()
                    .stopType(ServerTaskStopType.TIMEOUT_STOP.getType())
                    .tastResult("TIMEOUT")
                    .build();
            saveServerTaskMember(member, taskStatus);
        } catch (TaskStopException e) {
            TaskStatusBO taskStatus = TaskStatusBO.builder()
                    .stopType(taskLogRecorder.getAbortTaskMember(member.getId()))
                    .tastResult("ABORT")
                    .build();
            saveServerTaskMember(member, taskStatus);
        } catch (ExecuteException e) {
            TaskStatusBO taskStatus = TaskStatusBO.builder()
                    .stopType(ServerTaskStopType.TIMEOUT_STOP.getType())
                    .exitValue(e.getExitValue())
                    .tastResult(AnsibleResult.ERROR.getName())
                    .build();
            saveServerTaskMember(member, taskStatus);
        } catch (TaskLogExceededLimit e) {
            TaskStatusBO taskStatus = TaskStatusBO.builder()
                    .stopType(ServerTaskStopType.LOG_EXCEEDED_LIMIT.getType())
                    .tastResult("LOG_EXCEEDED_LIMIT")
                    .build();
            saveServerTaskMember(member, taskStatus);
        }  // 日志流转码错误 暂不处理
        catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    private void saveServerTaskMember(OcServerTaskMember member, TaskStatusBO taskStatus) {
        member.setFinalized(taskStatus.getFinalized());
        member.setExitValue(taskStatus.getExitValue());
        member.setStopType(taskStatus.getStopType());
        member.setTaskStatus(taskStatus.getTaskStatus());
        // 写入并清空日志
        MemberExecutorLogBO memberExecutorLogBO = taskLogRecorder.getLog(member.getId());
        if (memberExecutorLogBO != null) {
            try {
                if (!StringUtils.isEmpty(memberExecutorLogBO.getOutputMsg())) {
                    String outputLogPath = taskLogRecorder.getOutputLogPath(member);   //Joiner.on("/").join(playbookLogPath,member.getId() + "_output.log" );
                    IOUtils.writeFile(memberExecutorLogBO.getOutputMsg(), outputLogPath);
                    member.setOutputMsg(outputLogPath);
                }
            } catch (Exception e) {
                log.error("记录执行日志OutputMsg错误, memberId = {}", member.getId());
            }
            try {
                if (!StringUtils.isEmpty(memberExecutorLogBO.getErrorMsg())) {
                    String errorLogPath = taskLogRecorder.getErrorLogPath(member); //Joiner.on("/").join(playbookLogPath,member.getId() + "_error.log" );
                    IOUtils.writeFile(memberExecutorLogBO.getErrorMsg(), errorLogPath);
                    member.setErrorMsg(errorLogPath);
                }
            } catch (Exception e) {
                log.error("记录执行日志ErrorMsg错误, memberId = {}", member.getId());
            }
            taskLogRecorder.clearLog(member.getId());
        }

        if (!StringUtils.isEmpty(taskStatus.getTastResult()))
            member.setTaskResult(taskStatus.getTastResult());
        saveServerTaskMember(member);
    }

    private void saveServerTaskMember(OcServerTaskMember member) {
        ocServerTaskMemberService.updateOcServerTaskMember(member);
    }

}
