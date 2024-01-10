package com.baiyi.opscloud.kubernetes.terminal.handler.impl;

import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.kubernetes.terminal.handler.AbstractKubernetesTerminalMessageHandler;
import com.baiyi.opscloud.sshcore.ITerminalMessageHandler;
import com.baiyi.opscloud.sshcore.enums.MessageState;
import com.baiyi.opscloud.sshcore.message.KubernetesMessage;
import org.springframework.stereotype.Component;

import jakarta.websocket.Session;

/**
 * @Author baiyi
 * @Date 2021/7/16 10:25 上午
 * @Version 1.0
 */
@Component
public class KubernetesTerminalHeartbeatHandler extends AbstractKubernetesTerminalMessageHandler<KubernetesMessage.BaseMessage> implements ITerminalMessageHandler {

    /**
     * 登录
     *
     * @return
     */
    @Override
    public String getState() {
        return MessageState.HEARTBEAT.getState();
    }

    @Override
    public void handle(String message, Session session, TerminalSession terminalSession) {
    }

    @Override
    protected KubernetesMessage.BaseMessage toMessage(String message) {
        return KubernetesMessage.BaseMessage.HEARTBEAT;
    }

}