package com.baiyi.opscloud.kubernetes.terminal.handler.impl;

import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.kubernetes.terminal.handler.AbstractKubernetesTerminalMessageHandler;
import com.baiyi.opscloud.sshcore.ITerminalMessageHandler;
import com.baiyi.opscloud.sshcore.enums.MessageState;
import com.baiyi.opscloud.sshcore.message.KubernetesMessage;
import com.baiyi.opscloud.sshcore.model.KubernetesSessionContainer;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;

import jakarta.websocket.Session;

/**
 * @Author baiyi
 * @Date 2022/7/3 15:45
 * @Version 1.0
 */
@Component
public class KubernetesTerminalBatchCommandHandler extends AbstractKubernetesTerminalMessageHandler<KubernetesMessage.BatchCommand> implements ITerminalMessageHandler {

    /**
     * 登录
     *
     * @return
     */
    @Override
    public String getState() {
        return MessageState.BATCH_COMMAND.getState();
    }


    @Override
    public void handle(String message, Session session, TerminalSession terminalSession) {
        KubernetesMessage.BatchCommand batchMessage = toMessage(message);
        KubernetesSessionContainer.setBatchFlag(terminalSession.getSessionId(), batchMessage.getIsBatch());
    }

    @Override
    protected KubernetesMessage.BatchCommand toMessage(String message) {
        return new GsonBuilder().create().fromJson(message, KubernetesMessage.BatchCommand.class);
    }

}