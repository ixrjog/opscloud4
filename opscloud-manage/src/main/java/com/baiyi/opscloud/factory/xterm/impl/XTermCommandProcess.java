package com.baiyi.opscloud.factory.xterm.impl;

import com.baiyi.opscloud.common.base.XTermRequestStatus;
import com.baiyi.opscloud.factory.xterm.IXTermProcess;
import com.baiyi.opscloud.xterm.message.BaseXTermWSMessage;
import com.baiyi.opscloud.xterm.message.XTermCmdWSMessage;
import com.baiyi.opscloud.xterm.model.JSchSession;
import com.baiyi.opscloud.xterm.model.JSchSessionMap;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;

import javax.websocket.Session;

/**
 * @Author baiyi
 * @Date 2020/5/11 9:38 上午
 * @Version 1.0
 */
@Component
public class XTermCommandProcess extends BaseXTermProcess implements IXTermProcess {

    @Override
    public String getKey() {
        return XTermRequestStatus.COMMAND.getCode();
    }

    @Override
    public void xtermProcess(String message, Session session) {
        XTermCmdWSMessage cmdMessage = (XTermCmdWSMessage) getXTermMessage(message);
        JSchSession jSchSession = JSchSessionMap.getBySessionId(sessionId, cmdMessage.getInstanceId());
        jSchSession.getCommander().print(cmdMessage.getData());
    }

    @Override
    protected BaseXTermWSMessage getXTermMessage(String message) {
        XTermCmdWSMessage cmdMessage = new GsonBuilder().create().fromJson(message, XTermCmdWSMessage.class);
        return cmdMessage;
    }


}
