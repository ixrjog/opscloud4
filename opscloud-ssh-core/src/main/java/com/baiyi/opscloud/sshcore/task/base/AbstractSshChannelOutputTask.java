package com.baiyi.opscloud.sshcore.task.base;

import com.baiyi.opscloud.sshcore.AuditRecordHelper;
import com.baiyi.opscloud.sshcore.model.SessionOutput;
import com.baiyi.opscloud.sshcore.util.SessionOutputUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.input.ClosedInputStream;
import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

/**
 * @Author baiyi
 * @Date 2021/7/22 10:24 上午
 * @Version 1.0
 */
@Slf4j
@Data
public abstract class AbstractSshChannelOutputTask implements IOutputTask {

    private ByteArrayOutputStream baos;
    private SessionOutput sessionOutput;

    private boolean isClosed = false;

    private static final int BUFF_SIZE = 1024; // 1KB

    public void close() {
        this.isClosed = true;
    }

    @Override
    public void run() {
        SessionOutputUtil.addOutput(this.sessionOutput);
        try {
            while (!isClosed) {
                TimeUnit.MILLISECONDS.sleep(25L);
                InputStream ins = baos.toInputStream();
                if (ins instanceof ClosedInputStream)
                    continue;
                baos.reset();
                InputStreamReader isr = new InputStreamReader(ins);
                BufferedReader br = new BufferedReader(isr, BUFF_SIZE);
                char[] buff = new char[BUFF_SIZE];
                int read;
                while ((read = br.read(buff)) != -1) {
                    write(buff, 0, read);
                    auditing(buff, 0, read);
                }
            }
        } catch (Exception ex) {
            log.error(ex.toString(), ex);
        } finally {
            log.info("outputTask线程结束! sessionId = {} , instanceId = {}", sessionOutput.getSessionId(), sessionOutput.getInstanceId());
            SessionOutputUtil.removeOutput(sessionOutput.getSessionId(), sessionOutput.getInstanceId());
        }
    }

    /**
     * 审计日志
     *
     * @param buf
     */
    private void auditing(char[] buf, int off, int len) {
        AuditRecordHelper.recordAuditLog(sessionOutput.getSessionId(), sessionOutput.getInstanceId(), buf, off, len);
    }

    protected byte[] toBytes(char[] chars) {
        return TaskUtil.toBytes(chars);
    }

}
