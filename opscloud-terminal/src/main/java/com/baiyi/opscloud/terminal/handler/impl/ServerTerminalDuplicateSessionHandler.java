package com.baiyi.opscloud.terminal.handler.impl;

import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.sshcore.builder.TerminalSessionInstanceBuilder;
import com.baiyi.opscloud.sshcore.enums.InstanceSessionTypeEnum;
import com.baiyi.opscloud.sshcore.enums.MessageState;
import com.baiyi.opscloud.sshcore.handler.RemoteInvokeHandler;
import com.baiyi.opscloud.sshcore.message.ServerMessage;
import com.baiyi.opscloud.sshcore.model.HostSystem;
import com.baiyi.opscloud.sshcore.model.JSchSession;
import com.baiyi.opscloud.sshcore.model.JSchSessionContainer;
import com.baiyi.opscloud.terminal.handler.AbstractServerTerminalHandler;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;

import jakarta.websocket.Session;

/**
 * 复制会话
 *
 * @Author baiyi
 * @Date 2020/5/13 10:24 上午
 * @Version 1.0
 */
@Component
public class ServerTerminalDuplicateSessionHandler extends AbstractServerTerminalHandler<ServerMessage.DuplicateSession> {

    @Override
    public String getState() {
        return MessageState.DUPLICATE_SESSION.getState();
    }

    @Override
    public void handle(String message, Session session, TerminalSession terminalSession) {
        ServerMessage.DuplicateSession baseMessage = toMessage(message);
        JSchSession jSchSession = JSchSessionContainer.getBySessionId(terminalSession.getSessionId(), baseMessage.getDuplicateServerNode().getInstanceId());
        assert jSchSession != null;
        HostSystem hostSystem = hostSystemHandler.buildHostSystem(baseMessage.getServerNode(), baseMessage);
        RemoteInvokeHandler.openWebTerminal(terminalSession.getSessionId(), baseMessage.getServerNode().getInstanceId(), hostSystem);
        terminalSessionInstanceService.add(TerminalSessionInstanceBuilder.build(terminalSession, hostSystem, InstanceSessionTypeEnum.SERVER));
    }

    @Override
    protected ServerMessage.DuplicateSession toMessage(String message) {
        return new GsonBuilder().create().fromJson(message, ServerMessage.DuplicateSession.class);
    }

}