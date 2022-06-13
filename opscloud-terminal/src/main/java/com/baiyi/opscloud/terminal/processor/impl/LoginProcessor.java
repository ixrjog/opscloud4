package com.baiyi.opscloud.terminal.processor.impl;

import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.interceptor.SupserAdminInterceptor;
import com.baiyi.opscloud.service.server.ServerService;
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

import javax.annotation.Resource;
import javax.websocket.Session;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 登录
 *
 * @Author baiyi
 * @Date 2020/5/11 9:36 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class LoginProcessor extends AbstractServerTerminalProcessor<ServerMessage.Login> {

    @Resource
    private SupserAdminInterceptor sAInterceptor;

    @Resource
    private ServerService serverService;

    @Override
    public String getState() {
        return MessageState.LOGIN.getState();
    }

    @Override
    public void process(String message, Session session, TerminalSession terminalSession) {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        try {
            ServerMessage.Login loginMessage = getMessage(message);
            heartbeat(terminalSession.getSessionId());
            for (ServerNode serverNode : loginMessage.getServerNodes()) {
                executor.submit(() -> {
                    log.info("登录服务器节点: instanceId = {} ", serverNode.getInstanceId());
                    sAInterceptor.interceptLoginServer(serverNode.getId());
                    HostSystem hostSystem = hostSystemHandler.buildHostSystem(serverNode, loginMessage);
                    Server server = serverService.getById(serverNode.getId());
                    RemoteInvokeHandler.openWebTerminal(terminalSession.getSessionId(), serverNode.getInstanceId(), hostSystem);
                    terminalSessionInstanceService.add(TerminalSessionInstanceBuilder.build(terminalSession, hostSystem, InstanceSessionTypeEnum.SERVER));
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }

    @Override
    protected ServerMessage.Login getMessage(String message) {
        return new GsonBuilder().create().fromJson(message, ServerMessage.Login.class);
    }

}
