package com.baiyi.caesar.factory.terminal.impl;

import com.baiyi.caesar.builder.TerminalSessionInstanceBuilder;
import com.baiyi.caesar.domain.generator.caesar.TerminalSession;
import com.baiyi.caesar.factory.terminal.BaseProcess;
import com.baiyi.caesar.factory.terminal.ITerminalProcess;
import com.baiyi.caesar.terminal.enums.MessageState;
import com.baiyi.caesar.terminal.handler.RemoteInvokeHandler;
import com.baiyi.caesar.terminal.message.BaseMessage;
import com.baiyi.caesar.terminal.message.LoginMessage;
import com.baiyi.caesar.terminal.model.HostSystem;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.util.Map;

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

        Map<String, String> serverTreeHostPatternMap = null;

        heartbeat(terminalSession.getSessionId());

        loginMessage.getInstanceIds().parallelStream().forEach(k -> {
            if (serverTreeHostPatternMap.containsKey(k)) {
                String host = serverTreeHostPatternMap.get(k);
                HostSystem hostSystem = buildHostSystem(host, loginMessage);
                RemoteInvokeHandler.openSSHTermOnSystem(terminalSession.getSessionId(), k, hostSystem);
                terminalSessionInstanceService.add(TerminalSessionInstanceBuilder.build(terminalSession, hostSystem));
            }
        });
    }

    @Override
    protected BaseMessage getMessage(String message) {
        return new GsonBuilder().create().fromJson(message, LoginMessage.class);
    }

}
