package com.baiyi.caesar.terminal.factory.impl;

import com.baiyi.caesar.domain.generator.caesar.TerminalSession;
import com.baiyi.caesar.terminal.builder.TerminalSessionInstanceBuilder;
import com.baiyi.caesar.terminal.enums.MessageState;
import com.baiyi.caesar.terminal.factory.BaseProcess;
import com.baiyi.caesar.terminal.factory.ITerminalProcess;
import com.baiyi.caesar.sshcore.handler.RemoteInvokeHandler;
import com.baiyi.caesar.sshcore.message.BaseMessage;
import com.baiyi.caesar.sshcore.message.DuplicateSessionMessage;
import com.baiyi.caesar.sshcore.model.HostSystem;
import com.baiyi.caesar.sshcore.model.JSchSession;
import com.baiyi.caesar.sshcore.model.JSchSessionContainer;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;

import javax.websocket.Session;

/**
 * @Author baiyi
 * @Date 2020/5/13 10:24 上午
 * @Version 1.0
 */
@Component
public class DuplicateSessionProcess extends BaseProcess implements ITerminalProcess {

    /**
     * 复制会话
     *
     * @return
     */

    @Override
    public String getState() {
        return MessageState.DUPLICATE_SESSION.getState();
    }

    @Override
    public void process(String message, Session session, TerminalSession terminalSession) {
        DuplicateSessionMessage baseMessage = (DuplicateSessionMessage) getMessage(message);
        JSchSession jSchSession = JSchSessionContainer.getBySessionId(terminalSession.getSessionId(), baseMessage.getDuplicateServerNode().getInstanceId());
        assert jSchSession != null;
        HostSystem hostSystem = hostSystemHandler.buildHostSystem(baseMessage.getServerNode(), baseMessage);
        RemoteInvokeHandler.openSSHTermOnSystemForWebTerminal(terminalSession.getSessionId(), baseMessage.getServerNode().getInstanceId(), hostSystem);
        terminalSessionInstanceService.add(TerminalSessionInstanceBuilder.build(terminalSession, hostSystem));
    }


    @Override
    protected BaseMessage getMessage(String message) {
        return new GsonBuilder().create().fromJson(message, DuplicateSessionMessage.class);
    }

}
