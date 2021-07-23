package com.baiyi.opscloud.terminal.audit.impl;

import com.baiyi.opscloud.sshcore.enums.MessageState;
import com.baiyi.opscloud.sshcore.message.audit.TerminalAuditPlayMessage;
import com.baiyi.opscloud.sshcore.model.SessionOutput;
import com.baiyi.opscloud.sshcore.task.audit.TerminalAuditOutputTask;
import com.baiyi.opscloud.terminal.audit.AbstractTerminalAuditProcess;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.Session;

/**
 * @Author baiyi
 * @Date 2021/7/23 3:05 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class TerminalAuditPlayProcess extends AbstractTerminalAuditProcess<TerminalAuditPlayMessage> {

    /**
     * 播放
     *
     * @return
     */
    @Override
    public String getState() {
        return MessageState.PLAY.getState();
    }

    @Override
    public void process(String message, Session session) {
        TerminalAuditPlayMessage playMessage = getMessage(message);

        SessionOutput sessionOutput = new SessionOutput(playMessage.getSessionId(), playMessage.getInstanceId());
        // 启动线程处理会话
        Runnable run = new TerminalAuditOutputTask(session, sessionOutput);
        Thread thread = new Thread(run);
        thread.start();
    }

    @Override
    protected TerminalAuditPlayMessage getMessage(String message) {
        return new GsonBuilder().create().fromJson(message, TerminalAuditPlayMessage.class);
    }

}

