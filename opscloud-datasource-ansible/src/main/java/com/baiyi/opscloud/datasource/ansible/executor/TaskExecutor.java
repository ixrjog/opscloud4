package com.baiyi.opscloud.datasource.ansible.executor;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @Author baiyi
 * @Date 2021/9/22 3:23 下午
 * @Version 1.0
 */
@Slf4j
@Data
@Builder
public class TaskExecutor {

    private final static String CHARSET_NAME = "utf8";

    private DefaultExecutor executor;

    private ExecuteWatchdog watchdog;

    private final ByteArrayOutputStream outputStream;

    private final ByteArrayOutputStream errorStream;

    public void execute(CommandLine commandLine, DefaultExecuteResultHandler resultHandler) throws IOException {
        this.executor.execute(commandLine, resultHandler);
    }

    public boolean isWatching() {
        return watchdog.isWatching();
    }

    public boolean killedProcess() {
        return watchdog.killedProcess();
    }

    /**
     * 读取并清空缓存区
     *
     * @return
     */
    public String getOutputMsg() {
        try {
            String outStr = this.outputStream.toString(CHARSET_NAME);
            this.outputStream.reset();
            return outStr;
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
            return "";
        }
    }

    /**
     * 读取并清空缓存区
     *
     * @return
     */
    public String getErrorMsg() {
        try {
            String errStr = this.errorStream.toString(CHARSET_NAME);
            this.errorStream.reset();
            return errStr;
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
            return "";
        }
    }

}
