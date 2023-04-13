package com.baiyi.opscloud.sshcore.task.base;

import com.baiyi.opscloud.common.util.NewTimeUtil;
import com.baiyi.opscloud.sshcore.AuditRecordHelper;
import com.baiyi.opscloud.sshcore.model.SessionOutput;
import com.baiyi.opscloud.sshcore.util.SessionOutputUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.input.ClosedInputStream;
import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @Author baiyi
 * @Date 2021/7/22 10:24 上午
 * @Version 1.0
 */
@Slf4j
@Data
public abstract class AbstractSshChannelOutputTask implements IRecordOutputTask {

    private ByteArrayOutputStream outputStream;
    private SessionOutput sessionOutput;

    private boolean isClosed = false;

    private static final int BUFF_SIZE = 1024;

    public void close() {
        this.isClosed = true;
    }

    @Override
    public void run() {
        SessionOutputUtil.addOutput(this.sessionOutput);
        try {
            while (!isClosed) {
                NewTimeUtil.millisecondsSleep(25L);
                InputStream ins = outputStream.toInputStream();
                if (ins instanceof ClosedInputStream) {
                    continue;
                }
                outputStream.reset();
                InputStreamReader isr = new InputStreamReader(ins, StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(isr, BUFF_SIZE);
                char[] buff = new char[BUFF_SIZE];
                int read;
                while ((read = br.read(buff)) != -1) {
                    writeAndRecord(buff, 0, read);
                }
            }
        } catch (IOException ignored) {
        } finally {
            log.debug("Ssh channel output task end: sessionId={}, instanceId={}", sessionOutput.getSessionId(), sessionOutput.getInstanceId());
            SessionOutputUtil.removeOutput(sessionOutput.getSessionId(), sessionOutput.getInstanceId());
        }
    }

    /**
     * 写审计
     *
     * @param buf
     * @param off
     * @param len
     */
    @Override
    public void record(char[] buf, int off, int len) {
        AuditRecordHelper.record(sessionOutput.getSessionId(), sessionOutput.getInstanceId(), buf, off, len);
    }

    protected byte[] toBytes(char[] chars) {
        return String.valueOf(chars).getBytes(StandardCharsets.UTF_8);
    }

}