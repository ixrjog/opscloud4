package com.baiyi.opscloud.factory.xterm.impl;

import com.baiyi.opscloud.common.base.XTermRequestStatus;
import com.baiyi.opscloud.domain.generator.opscloud.OcTerminalSession;
import com.baiyi.opscloud.factory.xterm.IXTermProcess;
import com.baiyi.opscloud.xterm.message.BaseMessage;
import com.baiyi.opscloud.xterm.message.CommandMessage;
import com.baiyi.opscloud.xterm.model.JSchSession;
import com.baiyi.opscloud.xterm.model.JSchSessionMap;
import com.google.gson.GsonBuilder;
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
        CommandMessage xtermMessage = (CommandMessage) getXTermMessage(message);
        if(StringUtils.isEmpty(xtermMessage.getData()))
            return;
        if (!isBatch(ocTerminalSession)) {
            JSchSession jSchSession = JSchSessionMap.getBySessionId(ocTerminalSession.getSessionId(), xtermMessage.getInstanceId());
            jSchSession.getCommander().print(xtermMessage.getData());
           //  List<SessionOutput> list = SessionOutputUtil.getOutput(session.getId());
        } else {
            Map<String, JSchSession> sessionMap = JSchSessionMap.getBySessionId(ocTerminalSession.getSessionId());
            for (String instanceId : sessionMap.keySet()) {
                JSchSession jSchSession = JSchSessionMap.getBySessionId(ocTerminalSession.getSessionId(), instanceId);
                jSchSession.getCommander().print(xtermMessage.getData());
            }
        }
    }

    @Override
    protected BaseMessage getXTermMessage(String message) {
        CommandMessage xtermMessage = new GsonBuilder().create().fromJson(message, CommandMessage.class);
        return xtermMessage;
    }

}
