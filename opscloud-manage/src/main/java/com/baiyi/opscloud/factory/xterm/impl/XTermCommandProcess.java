package com.baiyi.opscloud.factory.xterm.impl;

import com.baiyi.opscloud.common.base.XTermRequestStatus;
import com.baiyi.opscloud.factory.xterm.IXTermProcess;
import com.baiyi.opscloud.xterm.message.BaseXTermWSMessage;
import com.baiyi.opscloud.xterm.message.XTermCommandWSMessage;
import com.baiyi.opscloud.xterm.model.JSchSession;
import com.baiyi.opscloud.xterm.model.JSchSessionMap;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/5/11 9:38 上午
 * @Version 1.0
 */
@Component
public class XTermCommandProcess extends BaseXTermProcess implements IXTermProcess {

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
    public void xtermProcess(String message, Session session) {
        XTermCommandWSMessage cmdMessage = (XTermCommandWSMessage) getXTermMessage(message);

        Boolean isBatch = JSchSessionMap.getBatchBySessionId(session.getId());
        if (isBatch == null)
            isBatch = false;

        if (!isBatch) {
            JSchSession jSchSession = JSchSessionMap.getBySessionId(session.getId(), cmdMessage.getInstanceId());
            jSchSession.getCommander().print(cmdMessage.getData());
        } else {
            Map<String, JSchSession> sessionMap = JSchSessionMap.getBySessionId(session.getId());
            for (String instanceId : sessionMap.keySet()) {
                JSchSession jSchSession = JSchSessionMap.getBySessionId(session.getId(), instanceId);
                jSchSession.getCommander().print(cmdMessage.getData());
            }
        }
    }

    @Override
    protected BaseXTermWSMessage getXTermMessage(String message) {
        XTermCommandWSMessage baseMessage = new GsonBuilder().create().fromJson(message, XTermCommandWSMessage.class);
        return baseMessage;
    }

}
