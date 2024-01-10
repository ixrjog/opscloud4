package com.baiyi.opscloud.kubernetes.terminal.handler.impl;

import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.kubernetes.terminal.handler.AbstractKubernetesTerminalMessageHandler;
import com.baiyi.opscloud.sshcore.ITerminalMessageHandler;
import com.baiyi.opscloud.sshcore.enums.MessageState;
import com.baiyi.opscloud.sshcore.message.KubernetesMessage;
import com.baiyi.opscloud.sshcore.model.KubernetesSession;
import com.baiyi.opscloud.sshcore.model.KubernetesSessionContainer;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.StringUtils;
import jakarta.websocket.Session;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/7/19 2:25 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class KubernetesTerminalCommandHandler extends AbstractKubernetesTerminalMessageHandler<KubernetesMessage.Command> implements ITerminalMessageHandler {

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
    public void handle(String message, Session session, TerminalSession terminalSession) {
        KubernetesMessage.Command commandMessage = toMessage(message);
        if (StringUtils.isEmpty(commandMessage.getCommand())) {
            return;
        }
        if (!hasBatchFlag(terminalSession)) {
            sendCommand(terminalSession.getSessionId(), commandMessage.getInstanceId(), commandMessage.getCommand());
        } else {
            Map<String, KubernetesSession> sessionMap = KubernetesSessionContainer.getBySessionId(terminalSession.getSessionId());
            sessionMap.keySet().parallelStream().forEach(e -> sendCommand(terminalSession.getSessionId(), e, commandMessage.getCommand()));
        }
    }

    private void sendCommand(String sessionId, String instanceId, String cmd) {
        KubernetesSession kubernetesSession = KubernetesSessionContainer.getBySessionId(sessionId, instanceId);
        if (kubernetesSession == null) {
            return;
        }
        try {
            OutputStream out = kubernetesSession.getExecWatch().getInput();
            out.write(cmd.getBytes(StandardCharsets.UTF_8));
            out.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    protected Boolean hasBatchFlag(TerminalSession terminalSession) {
        Boolean isBatch = KubernetesSessionContainer.getBatchFlagBySessionId(terminalSession.getSessionId());
        return isBatch != null && isBatch;
    }

    @Override
    protected KubernetesMessage.Command toMessage(String message) {
        return new GsonBuilder().create().fromJson(message, KubernetesMessage.Command.class);
    }

}