package com.baiyi.opscloud.ansible.executor;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.exec.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @Author baiyi
 * @Date 2020/4/13 9:29 上午
 * @Version 1.0
 */
@Data
@Builder
public class ExecutorEngine {

    @Builder.Default
    private DefaultExecutor executor = new DefaultExecutor();
    // 缓冲区1024字节
    @Builder.Default
    private ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);
    @Builder.Default
    private ByteArrayOutputStream errorStream = new ByteArrayOutputStream(1024);
    private final ExecuteWatchdog watchdog;

    public void execute(CommandLine commandLine, DefaultExecuteResultHandler resultHandler) throws IOException {
        this.executor.execute(commandLine,resultHandler);
    }

    public boolean isWatching(){
        return this.watchdog.isWatching();
    }

    public boolean killedProcess() {
        return this.watchdog.killedProcess();
    }

    public String getOutputMsg() {
        try {
            return this.outputStream.toString("utf8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public String getErrorMsg() {
        try {
            return this.errorStream.toString("utf8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

}
