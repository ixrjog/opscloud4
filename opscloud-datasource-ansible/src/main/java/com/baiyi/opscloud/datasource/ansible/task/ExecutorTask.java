package com.baiyi.opscloud.datasource.ansible.task;

import com.baiyi.opscloud.datasource.ansible.executor.ExecutorEngine;
import com.baiyi.opscloud.datasource.ansible.executor.ExecutorEngineBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;

import java.io.IOException;
import java.util.Date;

/**
 * @Author baiyi
 * @Date 2021/6/22 6:47 下午
 * @Version 1.0
 */
@Slf4j
public class ExecutorTask implements Runnable {

    Boolean stop;
    CommandLine commandLine;
    Long timeout;

    // 100 分钟
    public static final Long MAX_TIMEOUT = 6000000L;

    public void stop() {
        this.stop = true;
    }

    @Override
    public void run() {
        while (true) {
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
//                if (member.getTaskStatus().equals(ServerTaskStatus.QUEUE.getStatus())) {
//                    resultHandler.waitFor(500);
//                    if (executorEngine.isWatching()) {
//                        log.info("任务启动成功! id = {} ; isWatching = {} ; taskStatus = {}", member.getId(), executorEngine.isWatching(), member.getTaskStatus());
//                        member.setTaskStatus(ServerTaskStatus.EXECUTING.getStatus());
//                        ocServerTaskMemberService.updateOcServerTaskMember(member);
//                    } else {
//                        log.info("任务启动失败! id = {} ; isWatching = {} ; taskStatus = {}", member.getId(), executorEngine.isWatching(), member.getTaskStatus());
//                        member.setExitValue(1);
//                        member.setFinalized(1);
//                        member.setTaskStatus(ServerTaskStatus.FINALIZED.getStatus());
//                        ocServerTaskMemberService.updateOcServerTaskMember(member);
//                        return;
//                    }
//                }

            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}
