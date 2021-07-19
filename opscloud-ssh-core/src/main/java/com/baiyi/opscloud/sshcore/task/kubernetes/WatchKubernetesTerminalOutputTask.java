package com.baiyi.opscloud.sshcore.task.kubernetes;

import com.baiyi.opscloud.sshcore.model.SessionOutput;
import com.baiyi.opscloud.sshcore.task.base.IOutputTask;
import com.baiyi.opscloud.sshcore.util.SessionOutputUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.input.ClosedInputStream;
import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @Author baiyi
 * @Date 2021/7/16 10:42 上午
 * @Version 1.0
 */
@Slf4j
public class WatchKubernetesTerminalOutputTask implements IOutputTask {

    private ByteArrayOutputStream outputStream;
    private SessionOutput sessionOutput;

    private boolean isClosed = false;

    private static final int BUFF_SIZE = 1024; // 1KB

    public WatchKubernetesTerminalOutputTask(SessionOutput sessionOutput, ByteArrayOutputStream outputStream) {
        this.sessionOutput = sessionOutput;
        this.outputStream = outputStream;
    }

    public void close() {
        this.isClosed = true;
    }

    @Override
    public void run() {
        SessionOutputUtil.addOutput(this.sessionOutput);
        try {
            while (!isClosed) {
                Thread.sleep(150);
                InputStream ins = outputStream.toInputStream();
                if (ins instanceof ClosedInputStream)
                    continue;
                outputStream.reset();
                InputStreamReader isr = new InputStreamReader(ins);
                BufferedReader br = new BufferedReader(isr, BUFF_SIZE);
                char[] buff = new char[BUFF_SIZE];
                int read;
                while ((read = br.read(buff)) != -1) {
                    write(buff, 0, read);
                    Thread.sleep(25);
                }
            }
        } catch (Exception ex) {
            log.error(ex.toString(), ex);
        } finally {
            log.info("outputTask线程结束! sessionId = {} , instanceId = {}", sessionOutput.getSessionId(), sessionOutput.getInstanceId());
            SessionOutputUtil.removeOutput(sessionOutput.getSessionId(), sessionOutput.getInstanceId());
        }
    }

    @Override
    public void write(char[] buf, int off, int len) {
        SessionOutputUtil.addToOutput(sessionOutput.getSessionId(), sessionOutput.getInstanceId(), buf, off, len);
    }


}
