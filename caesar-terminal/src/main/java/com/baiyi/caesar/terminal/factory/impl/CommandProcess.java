package com.baiyi.caesar.terminal.factory.impl;

import com.baiyi.caesar.domain.generator.caesar.TerminalSession;
import com.baiyi.caesar.terminal.enums.MessageState;
import com.baiyi.caesar.terminal.factory.BaseProcess;
import com.baiyi.caesar.terminal.factory.ITerminalProcess;
import com.baiyi.caesar.sshcore.message.BaseMessage;
import com.baiyi.caesar.sshcore.message.CommandMessage;
import com.baiyi.caesar.sshcore.model.JSchSession;
import com.baiyi.caesar.sshcore.model.JSchSessionMap;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.websocket.Session;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/5/11 9:38 上午
 * @Version 1.0
 */
@Component
@Slf4j
public class CommandProcess extends BaseProcess implements ITerminalProcess {

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
        CommandMessage commandMessage = (CommandMessage) getMessage(message);
        if (StringUtils.isEmpty(commandMessage.getData()))
            return;
        if (!isBatch(terminalSession)) {
            printCommand(terminalSession.getSessionId(),commandMessage.getInstanceId(), commandMessage.getData());
        } else {
            Map<String, JSchSession> sessionMap = JSchSessionMap.getBySessionId(terminalSession.getSessionId());
            sessionMap.keySet().parallelStream().forEach(e -> printCommand(terminalSession.getSessionId(), e, commandMessage.getData()));
        }
    }

    private void printCommand(String sessionId, String instanceId, String cmd) {
        JSchSession jSchSession = JSchSessionMap.getBySessionId(sessionId, instanceId);
        if (jSchSession == null) return;
        jSchSession.getCommander().print(cmd);

    }

    @Override
    protected BaseMessage getMessage(String message) {
        return new GsonBuilder().create().fromJson(message, CommandMessage.class);
    }

}
