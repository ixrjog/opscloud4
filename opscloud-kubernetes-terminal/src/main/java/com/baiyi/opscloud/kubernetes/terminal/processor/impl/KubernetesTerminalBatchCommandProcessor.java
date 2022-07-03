package com.baiyi.opscloud.kubernetes.terminal.processor.impl;

import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.kubernetes.terminal.processor.AbstractKubernetesTerminalProcessor;
import com.baiyi.opscloud.sshcore.ITerminalProcessor;
import com.baiyi.opscloud.sshcore.enums.MessageState;
import com.baiyi.opscloud.sshcore.message.KubernetesMessage;
import com.baiyi.opscloud.sshcore.model.KubernetesSessionContainer;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;

import javax.websocket.Session;

/**
 * @Author baiyi
 * @Date 2022/7/3 15:45
 * @Version 1.0
 */
@Component
public class KubernetesTerminalBatchCommandProcessor extends AbstractKubernetesTerminalProcessor<KubernetesMessage.BatchCommand> implements ITerminalProcessor {

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
    public void process(String message, Session session, TerminalSession terminalSession) {
        KubernetesMessage.BatchCommand batchMessage = getMessage(message);
        KubernetesSessionContainer.setBatch(terminalSession.getSessionId(), batchMessage.getIsBatch());
    }

    @Override
    protected KubernetesMessage.BatchCommand getMessage(String message) {
        return new GsonBuilder().create().fromJson(message, KubernetesMessage.BatchCommand.class);
    }

}
