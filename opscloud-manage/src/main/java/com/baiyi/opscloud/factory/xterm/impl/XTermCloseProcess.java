package com.baiyi.opscloud.factory.xterm.impl;

import com.baiyi.opscloud.common.base.XTermRequestStatus;
import com.baiyi.opscloud.factory.xterm.IXTermProcess;
import com.baiyi.opscloud.xterm.message.BaseXTermMessage;
import com.baiyi.opscloud.xterm.model.JSchSession;
import com.baiyi.opscloud.xterm.model.JSchSessionMap;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/5/11 9:38 上午
 * @Version 1.0
 */
@Component
public class XTermCloseProcess extends BaseXTermProcess implements IXTermProcess {

    /**
     * 关闭所有XTerm
     * @return
     */

    @Override
    public String getKey() {
        return XTermRequestStatus.CLOSE.getCode();
    }

    @Override
    public void xtermProcess(String message, Session session) {
        Map<String, JSchSession> sessionMap = JSchSessionMap.getBySessionId(session.getId());
        if(sessionMap == null) return;
        for (String instanceId : sessionMap.keySet()) {
            try {
                JSchSession jSchSession = sessionMap.get(instanceId);
                jSchSession.getChannel().disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            sessionMap.clear();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected BaseXTermMessage getXTermMessage(String message) {
        return null;
    }
}
