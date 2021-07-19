package com.baiyi.opscloud.kubernetes.terminal.factory.impl;

import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.kubernetes.terminal.factory.AbstractKubernetesTerminalProcess;
import com.baiyi.opscloud.sshcore.base.ITerminalProcess;
import com.baiyi.opscloud.sshcore.enums.MessageState;
import com.baiyi.opscloud.sshcore.message.kubernetes.KubernetesResizeMessage;
import com.baiyi.opscloud.sshcore.model.KubernetesSession;
import com.baiyi.opscloud.sshcore.model.KubernetesSessionContainer;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;

import javax.websocket.Session;

/**
 * @Author baiyi
 * @Date 2021/7/16 3:19 下午
 * @Version 1.0
 */
@Component
public class KubernetesTerminalResizeProcess extends AbstractKubernetesTerminalProcess<KubernetesResizeMessage> implements ITerminalProcess {

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
    public void process(String message, Session session, TerminalSession terminalSession) {
        KubernetesResizeMessage resizeMessage = getMessage(message);
        try {
            KubernetesSession kubernetesSession = KubernetesSessionContainer.getBySessionId(terminalSession.getSessionId(), resizeMessage.getInstanceId());
            if (kubernetesSession == null) return;
            kubernetesSession.resize(resizeMessage);
        } catch (Exception ignored) {
        }
    }

    @Override
    protected KubernetesResizeMessage getMessage(String message) {
        return new GsonBuilder().create().fromJson(message, KubernetesResizeMessage.class);
    }

}
