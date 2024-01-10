package com.baiyi.opscloud.terminal.handler.impl;

import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.sshcore.enums.MessageState;
import com.baiyi.opscloud.sshcore.handler.RemoteInvokeHandler;
import com.baiyi.opscloud.sshcore.message.ServerMessage;
import com.baiyi.opscloud.sshcore.model.JSchSession;
import com.baiyi.opscloud.sshcore.model.JSchSessionContainer;
import com.baiyi.opscloud.terminal.handler.AbstractServerTerminalHandler;
import com.google.gson.GsonBuilder;
import com.jcraft.jsch.ChannelShell;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.websocket.Session;

/**
 * XTerm改变形体
 *
 * @Author baiyi
 * @Date 2020/5/12 10:43 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class ServerTerminalResizeHandler extends AbstractServerTerminalHandler<ServerMessage.Resize> {

    @Override
    public String getState() {
        return MessageState.RESIZE.getState();
    }

    @Override
    public void handle(String message, Session session, TerminalSession terminalSession) {
        ServerMessage.Resize resizeMessage = toMessage(message);
        try {
            JSchSession jSchSession = JSchSessionContainer.getBySessionId(terminalSession.getSessionId(), resizeMessage.getInstanceId());
            assert jSchSession != null;
            RemoteInvokeHandler.setChannelPtySize((ChannelShell) jSchSession.getChannel(), resizeMessage);
        } catch (Exception e) {
            log.warn("Web terminal resize error: {}", e.getMessage());
        }
    }

    @Override
    protected ServerMessage.Resize toMessage(String message) {
        return new GsonBuilder().create().fromJson(message, ServerMessage.Resize.class);
    }

}