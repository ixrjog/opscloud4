package com.baiyi.caesar.terminal.factory.impl;

import com.baiyi.caesar.domain.generator.caesar.TerminalSession;
import com.baiyi.caesar.terminal.enums.MessageState;
import com.baiyi.caesar.terminal.factory.BaseProcess;
import com.baiyi.caesar.terminal.factory.ITerminalProcess;
import com.baiyi.caesar.sshcore.handler.RemoteInvokeHandler;
import com.baiyi.caesar.sshcore.message.BaseMessage;
import com.baiyi.caesar.sshcore.message.ResizeMessage;
import com.baiyi.caesar.sshcore.model.JSchSession;
import com.baiyi.caesar.sshcore.model.JSchSessionMap;
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
public class ResizeProcess extends BaseProcess implements ITerminalProcess {

    /**
     * XTerm改变形体
     * @return
     */

    @Override
    public String getState() {
        return MessageState.RESIZE.getState();
    }

    @Override
    public void process(String message, Session session, TerminalSession terminalSession) {
        ResizeMessage xtermMessage= (ResizeMessage) getMessage(message);
        try {
            JSchSession jSchSession = JSchSessionMap.getBySessionId(terminalSession.getSessionId(), xtermMessage.getInstanceId());
            RemoteInvokeHandler.setChannelPtySize((ChannelShell)jSchSession.getChannel(),xtermMessage);
        } catch (Exception ignored) {
        }
    }

    @Override
    protected BaseMessage getMessage(String message) {
        return new GsonBuilder().create().fromJson(message, ResizeMessage.class);
    }
}

