package com.baiyi.opscloud.terminal.audit.impl;

import com.baiyi.opscloud.sshcore.enums.MessageState;
import com.baiyi.opscloud.sshcore.message.audit.TerminalAuditPlayMessage;
import com.baiyi.opscloud.sshcore.model.SessionOutput;
import com.baiyi.opscloud.sshcore.task.audit.TerminalAuditOutputTask;
import com.google.gson.GsonBuilder;
import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/7/23 3:05 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class TerminalAuditPlayHandler extends AbstractTerminalAuditHandler<TerminalAuditPlayMessage> {

    /**
     * 播放
     *
     * @return
     */
    @Override
    public String getState() {
        return MessageState.PLAY.getState();
    }

    @Override
    public void handle(String message, Session session) {
        TerminalAuditPlayMessage playMessage = getMessage(message);
        SessionOutput sessionOutput = new SessionOutput(playMessage.getSessionId(), playMessage.getInstanceId());
        // JDK21 VirtualThreads
        Thread.ofVirtual().start(new TerminalAuditOutputTask(session, sessionOutput));
    }

    @Override
    protected TerminalAuditPlayMessage getMessage(String message) {
        return new GsonBuilder().create().fromJson(message, TerminalAuditPlayMessage.class);
    }

}