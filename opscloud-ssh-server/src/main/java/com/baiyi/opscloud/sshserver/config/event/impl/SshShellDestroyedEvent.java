package com.baiyi.opscloud.sshserver.config.event.impl;

import com.baiyi.opscloud.sshserver.listeners.SshShellEvent;
import com.baiyi.opscloud.sshserver.listeners.SshShellEventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/9/1 17:09
 * @Version 1.0
 */
@Slf4j
@Component
public class SshShellDestroyedEvent extends BaseSshShellEvent {

    @Override
    public String getEventType() {
        return SshShellEventType.SESSION_DESTROYED.name();
    }

    @Override
    public void handle(SshShellEvent event) {
        final String username = event.getSession().getServerSession().getUsername();
        closeTerminalSession(event);
    }

}
