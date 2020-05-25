package com.baiyi.opscloud.factory.xterm.impl;

import com.baiyi.opscloud.common.base.XTermRequestStatus;
import com.baiyi.opscloud.domain.generator.opscloud.OcTerminalSession;
import com.baiyi.opscloud.factory.xterm.IXTermProcess;
import com.baiyi.opscloud.xterm.handler.RemoteInvokeHandler;
import com.baiyi.opscloud.xterm.message.BaseMessage;
import com.baiyi.opscloud.xterm.message.ResizeMessage;
import com.baiyi.opscloud.xterm.model.JSchSession;
import com.baiyi.opscloud.xterm.model.JSchSessionMap;
import com.google.gson.GsonBuilder;
import com.jcraft.jsch.ChannelShell;
import org.springframework.stereotype.Component;

import javax.websocket.Session;

/**
 * @Author baiyi
 * @Date 2020/5/12 10:43 上午
 * @Version 1.0
 */
@Component
public class ResizeProcess extends BaseProcess implements IXTermProcess {

    /**
     * XTerm改变形体
     * @return
     */

    @Override
    public String getKey() {
        return XTermRequestStatus.RESIZE.getCode();
    }

    @Override
    public void xtermProcess(String message, Session session, OcTerminalSession ocTerminalSession) {
        ResizeMessage xtermMessage= (ResizeMessage) getXTermMessage(message);
        try {
            JSchSession jSchSession = JSchSessionMap.getBySessionId(ocTerminalSession.getSessionId(), xtermMessage.getInstanceId());
            RemoteInvokeHandler.invokeChannelPtySize((ChannelShell)jSchSession.getChannel(),xtermMessage);
        } catch (Exception e) {
        }
    }

    @Override
    protected BaseMessage getXTermMessage(String message) {
        ResizeMessage xtermMessage = new GsonBuilder().create().fromJson(message, ResizeMessage.class);
        return  xtermMessage;
    }
}

