package com.baiyi.opscloud.sshcore.task.audit;

import com.baiyi.opscloud.sshcore.AuditRecordHelper;
import com.baiyi.opscloud.sshcore.model.SessionOutput;
import com.baiyi.opscloud.sshcore.task.audit.output.OutputMessage;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.Session;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.concurrent.TimeUnit;

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
        String str;
        try {
            LineNumberReader reader = new LineNumberReader(new FileReader(auditLogPath));
            while (session.isOpen() && (str = reader.readLine()) != null) {
                if (!str.isEmpty()) {
                    send(str +"\n");
                }
                TimeUnit.MILLISECONDS.sleep(25L);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        log.info("outputTask线程结束! sessionId = {} , instanceId = {}", sessionOutput.getSessionId(), sessionOutput.getInstanceId());
    }

    private void send(String auditLog) throws IOException {
        OutputMessage om = OutputMessage.builder()
                .instanceId(sessionOutput.getInstanceId())
                .output(auditLog)
                .build();
        session.getBasicRemote().sendText(om.toString());
    }

}
