package com.baiyi.opscloud.factory.xterm.impl;

import com.baiyi.opscloud.common.base.XTermRequestStatus;
import com.baiyi.opscloud.domain.generator.opscloud.OcTerminalSession;
import com.baiyi.opscloud.factory.xterm.IXTermProcess;
import com.baiyi.opscloud.xterm.message.BaseMessage;
import com.baiyi.opscloud.xterm.message.CommandMessage;
import com.baiyi.opscloud.xterm.model.JSchSession;
import com.baiyi.opscloud.xterm.model.JSchSessionMap;
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
public class CommandProcess extends BaseProcess implements IXTermProcess {

    /**
     * XTerm发送指令
     *
     * @return
     */

    @Override
    public String getKey() {
        return XTermRequestStatus.COMMAND.getCode();
    }

    @Override
    public void xtermProcess(String message, Session session, OcTerminalSession ocTerminalSession) {
        CommandMessage xtermMessage = (CommandMessage) getMessage(message);
        if (StringUtils.isEmpty(xtermMessage.getData()))
            return;
        if (!isBatch(ocTerminalSession)) {
            printCommand(ocTerminalSession.getSessionId(), xtermMessage.getInstanceId(), xtermMessage.getData());
        } else {
            Map<String, JSchSession> sessionMap = JSchSessionMap.getBySessionId(ocTerminalSession.getSessionId());
            sessionMap.keySet().forEach(e -> printCommand(ocTerminalSession.getSessionId(), e, xtermMessage.getData()));
        }
    }

    private void printCommand(String sessionId, String instanceId, String cmd) {
        JSchSession jSchSession = JSchSessionMap.getBySessionId(sessionId, instanceId);
        if (jSchSession == null) return;
        jSchSession.getCommander().print(cmd);

//        char[] cs = cmd.toCharArray();
//        for (char c : cs) {
//            if (c == '\u007F') {
//                jSchSession.appendCmd("\\b");
//            } else {
//                jSchSession.appendCmd(c);
//            }
//        }
    }

    @Override
    protected BaseMessage getMessage(String message) {
        return new GsonBuilder().create().fromJson(message, CommandMessage.class);
    }

}
