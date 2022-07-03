package com.baiyi.opscloud.kubernetes.terminal.processor.impl;

import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.kubernetes.terminal.processor.AbstractKubernetesTerminalProcessor;
import com.baiyi.opscloud.sshcore.ITerminalProcessor;
import com.baiyi.opscloud.sshcore.enums.MessageState;
import com.baiyi.opscloud.sshcore.message.KubernetesMessage;
import com.baiyi.opscloud.sshcore.model.KubernetesSession;
import com.baiyi.opscloud.sshcore.model.KubernetesSessionContainer;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.StringUtils;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/7/19 2:25 下午
 * @Version 1.0
 */
@Component
public class KubernetesTerminalCommandProcessor extends AbstractKubernetesTerminalProcessor<KubernetesMessage.Command> implements ITerminalProcessor {

    /**
     * 登录
     *
     * @return
     */
    @Override
    public String getState() {
        return MessageState.COMMAND.getState();
    }

    @Override
    public void process(String message, Session session, TerminalSession terminalSession) {
        KubernetesMessage.Command commandMessage = getMessage(message);
        if (StringUtils.isEmpty(commandMessage.getCommand()))
            return;
        if (!isBatch(terminalSession)) {
            sendCommand(terminalSession.getSessionId(), commandMessage.getInstanceId(), commandMessage.getCommand());
        } else {
            Map<String, KubernetesSession> sessionMap = KubernetesSessionContainer.getBySessionId(terminalSession.getSessionId());
            sessionMap.keySet().parallelStream().forEach(e -> sendCommand(terminalSession.getSessionId(), e, commandMessage.getCommand()));
        }
    }

    private void sendCommand(String sessionId, String instanceId, String cmd) {
        KubernetesSession kubernetesSession = KubernetesSessionContainer.getBySessionId(sessionId, instanceId);
        if (kubernetesSession == null) return;
        try {
            kubernetesSession.getExecWatch().getInput().write(cmd.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected KubernetesMessage.Command getMessage(String message) {
        return new GsonBuilder().create().fromJson(message, KubernetesMessage.Command.class);
    }

}
