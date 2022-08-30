package com.baiyi.opscloud.sshserver.config.event.impl;

import com.baiyi.opscloud.sshserver.listeners.SshShellEvent;
import com.baiyi.opscloud.sshserver.listeners.SshShellEventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/8/30 15:39
 * @Version 1.0
 */
@Slf4j
@Component
public class SshShellUnexpectedlyEvent extends BaseSshShellEvent {

    @Override
    public String getEventType() {
        return SshShellEventType.SESSION_STOPPED_UNEXPECTEDLY.name();
    }

    @Override
    public void handle(SshShellEvent event) {
        final String username = event.getSession().getServerSession().getUsername();
        log.warn(String.format("The user %s disconnects SSH-Server", username));
        closeTerminalSession(event);
    }

}
