package com.baiyi.opscloud.terminal.factory.impl;

import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.sshcore.base.ITerminalProcess;
import com.baiyi.opscloud.sshcore.enums.MessageState;
import com.baiyi.opscloud.sshcore.handler.RemoteInvokeHandler;
import com.baiyi.opscloud.sshcore.message.server.ServerResizeMessage;
import com.baiyi.opscloud.sshcore.model.JSchSession;
import com.baiyi.opscloud.sshcore.model.JSchSessionContainer;
import com.baiyi.opscloud.terminal.factory.AbstractServerTerminalProcess;
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
public class ResizeProcess extends AbstractServerTerminalProcess<ServerResizeMessage> implements ITerminalProcess {

    /**
     * XTerm改变形体
     *
     * @return
     */

    @Override
    public String getState() {
        return MessageState.RESIZE.getState();
    }

    @Override
    public void process(String message, Session session, TerminalSession terminalSession) {
        ServerResizeMessage resizeMessage = getMessage(message);
        try {
            JSchSession jSchSession = JSchSessionContainer.getBySessionId(terminalSession.getSessionId(), resizeMessage.getInstanceId());
            RemoteInvokeHandler.setChannelPtySize((ChannelShell) jSchSession.getChannel(), resizeMessage);
        } catch (Exception ignored) {
        }
    }

    @Override
    protected ServerResizeMessage getMessage(String message) {
        return new GsonBuilder().create().fromJson(message, ServerResizeMessage.class);
    }
}

