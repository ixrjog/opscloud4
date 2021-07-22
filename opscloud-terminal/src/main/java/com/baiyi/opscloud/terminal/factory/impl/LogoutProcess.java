package com.baiyi.opscloud.terminal.factory.impl;

import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.sshcore.base.ITerminalProcess;
import com.baiyi.opscloud.sshcore.enums.MessageState;
import com.baiyi.opscloud.sshcore.message.server.ServerLogoutMessage;
import com.baiyi.opscloud.sshcore.model.JSchSessionContainer;
import com.baiyi.opscloud.terminal.factory.AbstractServerTerminalProcess;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;

import javax.websocket.Session;

/**
 * @Author baiyi
 * @Date 2020/5/11 5:19 下午
 * @Version 1.0
 */
@Component
public class LogoutProcess extends AbstractServerTerminalProcess<ServerLogoutMessage> implements ITerminalProcess {

    /**
     * 单个关闭
     *
     * @return
     */


    @Override
    public String getState() {
        return MessageState.LOGOUT.getState();
    }

    @Override
    public void process(String message, Session session, TerminalSession terminalSession) {
        ServerLogoutMessage baseMessage = getMessage(message);
        recordAuditLog(terminalSession, baseMessage.getInstanceId()); // 写审计日志
        simpleTerminalSessionFacade.closeTerminalSessionInstance(terminalSession, baseMessage.getInstanceId()); // 设置关闭会话
        JSchSessionContainer.closeSession(terminalSession.getSessionId(), baseMessage.getInstanceId());
    }

    @Override
    protected ServerLogoutMessage getMessage(String message) {
        return new GsonBuilder().create().fromJson(message, ServerLogoutMessage.class);
    }
}
