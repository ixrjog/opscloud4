package com.baiyi.opscloud.sshcore.task.audit;

import com.baiyi.opscloud.common.util.NewTimeUtil;
import com.baiyi.opscloud.sshcore.AuditRecordHelper;
import com.baiyi.opscloud.sshcore.model.SessionOutput;
import com.baiyi.opscloud.sshcore.task.audit.output.OutputMessage;
import lombok.extern.slf4j.Slf4j;

import jakarta.websocket.Session;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

/**
 * @Author baiyi
 * @Date 2021/7/23 3:29 下午
 * @Version 1.0
 */
@Slf4j
public class TerminalAuditOutputTask implements Runnable {

    private final SessionOutput sessionOutput;
    private final Session session;

    public TerminalAuditOutputTask(Session session, SessionOutput sessionOutput) {
        this.session = session;
        this.sessionOutput = sessionOutput;
    }

    @Override
    public void run() {
        String auditLogPath = AuditRecordHelper.getAuditLogPath(sessionOutput.getSessionId(), sessionOutput.getInstanceId());
        try {
            LineNumberReader reader = new LineNumberReader(new FileReader(auditLogPath));
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                    send(line + "\n");
                }
                NewTimeUtil.millisecondsSleep(25L);
            }
        } catch (IOException ignored) {
        }
    }

    private void send(String auditLog) throws IOException {
        OutputMessage om = OutputMessage.builder()
                .instanceId(sessionOutput.getInstanceId())
                .output(auditLog)
                .build();
        session.getBasicRemote().sendText(om.toString());
    }

}