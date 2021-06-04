package com.baiyi.caesar.terminal.factory.impl;

import com.baiyi.caesar.domain.generator.caesar.TerminalSession;
import com.baiyi.caesar.terminal.enums.MessageState;
import com.baiyi.caesar.terminal.factory.BaseProcess;
import com.baiyi.caesar.terminal.factory.ITerminalProcess;
import com.baiyi.caesar.sshcore.message.BaseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.Session;

/**
 * @Author baiyi
 * @Date 2020/5/13 6:50 下午
 * @Version 1.0 HEARTBEAT
 */
@Slf4j
@Component
public class HeartbeatProcess extends BaseProcess implements ITerminalProcess {

    /**
     * 心跳
     *
     * @return
     */


    @Override
    public String getState() {
        return MessageState.HEARTBEAT.getState();
    }

    @Override
    public void process(String message, Session session, TerminalSession terminalSession) {
        //  log.info("收到前端心跳");
        heartbeat(terminalSession.getSessionId());
    }

    @Override
    protected BaseMessage getMessage(String message) {
        return null;
    }

}
