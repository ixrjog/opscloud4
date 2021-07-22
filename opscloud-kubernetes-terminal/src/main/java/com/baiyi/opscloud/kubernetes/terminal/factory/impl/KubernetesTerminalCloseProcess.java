package com.baiyi.opscloud.kubernetes.terminal.factory.impl;

import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.kubernetes.terminal.factory.AbstractKubernetesTerminalProcess;
import com.baiyi.opscloud.sshcore.base.ITerminalProcess;
import com.baiyi.opscloud.sshcore.enums.MessageState;
import com.baiyi.opscloud.sshcore.message.kubernetes.BaseKubernetesMessage;
import com.baiyi.opscloud.sshcore.model.KubernetesSession;
import com.baiyi.opscloud.sshcore.model.KubernetesSessionContainer;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.util.Date;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/7/16 5:08 下午
 * @Version 1.0
 */
@Component
public class KubernetesTerminalCloseProcess extends AbstractKubernetesTerminalProcess<BaseKubernetesMessage> implements ITerminalProcess {

    @Override
    public String getState() {
        return MessageState.CLOSE.getState();
    }

    @Override
    public void process(String message, Session session, TerminalSession terminalSession) {

        Map<String, KubernetesSession> sessionMap = KubernetesSessionContainer.getBySessionId(terminalSession.getSessionId());
        if (sessionMap == null) return;
        for (String instanceId : sessionMap.keySet())
            try {
                KubernetesSession kubernetesSession = sessionMap.get(instanceId);
                //  kubernetesSession.getLogWatch().close();
                // recordAuditLog(terminalSession, instanceId); // 写审计日志
                //  writeCommanderLog(jSchSession.getCommanderLog(),ocTerminalSession, instanceId); // 写命令日志
                simpleTerminalSessionFacade.closeTerminalSessionInstance(terminalSession, instanceId);  // 设置关闭会话
                KubernetesSessionContainer.closeSession(terminalSession.getSessionId(), instanceId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        try {
            sessionMap.clear();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        terminalSession.setCloseTime(new Date());
        terminalSession.setSessionClosed(true);
        terminalSessionService.update(terminalSession);
        terminalSession = null;
    }

    @Override
    protected BaseKubernetesMessage getMessage(String message) {
        return BaseKubernetesMessage.CLOSE;
    }
}
