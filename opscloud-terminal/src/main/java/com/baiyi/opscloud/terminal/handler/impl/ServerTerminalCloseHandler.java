package com.baiyi.opscloud.terminal.handler.impl;

import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.sshcore.enums.MessageState;
import com.baiyi.opscloud.sshcore.message.ServerMessage;
import com.baiyi.opscloud.sshcore.model.JSchSession;
import com.baiyi.opscloud.sshcore.model.JSchSessionContainer;
import com.baiyi.opscloud.terminal.handler.AbstractServerTerminalHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.websocket.Session;
import java.io.IOException;
import java.util.Map;

/**
 * 关闭会话
 *
 * @Author baiyi
 * @Date 2020/5/11 9:38 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class ServerTerminalCloseHandler extends AbstractServerTerminalHandler<ServerMessage.BaseMessage> {

    @Override
    public String getState() {
        return MessageState.CLOSE.getState();
    }

    @Override
    public void handle(String message, Session session, TerminalSession terminalSession) {
        Map<String, JSchSession> sessionMap = JSchSessionContainer.getBySessionId(terminalSession.getSessionId());
        if (sessionMap == null) {
            return;
        }
        for (String instanceId : sessionMap.keySet()) {
            try {
                JSchSession jSchSession = sessionMap.get(instanceId);
                if (jSchSession.getChannel() != null) {
                    jSchSession.getChannel().disconnect();
                }
                jSchSession.setChannel(null);
                jSchSession.setInputToChannel(null);
                jSchSession.setCommander(null);
                jSchSession = null;
                // 设置关闭会话
                simpleTerminalSessionFacade.closeTerminalSessionInstance(terminalSession, instanceId);
                serverCommandAudit.asyncRecordCommand(terminalSession.getSessionId(), instanceId);
            } catch (Exception e) {
                log.warn("关闭JSchSession错误: instanceId={}, {}", instanceId, e.getMessage());
            }
        }
        try {
            sessionMap.clear();
            session.close();
        } catch (IOException e) {
            log.warn("关闭会话错误: {}", e.getMessage());
        }
        simpleTerminalSessionFacade.closeTerminalSession(terminalSession);
        terminalSession = null;
    }

    @Override
    protected ServerMessage.BaseMessage toMessage(String message) {
        return ServerMessage.BaseMessage.CLOSE;
    }

}