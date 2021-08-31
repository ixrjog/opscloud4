package com.baiyi.opscloud.ansible.handler;

import com.baiyi.opscloud.ansible.model.AnsibleExecuteResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.ByteArrayOutputStream;

/**
 * @Author baiyi
 * @Date 2021/8/16 10:53 上午
 * @Version 1.0
 */
@Slf4j
public class AnsibleHandler {

    // 100 分钟
    public static final Long MAX_TIMEOUT = 6000000L;

    /**
     * 阻塞方式运行
     *
     * @param commandLine
     * @param timeout
     * @return
     */
    public static AnsibleExecuteResult execute(CommandLine commandLine, Long timeout) {
        if (timeout == 0)
            timeout = MAX_TIMEOUT;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
            DefaultExecutor exec = new DefaultExecutor();
            //创建监控时间X秒，超过X秒则中断执行
            ExecuteWatchdog watchdog = new ExecuteWatchdog(timeout);
            exec.setWatchdog(watchdog);
            exec.setExitValues(null);
            PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream, errorStream);
            exec.setStreamHandler(streamHandler);
            int exitValue = exec.execute(commandLine);
            return AnsibleExecuteResult.builder()
                    .exitValue(exitValue)
                    .output(outputStream)
                    .error(errorStream)
                    .build();
        } catch (Exception e) {
            AnsibleExecuteResult result = AnsibleExecuteResult.FAILED;
            result.setException(e);
            return result;
        }
    }
}
