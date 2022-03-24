package com.baiyi.opscloud.terminal.processor.impl;

import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.sshcore.enums.MessageState;
import com.baiyi.opscloud.sshcore.message.ServerMessage;
import com.baiyi.opscloud.terminal.processor.AbstractServerTerminalProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.Session;

/**
 * 心跳
 * @Author baiyi
 * @Date 2020/5/13 6:50 下午
 * @Version 1.0 HEARTBEAT
 */
@Slf4j
@Component
public class HeartbeatProcessor extends AbstractServerTerminalProcessor<ServerMessage.BaseMessage> {

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
    protected ServerMessage.BaseMessage getMessage(String message) {
        return null;
    }

}
