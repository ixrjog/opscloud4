package com.baiyi.opscloud.sshserver.command.builtin;

import com.baiyi.opscloud.common.util.StringFormatter;
import com.baiyi.opscloud.sshserver.SshShellHelper;
import com.baiyi.opscloud.sshserver.SshShellProperties;
import com.baiyi.opscloud.sshserver.command.AbstractCommand;
import com.baiyi.opscloud.sshserver.command.SshShellComponent;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringBootVersion;
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

    private static final String VERSION = "Spring Boot<{}>  Spring Shell<3.2.0>";

//    @Value("${spring.application.spring-shell-starter.version}")
//    private String shellVersion;

    @Resource
    private SshShellHelper sshShellHelper;

    public VersionCommand(SshShellProperties properties, SshShellHelper helper) {
        super(helper, properties, properties.getCommands().getHistory());
    }

    @SuppressWarnings("SpringShellCommandInspection")
    @ShellMethod(key = COMMAND_VERSION, value = "Show version info")
    public void version() {
        sshShellHelper.print(StringFormatter.arrayFormat(VERSION, SpringBootVersion.getVersion()));
    }

}