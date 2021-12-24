package com.baiyi.opscloud.kubernetes.terminal.process.impl;

import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.kubernetes.terminal.process.AbstractKubernetesTerminalProcess;
import com.baiyi.opscloud.sshcore.ITerminalProcess;
import com.baiyi.opscloud.sshcore.enums.MessageState;
import com.baiyi.opscloud.sshcore.message.KubernetesMessage;
import org.springframework.stereotype.Component;

import javax.websocket.Session;

/**
 * @Author baiyi
 * @Date 2021/7/16 10:25 上午
 * @Version 1.0
 */
@Component
public class KubernetesTerminalHeartbeatProcess extends AbstractKubernetesTerminalProcess<KubernetesMessage.BaseMessage> implements ITerminalProcess {

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
    public void process(String message, Session session, TerminalSession terminalSession) {
    }

    @Override
    protected KubernetesMessage.BaseMessage getMessage(String message) {
        return KubernetesMessage.BaseMessage.HEARTBEAT;
    }

}
