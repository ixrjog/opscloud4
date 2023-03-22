package com.baiyi.opscloud.datasource.ansible.executor;

import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.ByteArrayOutputStream;

/**
 * @Author baiyi
 * @Date 2021/9/22 3:24 下午
 * @Version 1.0
 */
public class TaskExecutorBuilder {

    public static TaskExecutor build(long timeout) {
        ExecuteWatchdog watchdog = new ExecuteWatchdog(timeout);
        // 缓冲区1024字节
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);
        ByteArrayOutputStream errorStream = new ByteArrayOutputStream(1024);
        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream, errorStream);
        DefaultExecutor executor = new DefaultExecutor();
        executor.setStreamHandler(streamHandler);
        executor.setWatchdog(watchdog);
        return TaskExecutor.builder()
                .executor(executor)
                .outputStream(outputStream)
                .errorStream(errorStream)
                .watchdog(watchdog)
                .build();
    }

}
