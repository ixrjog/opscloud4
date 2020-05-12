package com.baiyi.opscloud.ansible.builder;

import com.baiyi.opscloud.ansible.executor.ExecutorEngine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.ByteArrayOutputStream;

/**
 * @Author baiyi
 * @Date 2020/4/19 3:13 下午
 * @Version 1.0
 */
public class ExecutorEngineBuilder {

    public static ExecutorEngine build(long timeout) {
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
}
