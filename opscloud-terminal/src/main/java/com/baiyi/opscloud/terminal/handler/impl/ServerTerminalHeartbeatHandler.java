package com.baiyi.opscloud.terminal.handler.impl;

import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.sshcore.enums.MessageState;
import com.baiyi.opscloud.sshcore.message.ServerMessage;
import com.baiyi.opscloud.terminal.handler.AbstractServerTerminalHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.websocket.Session;

/**
 * 心跳
 * @Author baiyi
 * @Date 2020/5/13 6:50 下午
 * @Version 1.0 HEARTBEAT
 */
@Slf4j
@Component
public class ServerTerminalHeartbeatHandler extends AbstractServerTerminalHandler<ServerMessage.BaseMessage> {

    @Override
    public String getState() {
        return MessageState.HEARTBEAT.getState();
    }

    @Override
    public void handle(String message, Session session, TerminalSession terminalSession) {
        heartbeat(terminalSession.getSessionId());
    }

    @Override
    protected ServerMessage.BaseMessage toMessage(String message) {
        return null;
    }

}