package com.baiyi.opscloud.terminal.factory.impl;

import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.terminal.builder.TerminalSessionInstanceBuilder;
import com.baiyi.opscloud.sshcore.enums.MessageState;
import com.baiyi.opscloud.terminal.factory.BaseProcess;
import com.baiyi.opscloud.terminal.factory.ITerminalProcess;
import com.baiyi.opscloud.sshcore.handler.RemoteInvokeHandler;
import com.baiyi.opscloud.sshcore.message.server.BaseServerMessage;
import com.baiyi.opscloud.sshcore.message.server.LoginMessage;
import com.baiyi.opscloud.sshcore.model.HostSystem;
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
            HostSystem hostSystem = hostSystemHandler.buildHostSystem(serverNode, loginMessage);
            RemoteInvokeHandler.openSSHTermOnSystemForWebTerminal(terminalSession.getSessionId(), serverNode.getInstanceId(), hostSystem);
            terminalSessionInstanceService.add(TerminalSessionInstanceBuilder.build(terminalSession, hostSystem));
        });
    }

    @Override
    protected BaseServerMessage getMessage(String message) {
        return new GsonBuilder().create().fromJson(message, LoginMessage.class);
    }

}
