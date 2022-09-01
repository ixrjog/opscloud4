package com.baiyi.opscloud.sshserver.listeners.event.impl;

import com.baiyi.opscloud.sshcore.facade.SimpleTerminalSessionFacade;
import com.baiyi.opscloud.sshserver.SshShellHelper;
import com.baiyi.opscloud.sshserver.listeners.SshShellEvent;
import com.baiyi.opscloud.sshserver.listeners.SshShellEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/8/30 15:37
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SshShellStoppedEvent extends AbstractSshShellEvent {

    private final SshShellHelper sshShellHelper;

    private final SimpleTerminalSessionFacade simpleTerminalSessionFacade;

    @Override
    public String getEventType() {
        return SshShellEventType.SESSION_STOPPED.name();
    }

    @Override
    public void handle(SshShellEvent event) {
        final String username = event.getSession().getServerSession().getUsername();
        log.info(String.format("The user %s exits SSH-Server normally", username));
        closeTerminalSession(event);
    }

}
