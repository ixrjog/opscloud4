package com.baiyi.opscloud.terminal.processor.impl;

import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.sshcore.enums.MessageState;
import com.baiyi.opscloud.sshcore.message.ServerMessage;
import com.baiyi.opscloud.sshcore.model.JSchSession;
import com.baiyi.opscloud.sshcore.model.JSchSessionContainer;
import com.baiyi.opscloud.terminal.processor.AbstractServerTerminalProcessor;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.StringUtils;
import javax.websocket.Session;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/5/11 9:38 上午
 * @Version 1.0
 */
@Component
@Slf4j
public class CommandProcessor extends AbstractServerTerminalProcessor<ServerMessage.Command> {

    /**
     * 发送指令
     *
     * @return
     */
    @Override
    public String getState() {
        return MessageState.COMMAND.getState();
    }

    @Override
    public void process(String message, Session session, TerminalSession terminalSession) {
        ServerMessage.Command commandMessage = getMessage(message);
        if (StringUtils.isEmpty(commandMessage.getData()))
            return;
        if (!needBatch(terminalSession)) {
            sendCommand(terminalSession.getSessionId(), commandMessage.getInstanceId(), commandMessage.getData());
        } else {
            Map<String, JSchSession> sessionMap = JSchSessionContainer.getBySessionId(terminalSession.getSessionId());
            sessionMap.keySet().parallelStream().forEach(e -> sendCommand(terminalSession.getSessionId(), e, commandMessage.getData()));
        }
    }

    private void sendCommand(String sessionId, String instanceId, String cmd) {
        JSchSession jSchSession = JSchSessionContainer.getBySessionId(sessionId, instanceId);
        if (jSchSession == null) return;
        jSchSession.getCommander().print(cmd);
    }

    @Override
    protected ServerMessage.Command getMessage(String message) {
        return new GsonBuilder().create().fromJson(message, ServerMessage.Command.class);
    }

}
