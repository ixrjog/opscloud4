package com.baiyi.opscloud.sshcore.task.ssh;

import com.baiyi.opscloud.sshcore.model.SessionOutput;
import com.baiyi.opscloud.sshcore.util.SessionOutputUtil;
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
@Deprecated
@Slf4j
public class SshSentOutputTask implements Runnable {

    ServerSession serverSession;
    String sessionId;
    Terminal terminal;
    Boolean stop;

    public SshSentOutputTask(String sessionId, ServerSession serverSession, Terminal terminal) {
        this.sessionId = sessionId;
        this.serverSession = serverSession;
        this.terminal = terminal;
        this.stop = false;
    }

    public void stop() {
        this.stop = true;
    }

    @Override
    public void run() {
        while (serverSession.isOpen() && !stop) {
            List<SessionOutput> outputList = SessionOutputUtil.getOutput(sessionId);
            try {
                if (CollectionUtils.isNotEmpty(outputList)) {
                    for (SessionOutput sessionOutput : outputList) {
                        terminal.writer().append(sessionOutput.getOutput());
                        terminal.writer().flush();
                    }
                }
                Thread.sleep(25L);
            } catch (Exception ex) {
                log.error(ex.toString(), ex);
            }
        }
    }
}
