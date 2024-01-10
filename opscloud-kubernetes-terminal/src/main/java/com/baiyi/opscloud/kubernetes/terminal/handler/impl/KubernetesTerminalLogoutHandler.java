package com.baiyi.opscloud.kubernetes.terminal.handler.impl;

import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.kubernetes.terminal.handler.AbstractKubernetesTerminalMessageHandler;
import com.baiyi.opscloud.sshcore.ITerminalMessageHandler;
import com.baiyi.opscloud.sshcore.audit.PodCommandAudit;
import com.baiyi.opscloud.sshcore.enums.MessageState;
import com.baiyi.opscloud.sshcore.message.KubernetesMessage;
import com.baiyi.opscloud.sshcore.model.KubernetesSessionContainer;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import jakarta.websocket.Session;

/**
 * @Author baiyi
 * @Date 2021/7/16 5:17 下午
 * @Version 1.0
 */
@Component
public class KubernetesTerminalLogoutHandler extends AbstractKubernetesTerminalMessageHandler<KubernetesMessage.Logout> implements ITerminalMessageHandler {

    @Resource
    private PodCommandAudit podCommandAudit;

    /**
     * 单个关闭
     *
     * @return
     */
    @Override
    public String getState() {
        return MessageState.LOGOUT.getState();
    }

    @Override
    public void handle(String message, Session session, TerminalSession terminalSession) {
        KubernetesMessage.Logout baseMessage = toMessage(message);
        // 设置关闭会话
        simpleTerminalSessionFacade.closeTerminalSessionInstance(terminalSession, baseMessage.getInstanceId());
        KubernetesSessionContainer.closeSession(terminalSession.getSessionId(), baseMessage.getInstanceId());
        podCommandAudit.asyncRecordCommand(terminalSession.getSessionId(), baseMessage.getInstanceId());
    }

    @Override
    protected KubernetesMessage.Logout toMessage(String message) {
        return new GsonBuilder().create().fromJson(message, KubernetesMessage.Logout.class);
    }

}