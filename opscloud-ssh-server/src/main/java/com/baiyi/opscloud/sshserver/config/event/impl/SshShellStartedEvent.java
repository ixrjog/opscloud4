package com.baiyi.opscloud.sshserver.config.event.impl;

import com.baiyi.opscloud.sshserver.PromptColor;
import com.baiyi.opscloud.sshserver.SshShellHelper;
import com.baiyi.opscloud.sshserver.listeners.SshShellEvent;
import com.baiyi.opscloud.sshserver.listeners.SshShellEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/8/30 15:27
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SshShellStartedEvent extends BaseSshShellEvent {

    private final SshShellHelper sshShellHelper;

    private static final String WELCOME = "Hi %s, Welcome to Opscloud SSH-Server! \n";

    @Override
    public String getEventType() {
        return SshShellEventType.SESSION_STARTED.name();
    }

    @Override
    public void handle(SshShellEvent event) {
        // this.terminal.puts(InfoCmp.Capability.clear_screen, new Object[0]);  // 清屏
        openTerminalSession(event);
        final String username = event.getSession().getServerSession().getUsername();
        String welcome = String.format(WELCOME, username);
        sshShellHelper.print(welcome, PromptColor.RED);
    }

}
