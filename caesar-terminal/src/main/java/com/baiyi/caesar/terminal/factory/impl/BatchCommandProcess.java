package com.baiyi.caesar.terminal.factory.impl;

import com.baiyi.caesar.domain.generator.caesar.TerminalSession;
import com.baiyi.caesar.terminal.enums.MessageState;
import com.baiyi.caesar.terminal.factory.BaseProcess;
import com.baiyi.caesar.terminal.factory.ITerminalProcess;
import com.baiyi.caesar.sshcore.message.BaseMessage;
import com.baiyi.caesar.sshcore.message.BatchCommandMessage;
import com.baiyi.caesar.sshcore.model.JSchSessionContainer;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.Session;

/**
 * @Author baiyi
 * @Date 2020/5/11 7:22 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class BatchCommandProcess extends BaseProcess implements ITerminalProcess {

    /**
     * 设置批量命令
     * @return
     */

    @Override
    public String getState() {
        return MessageState.BATCH_COMMAND.getState();
    }

    @Override
    public void process(String message, Session session, TerminalSession terminalSession) {
        BatchCommandMessage batchMessage = (BatchCommandMessage) getMessage(message);
        JSchSessionContainer.setBatch(terminalSession.getSessionId(), batchMessage.getIsBatch());
    }

    @Override
    protected BaseMessage getMessage(String message) {
        return new GsonBuilder().create().fromJson(message, BatchCommandMessage.class);
    }

}
