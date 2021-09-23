package com.baiyi.opscloud.ansible.executor;

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

    private DefaultExecutor executor;

    private ExecuteWatchdog watchdog;

    private ByteArrayOutputStream outputStream;

    private ByteArrayOutputStream errorStream;

    //  private ExecuteWatchdog watchdog;

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
            String outStr = this.outputStream.toString("utf8");
            this.outputStream.reset();
            return outStr;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
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
            String errStr = this.errorStream.toString("utf8");
            this.errorStream.reset();
            return errStr;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }
}
