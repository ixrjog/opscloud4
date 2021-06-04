package com.baiyi.caesar.sshcore.task.server;

import com.baiyi.caesar.sshcore.model.SessionOutput;
import com.baiyi.caesar.sshcore.util.SessionOutputUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.sshd.server.session.ServerSession;
import org.jline.terminal.Terminal;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/3 12:43 下午
 * @Version 1.0
 */
@Slf4j
public class ServerSentOutputTask implements Runnable {

    ServerSession serverSession;
    String sessionId;
    Terminal terminal;

    public ServerSentOutputTask(String sessionId, ServerSession serverSession, Terminal terminal) {
        this.sessionId = sessionId;
        this.serverSession = serverSession;
        this.terminal = terminal;
    }

    @Override
    public void run() {
        while (serverSession.isOpen()) {
            List<SessionOutput> outputList = SessionOutputUtil.getOutput(sessionId);
            try {
                if (CollectionUtils.isNotEmpty(outputList)) {
                    try {
                        if (CollectionUtils.isNotEmpty(outputList)) {
                            outputList.forEach(e ->
                                        terminal.writer().append(e.getOutput())
                            );
                            terminal.flush();
                        }
                    } catch (Exception ex) {
                        log.error(ex.toString(), ex);
                    }
                }
                Thread.sleep(25L);
            } catch (Exception ex) {
                log.error(ex.toString(), ex);
            }
        }
    }
}
