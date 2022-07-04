package com.baiyi.opscloud.terminal.handler.impl;

import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.sshcore.enums.MessageState;
import com.baiyi.opscloud.sshcore.message.ServerMessage;
import com.baiyi.opscloud.sshcore.model.JSchSessionContainer;
import com.baiyi.opscloud.terminal.handler.AbstractServerTerminalHandler;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;

import javax.websocket.Session;

/**
 * 登出服务器节点
 * @Author baiyi
 * @Date 2020/5/11 5:19 下午
 * @Version 1.0
 */
@Component
public class ServerTerminalLogoutHandler extends AbstractServerTerminalHandler<ServerMessage.Logout> {

    @Override
    public String getState() {
        return MessageState.LOGOUT.getState();
    }

    @Override
    public void handle(String message, Session session, TerminalSession terminalSession) {
        ServerMessage.Logout baseMessage = toMessage(message);
        //  recordAuditLog(terminalSession, baseMessage.getInstanceId()); // 写审计日志
        //  AuditRecordHandler.formatCommanderLog(terminalSession.getSessionId(),baseMessage.getInstanceId());
        simpleTerminalSessionFacade.closeTerminalSessionInstance(terminalSession, baseMessage.getInstanceId()); // 设置关闭会话
        serverCommandAudit.recordCommand(terminalSession.getSessionId(),baseMessage.getInstanceId());
        JSchSessionContainer.closeSession(terminalSession.getSessionId(), baseMessage.getInstanceId());
    }

    @Override
    protected ServerMessage.Logout toMessage(String message) {
        return new GsonBuilder().create().fromJson(message, ServerMessage.Logout.class);
    }

}
