package com.baiyi.opscloud.sshcore.task.base;

import com.baiyi.opscloud.common.util.NewTimeUtil;
import com.baiyi.opscloud.sshcore.AuditRecordHelper;
import com.baiyi.opscloud.sshcore.model.SessionOutput;
import com.baiyi.opscloud.sshcore.util.SessionOutputUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @Author baiyi
 * @Date 2021/7/7 12:51 下午
 * @Version 1.0
 */
@Slf4j
@Data
public abstract class AbstractOutputTask implements IRecordOutputTask {

    private InputStream outFromChannel;

    private SessionOutput sessionOutput;

    private static final int BUFF_SIZE = 1024 * 8;

    public AbstractOutputTask(SessionOutput sessionOutput, InputStream outFromChannel) {
        setSessionOutput(sessionOutput);
        setOutFromChannel(outFromChannel);
    }

    @Override
    public void run() {
        InputStreamReader isr = new InputStreamReader(outFromChannel, StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr, BUFF_SIZE);
        try {
            SessionOutputUtil.addOutput(sessionOutput);
            char[] buff = new char[BUFF_SIZE];
            int read;
            while ((read = br.read(buff)) != -1) {
                char[] outBuff = com.baiyi.opscloud.common.util.ArrayUtil.sub(buff, 0, read);
                writeAndRecord(outBuff, 0, outBuff.length);
                NewTimeUtil.millisecondsSleep(10L);
            }
        } catch (IOException ignored) {
        } finally {
            log.debug("Watch server output task end: sessionId={}, instanceId={}", sessionOutput.getSessionId(), sessionOutput.getInstanceId());
            SessionOutputUtil.removeOutput(sessionOutput.getSessionId(), sessionOutput.getInstanceId());
        }
    }

    protected byte[] toBytes(char[] chars) {
        return String.valueOf(chars).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public void record(char[] buf, int off, int len) {
        AuditRecordHelper.record(sessionOutput.getSessionId(), sessionOutput.getInstanceId(), buf, off, len);
    }

}