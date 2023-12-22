package com.baiyi.opscloud.sshserver.listeners.event.impl;

import com.baiyi.opscloud.common.util.StringFormatter;
import com.baiyi.opscloud.sshserver.PromptColor;
import com.baiyi.opscloud.sshserver.SshShellHelper;
import com.baiyi.opscloud.sshserver.listeners.SshShellEvent;
import com.baiyi.opscloud.sshserver.listeners.SshShellEventType;
import com.baiyi.opscloud.sshserver.listeners.event.AbstractSshShellEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/8/30 15:27
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SshShellStartedEvent extends AbstractSshShellEvent {

    private final SshShellHelper sshShellHelper;

    @Value("${ssh.shell.version}")
    private String version;

    private static final String WELCOME = "Dear {}, Welcome to SSH-Server<{}>@{} \n";

    @Override
    public String getEventType() {
        return SshShellEventType.SESSION_STARTED.name();
    }

    @Override
    public void handle(SshShellEvent event) {
        openTerminalSession(event);
        final String username = event.getSession().getServerSession().getUsername();
        final String sshServerInstance = sshShellHelper.getColored(AbstractSshShellEvent.HOST_INFO.getHostname(), PromptColor.BLUE);
        String welcome = StringFormatter.arrayFormat(WELCOME, username, version, sshServerInstance);
        sshShellHelper.print(welcome, PromptColor.CYAN);
    }

    private void preHandle() {
        // this.terminal.puts(InfoCmp.Capability.clear_screen, new Object[0]);  // 清屏
    }

}