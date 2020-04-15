package com.baiyi.opscloud.ansible.handler;

import com.baiyi.opscloud.ansible.bo.TaskResult;
import com.baiyi.opscloud.ansible.executor.ExecutorEngine;
import com.baiyi.opscloud.common.base.AnsibleResult;
import com.baiyi.opscloud.common.base.ServerTaskStatus;
import com.baiyi.opscloud.common.base.ServerTaskStopType;
import com.baiyi.opscloud.common.util.TimeUtils;
import com.baiyi.opscloud.domain.generator.OcServerTask;
import com.baiyi.opscloud.domain.generator.OcServerTaskMember;
import com.baiyi.opscloud.service.server.OcServerTaskMemberService;
import com.baiyi.opscloud.service.server.OcServerTaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
    private OcServerTaskService ocServerTaskService;

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


    public static ExecutorEngine buildExecutorEngine(long timeout) {
        final ExecuteWatchdog watchdog = new ExecuteWatchdog(timeout);
        // 缓冲区1024字节
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);
        ByteArrayOutputStream errorStream = new ByteArrayOutputStream(1024);
        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream, errorStream);
        DefaultExecutor executor = new DefaultExecutor();
        executor.setStreamHandler(streamHandler);
        executor.setWatchdog(watchdog);

        ExecutorEngine executorEngine = ExecutorEngine.builder()
                .executor(executor)
                .outputStream(outputStream)
                .errorStream(errorStream)
                .build();
        return executorEngine;
    }

    private void setMemberExitValue(OcServerTaskMember member,DefaultExecuteResultHandler resultHandler){
        member.setExitValue(resultHandler.getExitValue());
        member.setTaskResult(AnsibleResult.getName(resultHandler.getExitValue()));
    }


    /**
     * https://blog.csdn.net/doublesin/article/details/79082113
     *
     * @param timeout
     * @return
     */
    @Async(value = ASYNC_POOL_TASK_EXECUTOR)
    public void executorRecorder(OcServerTaskMember member, CommandLine commandLine, Long timeout) {
        //System.err.println(JSON.toJSONString(commandLine));

        if (timeout == 0)
            timeout = MAX_TIMEOUT;
        try {
            final DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
            ExecutorEngine executorEngine = buildExecutorEngine(timeout);
            executorEngine.execute(commandLine, resultHandler);
            resultHandler.waitFor(1000);
            // 启动时间
            Long startTaskTime = new Date().getTime();

            while (true) {
                //  resultHandler.waitFor(2000);
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
                // 日志容量上限判断
                if (member.getOutputMsg().length() >= MAX_LOG_LENGTH) {

                }

                resultHandler.waitFor(2000);
                member.setOutputMsg(executorEngine.getOutputMsg());
                member.setErrorMsg(executorEngine.getErrorMsg());
                ocServerTaskMemberService.updateOcServerTaskMember(member);

                // 任务结束
                if (resultHandler.hasResult()) {
                    member.setExitValue(resultHandler.getExitValue());
                    member.setOutputMsg(executorEngine.getOutputMsg());
                    member.setFinalized(1);
                    member.setTaskStatus(ServerTaskStatus.FINALIZED.getStatus());
                    switch (resultHandler.getExitValue()) {
                        case 0:
                            member.setErrorMsg(executorEngine.getErrorMsg());
                            member.setStopType(ServerTaskStopType.COMPLETE_STOP.getType());
                            setMemberExitValue(member,resultHandler);
                            ocServerTaskMemberService.updateOcServerTaskMember(member);
                            return; // 退出
                        // 异常结束
                        default:
                            member.setOutputMsg(executorEngine.getOutputMsg());
                            member.setErrorMsg(executorEngine.getErrorMsg() + resultHandler.getException().getMessage());
                            setMemberExitValue(member,resultHandler);
                            ocServerTaskMemberService.updateOcServerTaskMember(member);
                            return;
                    }
                } else {
                    // 判断任务是否需要终止或超时
                    if (TimeUtils.checkTimeout(startTaskTime, timeout)) {
                        // 停止任务
                        executorEngine.killedProcess();
                        updateServerTaskMemberStopType(member, ServerTaskStopType.TIMEOUT_STOP.getType(), 1);
                        return;
                    }
                    // 判断主任务是否终止
                    OcServerTask ocServerTask = ocServerTaskService.queryOcServerTaskById(member.getTaskId());
                    if (ocServerTask.getStopType() == ServerTaskStopType.SERVER_TASK_STOP.getType()) {
                        executorEngine.killedProcess();
                        updateServerTaskMemberStopType(member, ServerTaskStopType.SERVER_TASK_STOP.getType(), 1);
                        return;
                    }
                    // 判断子任务是否终止
                    OcServerTaskMember checkMember = ocServerTaskMemberService.queryOcServerTaskMemberById(member.getId());
                    if (checkMember.getStopType() == ServerTaskStopType.MEMBER_TASK_STOP.getType()) {
                        executorEngine.killedProcess();
                        updateServerTaskMemberStopType(member, ServerTaskStopType.MEMBER_TASK_STOP.getType(), 1);
                        return;
                    }
                }
                member = ocServerTaskMemberService.queryOcServerTaskMemberById(member.getId());
            }

        } catch (ExecuteException e) {
            e.printStackTrace();
            // 结束任务
            member.setExitValue(e.getExitValue());
            member.setTaskResult(AnsibleResult.ERROR.getName());
            member.setFinalized(1);
            member.setTaskStatus(ServerTaskStatus.FINALIZED.getStatus());
            member.setErrorMsg(e.getMessage());
            ocServerTaskMemberService.updateOcServerTaskMember(member);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // 日志流转码错误
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateServerTaskMemberStopType(OcServerTaskMember member, int stopType, int exitValue) {
        member.setStopType(stopType);
        member.setExitValue(exitValue);
        member.setFinalized(1);
        member.setTaskStatus(ServerTaskStatus.FINALIZED.getStatus());
        ocServerTaskMemberService.updateOcServerTaskMember(member);
    }
}
