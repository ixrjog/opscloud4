package com.baiyi.opscloud.datasource.ansible.executor;

import com.baiyi.opscloud.common.util.NewTimeUtil;
import com.baiyi.opscloud.datasource.ansible.entity.AnsibleExecuteResult;
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
public class AnsibleExecutor {

    /**
     * 100 分钟
     */
    public static final long MAX_TIMEOUT = NewTimeUtil.MINUTE_TIME * 100;

    /**
     * 阻塞方式运行
     *
     * @param commandLine
     * @param timeout
     * @return
     */
    public static AnsibleExecuteResult execute(CommandLine commandLine, long timeout) {
        if (timeout == 0) {
            timeout = MAX_TIMEOUT;
        }
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
            log.error(e.getMessage());
            AnsibleExecuteResult result = AnsibleExecuteResult.FAILED;
            result.setException(e);
            return result;
        }
    }

}
