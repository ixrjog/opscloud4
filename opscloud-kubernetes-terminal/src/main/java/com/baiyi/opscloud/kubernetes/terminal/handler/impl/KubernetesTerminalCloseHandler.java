package com.baiyi.opscloud.kubernetes.terminal.handler.impl;

import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.kubernetes.terminal.handler.AbstractKubernetesTerminalMessageHandler;
import com.baiyi.opscloud.sshcore.ITerminalMessageHandler;
import com.baiyi.opscloud.sshcore.audit.PodCommandAudit;
import com.baiyi.opscloud.sshcore.enums.MessageState;
import com.baiyi.opscloud.sshcore.message.KubernetesMessage;
import com.baiyi.opscloud.sshcore.model.KubernetesSession;
import com.baiyi.opscloud.sshcore.model.KubernetesSessionContainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import jakarta.websocket.Session;
import java.util.Date;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/7/16 5:08 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class KubernetesTerminalCloseHandler extends AbstractKubernetesTerminalMessageHandler<KubernetesMessage.BaseMessage> implements ITerminalMessageHandler {

    @Resource
    private PodCommandAudit podCommandAudit;

    @Override
    public String getState() {
        return MessageState.CLOSE.getState();
    }

    @Override
    public void handle(String message, Session session, TerminalSession terminalSession) {
        Map<String, KubernetesSession> sessionMap = KubernetesSessionContainer.getBySessionId(terminalSession.getSessionId());
        if (sessionMap == null) {
            return;
        }
        for (String instanceId : sessionMap.keySet()) {
            try {
                KubernetesSession kubernetesSession = sessionMap.get(instanceId);
                // 设置关闭会话
                simpleTerminalSessionFacade.closeTerminalSessionInstance(terminalSession, instanceId);
                KubernetesSessionContainer.closeSession(terminalSession.getSessionId(), instanceId);
                podCommandAudit.asyncRecordCommand(terminalSession.getSessionId(), instanceId);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        try {
            sessionMap.clear();
            session.close();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        terminalSession.setCloseTime(new Date());
        terminalSession.setSessionClosed(true);
        terminalSessionService.update(terminalSession);
        terminalSession = null;
    }

    @Override
    protected KubernetesMessage.BaseMessage toMessage(String message) {
        return KubernetesMessage.BaseMessage.CLOSE;
    }

}