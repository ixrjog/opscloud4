package com.baiyi.opscloud.terminal.processor.impl;

import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.sshcore.builder.TerminalSessionInstanceBuilder;
import com.baiyi.opscloud.sshcore.enums.InstanceSessionTypeEnum;
import com.baiyi.opscloud.sshcore.enums.MessageState;
import com.baiyi.opscloud.sshcore.handler.RemoteInvokeHandler;
import com.baiyi.opscloud.sshcore.message.ServerMessage;
import com.baiyi.opscloud.sshcore.model.HostSystem;
import com.baiyi.opscloud.sshcore.model.ServerNode;
import com.baiyi.opscloud.terminal.processor.AbstractServerTerminalProcessor;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author baiyi
 * @Date 2020/5/11 9:36 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class LoginProcessor extends AbstractServerTerminalProcessor<ServerMessage.Login> {

    private static final ExecutorService executor = Executors.newFixedThreadPool(4);

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
        ServerMessage.Login loginMessage = getMessage(message);
        heartbeat(terminalSession.getSessionId());
        for (ServerNode serverNode : loginMessage.getServerNodes()) {
            executor.submit(() -> {
                log.info("初始化serverNode: instanceId = {} ",serverNode.getInstanceId());
                HostSystem hostSystem = hostSystemHandler.buildHostSystem(serverNode, loginMessage);
                RemoteInvokeHandler.openWebTerminal(terminalSession.getSessionId(), serverNode.getInstanceId(), hostSystem);
                terminalSessionInstanceService.add(TerminalSessionInstanceBuilder.build(terminalSession, hostSystem, InstanceSessionTypeEnum.SERVER));
            });
        }
    }

    @Override
    protected ServerMessage.Login getMessage(String message) {
        return new GsonBuilder().create().fromJson(message, ServerMessage.Login.class);
    }

}
