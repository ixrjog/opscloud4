package com.baiyi.opscloud.terminal.processor.impl;

import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.sshcore.enums.MessageState;
import com.baiyi.opscloud.sshcore.message.ServerMessage;
import com.baiyi.opscloud.sshcore.model.JSchSessionContainer;
import com.baiyi.opscloud.terminal.processor.AbstractServerTerminalProcessor;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.Session;

/**
 * 设置批量命令
 *
 * @Author baiyi
 * @Date 2020/5/11 7:22 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class BatchCommandProcessor extends AbstractServerTerminalProcessor<ServerMessage.BatchCommand> {

    @Override
    public String getState() {
        return MessageState.BATCH_COMMAND.getState();
    }

    @Override
    public void process(String message, Session session, TerminalSession terminalSession) {
        ServerMessage.BatchCommand batchMessage = getMessage(message);
        JSchSessionContainer.setBatch(terminalSession.getSessionId(), batchMessage.getIsBatch());
    }

    @Override
    protected ServerMessage.BatchCommand getMessage(String message) {
        return new GsonBuilder().create().fromJson(message, ServerMessage.BatchCommand.class);
    }

}
