package com.baiyi.caesar.terminal.factory.impl;

import com.baiyi.caesar.domain.generator.caesar.TerminalSession;
import com.baiyi.caesar.terminal.builder.TerminalSessionInstanceBuilder;
import com.baiyi.caesar.terminal.enums.MessageState;
import com.baiyi.caesar.terminal.factory.BaseProcess;
import com.baiyi.caesar.terminal.factory.ITerminalProcess;
import com.baiyi.caesar.terminal.handler.RemoteInvokeHandler;
import com.baiyi.caesar.terminal.message.BaseMessage;
import com.baiyi.caesar.terminal.message.LoginMessage;
import com.baiyi.caesar.terminal.model.HostSystem;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;

import javax.websocket.Session;

/**
 * @Author baiyi
 * @Date 2020/5/11 9:36 上午
 * @Version 1.0
 */
@Component
public class LoginProcess extends BaseProcess implements ITerminalProcess {

    /**
     * 登录
     *
     * @return
     */
    @Override
    public String getState() {
        return MessageState.LOGIN.getState();
    }

    @Override
    public void process(String message, Session session, TerminalSession terminalSession) {
        LoginMessage loginMessage = (LoginMessage) getMessage(message);
        heartbeat(terminalSession.getSessionId());
        loginMessage.getServerNodes().forEach(serverNode -> {
            HostSystem hostSystem = buildHostSystem(serverNode, loginMessage);
            RemoteInvokeHandler.openSSHTermOnSystem(terminalSession.getSessionId(), serverNode.getInstanceId(), hostSystem);
            terminalSessionInstanceService.add(TerminalSessionInstanceBuilder.build(terminalSession, hostSystem));
        });
    }

    @Override
    protected BaseMessage getMessage(String message) {
        return new GsonBuilder().create().fromJson(message, LoginMessage.class);
    }

}
