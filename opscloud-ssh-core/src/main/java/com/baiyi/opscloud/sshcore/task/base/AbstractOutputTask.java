package com.baiyi.opscloud.sshcore.task.base;

import com.baiyi.opscloud.sshcore.AuditRecordHelper;
import com.baiyi.opscloud.sshcore.model.SessionOutput;
import com.baiyi.opscloud.sshcore.util.SessionOutputUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

/**
 * @Author baiyi
 * @Date 2021/7/7 12:51 下午
 * @Version 1.0
 */
@Slf4j
@Data
public abstract class AbstractOutputTask implements IOutputTask {

    private InputStream outFromChannel;
    private SessionOutput sessionOutput;

    private static final int BUFF_SIZE = 1024 * 8; // 8KB

    public AbstractOutputTask(SessionOutput sessionOutput, InputStream outFromChannel) {
        setSessionOutput(sessionOutput);
        setOutFromChannel(outFromChannel);
    }

    @Override
    public void run() {
        InputStreamReader isr = new InputStreamReader(outFromChannel);
        BufferedReader br = new BufferedReader(isr, BUFF_SIZE);
        try {
            SessionOutputUtil.addOutput(sessionOutput);
            char[] buff = new char[BUFF_SIZE];
            int read;
            while ((read = br.read(buff)) != -1) {
                write(buff, 0, read);
                auditing(buff, 0, read);
                TimeUnit.MILLISECONDS.sleep(10L);
            }
        } catch (Exception ex) {
            log.error(ex.toString(), ex);
        } finally {
            log.info("outputTask线程结束! sessionId = {} , instanceId = {}", sessionOutput.getSessionId(), sessionOutput.getInstanceId());
            SessionOutputUtil.removeOutput(sessionOutput.getSessionId(), sessionOutput.getInstanceId());
        }
    }

    protected byte[] toBytes(char[] chars) {
        return TaskUtil.toBytes(chars);
    }


    /**
     * 审计日志
     *
     * @param buf
     */
    private void auditing(char[] buf, int off, int len) {
        AuditRecordHelper.recordAuditLog(sessionOutput.getSessionId(), sessionOutput.getInstanceId(), buf, off, len);
    }

}

