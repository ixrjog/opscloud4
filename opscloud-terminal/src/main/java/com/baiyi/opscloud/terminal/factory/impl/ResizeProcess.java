package com.baiyi.opscloud.terminal.factory.impl;

import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.terminal.enums.MessageState;
import com.baiyi.opscloud.terminal.factory.BaseProcess;
import com.baiyi.opscloud.terminal.factory.ITerminalProcess;
import com.baiyi.opscloud.sshcore.handler.RemoteInvokeHandler;
import com.baiyi.opscloud.sshcore.message.BaseMessage;
import com.baiyi.opscloud.sshcore.message.ResizeMessage;
import com.baiyi.opscloud.sshcore.model.JSchSession;
import com.baiyi.opscloud.sshcore.model.JSchSessionContainer;
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
        ResizeMessage resizeMessage= (ResizeMessage) getMessage(message);
        try {
            JSchSession jSchSession = JSchSessionContainer.getBySessionId(terminalSession.getSessionId(), resizeMessage.getInstanceId());
            RemoteInvokeHandler.setChannelPtySize((ChannelShell)jSchSession.getChannel(),resizeMessage);
        } catch (Exception ignored) {
        }
    }

    @Override
    protected BaseMessage getMessage(String message) {
        return new GsonBuilder().create().fromJson(message, ResizeMessage.class);
    }
}

