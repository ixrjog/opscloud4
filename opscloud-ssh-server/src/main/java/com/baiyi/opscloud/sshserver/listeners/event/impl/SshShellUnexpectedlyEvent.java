package com.baiyi.opscloud.sshserver.listeners.event.impl;

import com.baiyi.opscloud.sshserver.command.custom.context.SessionCommandContext;
import com.baiyi.opscloud.sshserver.listeners.SshShellEvent;
import com.baiyi.opscloud.sshserver.listeners.SshShellEventType;
import com.baiyi.opscloud.sshserver.listeners.event.AbstractSshShellEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/8/30 15:39
 * @Version 1.0
 */
@Slf4j
@Component
public class SshShellUnexpectedlyEvent extends AbstractSshShellEvent {

    @Override
    public String getEventType() {
        return SshShellEventType.SESSION_STOPPED_UNEXPECTEDLY.name();
    }

    @Override
    public void handle(SshShellEvent event) {
        SessionCommandContext.remove();
        final String username = event.getSession().getServerSession().getUsername();
        log.warn("{} disconnects SSH-Server", username);
        closeTerminalSession(event);
    }

}