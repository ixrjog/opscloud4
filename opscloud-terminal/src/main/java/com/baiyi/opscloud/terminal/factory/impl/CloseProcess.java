package com.baiyi.opscloud.terminal.factory.impl;

import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.terminal.enums.MessageState;
import com.baiyi.opscloud.terminal.factory.BaseProcess;
import com.baiyi.opscloud.terminal.factory.ITerminalProcess;
import com.baiyi.opscloud.sshcore.message.BaseMessage;
import com.baiyi.opscloud.sshcore.model.JSchSession;
import com.baiyi.opscloud.sshcore.model.JSchSessionContainer;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.util.Date;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/5/11 9:38 上午
 * @Version 1.0
 */
@Component
public class CloseProcess extends BaseProcess implements ITerminalProcess {

    /**
     * 关闭会话
     *
     * @return
     */


    @Override
    public String getState() {
        return MessageState.CLOSE.getState();
    }

    @Override
    public void process(String message, Session session, TerminalSession terminalSession) {
        Map<String, JSchSession> sessionMap = JSchSessionContainer.getBySessionId(terminalSession.getSessionId());
        if (sessionMap == null) return;
        for (String instanceId : sessionMap.keySet())
            try {
                JSchSession jSchSession = sessionMap.get(instanceId);
                jSchSession.getChannel().disconnect();
                recordAuditLog(terminalSession, instanceId); // 写审计日志
                //  writeCommanderLog(jSchSession.getCommanderLog(),ocTerminalSession, instanceId); // 写命令日志
                closeSessionInstance(terminalSession, instanceId); // 设置关闭会话
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
    protected BaseMessage getMessage(String message) {
        return null;
    }
}
