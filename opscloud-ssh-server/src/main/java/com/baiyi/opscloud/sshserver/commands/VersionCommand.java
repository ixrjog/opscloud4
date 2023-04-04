package com.baiyi.opscloud.sshserver.commands;

import com.baiyi.opscloud.sshserver.SshShellHelper;
import com.baiyi.opscloud.sshserver.SshShellProperties;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.commands.Version;

/**
 * @Author baiyi
 * @Date 2023/4/4 15:48
 * @Version 1.0
 */
@Slf4j
@SshShellComponent
@ShellCommandGroup("Built-In Commands")
@ConditionalOnProperty(
        name = SshShellProperties.SSH_SHELL_PREFIX + ".commands." + VersionCommand.GROUP + ".create",
        havingValue = "true", matchIfMissing = true
)
public class VersionCommand extends AbstractCommand implements Version.Command {

    public static final String GROUP = "version";
    public static final String COMMAND_VERSION = GROUP;

    @Resource
    private SshShellHelper sshShellHelper;

    public VersionCommand(SshShellProperties properties, SshShellHelper helper) {
        super(helper, properties, properties.getCommands().getHistory());
    }

    @SuppressWarnings("SpringShellCommandInspection")
    @ShellMethod(key = COMMAND_VERSION, value = "Show version info")
    public void version() {
        sshShellHelper.print("Spring Boot 3.0.5, Spring Shell 3.0.1");
    }

}
