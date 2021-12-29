package com.baiyi.opscloud.kubernetes.terminal.processor.impl;

import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.kubernetes.terminal.processor.AbstractKubernetesTerminalProcessor;
import com.baiyi.opscloud.sshcore.ITerminalProcess;
import com.baiyi.opscloud.sshcore.enums.MessageState;
import com.baiyi.opscloud.sshcore.message.KubernetesMessage;
import com.baiyi.opscloud.sshcore.model.KubernetesSessionContainer;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;

import javax.websocket.Session;

/**
 * @Author baiyi
 * @Date 2021/7/16 5:17 下午
 * @Version 1.0
 */
@Component
public class KubernetesTerminalLogoutProcessor extends AbstractKubernetesTerminalProcessor<KubernetesMessage.Logout> implements ITerminalProcess {

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
    public void process(String message, Session session, TerminalSession terminalSession) {
        KubernetesMessage.Logout baseMessage = getMessage(message);
        //recordAuditLog(terminalSession, baseMessage.getInstanceId()); // 写审计日志
        simpleTerminalSessionFacade.closeTerminalSessionInstance(terminalSession, baseMessage.getInstanceId());  // 设置关闭会话
        KubernetesSessionContainer.closeSession(terminalSession.getSessionId(), baseMessage.getInstanceId());
    }

    @Override
    protected KubernetesMessage.Logout getMessage(String message) {
        return new GsonBuilder().create().fromJson(message, KubernetesMessage.Logout.class);
    }
}
