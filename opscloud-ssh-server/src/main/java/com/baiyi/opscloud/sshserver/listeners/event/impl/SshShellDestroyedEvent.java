package com.baiyi.opscloud.sshserver.listeners.event.impl;

import com.baiyi.opscloud.sshserver.command.custom.context.SessionCommandContext;
import com.baiyi.opscloud.sshserver.listeners.SshShellEvent;
import com.baiyi.opscloud.sshserver.listeners.SshShellEventType;
import com.baiyi.opscloud.sshserver.listeners.event.AbstractSshShellEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/9/1 17:09
 * @Version 1.0
 */
@Slf4j
@Component
public class SshShellDestroyedEvent extends AbstractSshShellEvent {

    @Override
    public String getEventType() {
        return SshShellEventType.SESSION_DESTROYED.name();
    }

    /**
     * 销毁补偿
     * @param event
     */
    @Override
    public void handle(SshShellEvent event) {
        SessionCommandContext.remove();
        closeTerminalSession(event);
    }

}