package com.baiyi.opscloud.factory.xterm.impl;

import com.baiyi.opscloud.common.base.XTermRequestStatus;
import com.baiyi.opscloud.factory.xterm.IXTermProcess;
import com.baiyi.opscloud.xterm.message.BaseXTermMessage;
import com.baiyi.opscloud.xterm.message.LogoutMessage;
import com.baiyi.opscloud.xterm.model.JSchSession;
import com.baiyi.opscloud.xterm.model.JSchSessionMap;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;

import javax.websocket.Session;

/**
 * @Author baiyi
 * @Date 2020/5/11 5:19 下午
 * @Version 1.0
 */
@Component
public class XTermLogoutProcess extends BaseXTermProcess implements IXTermProcess {

    /**
     * 初始化XTerm 单个关闭
     *
     * @return
     */

    @Override
    public String getKey() {
        return XTermRequestStatus.LOGOUT.getCode();
    }

    @Override
    public void xtermProcess(String message, Session session) {
        LogoutMessage xtermMessage = (LogoutMessage) getXTermMessage(message);

        JSchSession jSchSession = JSchSessionMap.getBySessionId(session.getId(), xtermMessage.getInstanceId());
        jSchSession.getChannel().disconnect();
        jSchSession.setCommander(null);
        jSchSession.setChannel(null);
        jSchSession.setInputToChannel(null);
        jSchSession = null;
        JSchSessionMap.removeSession(session.getId(), xtermMessage.getInstanceId());
    }

    @Override
    protected BaseXTermMessage getXTermMessage(String message) {
        LogoutMessage xtermMessage = new GsonBuilder().create().fromJson(message, LogoutMessage.class);
        return xtermMessage;
    }
}
