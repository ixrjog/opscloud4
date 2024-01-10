package com.baiyi.opscloud.kubernetes.terminal.handler.impl;

import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.kubernetes.terminal.handler.AbstractKubernetesTerminalMessageHandler;
import com.baiyi.opscloud.sshcore.ITerminalMessageHandler;
import com.baiyi.opscloud.sshcore.enums.MessageState;
import com.baiyi.opscloud.sshcore.message.KubernetesMessage;
import com.baiyi.opscloud.sshcore.model.KubernetesSession;
import com.baiyi.opscloud.sshcore.model.KubernetesSessionContainer;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;

import jakarta.websocket.Session;

/**
 * @Author baiyi
 * @Date 2021/7/16 3:19 下午
 * @Version 1.0
 */
@Component
public class KubernetesTerminalResizeHandler extends AbstractKubernetesTerminalMessageHandler<KubernetesMessage.Resize> implements ITerminalMessageHandler {

    /**
     * 调整终端
     *
     * @return
     */
    @Override
    public String getState() {
        return MessageState.RESIZE.getState();
    }

    @Override
    public void handle(String message, Session session, TerminalSession terminalSession) {
        KubernetesMessage.Resize resizeMessage = toMessage(message);
        try {
            KubernetesSession kubernetesSession = KubernetesSessionContainer.getBySessionId(terminalSession.getSessionId(), resizeMessage.getInstanceId());
            if (kubernetesSession == null) {
                return;
            }
            kubernetesSession.resize(resizeMessage);
        } catch (Exception ignored) {
        }
    }

    @Override
    protected KubernetesMessage.Resize toMessage(String message) {
        return new GsonBuilder().create().fromJson(message, KubernetesMessage.Resize.class);
    }

}