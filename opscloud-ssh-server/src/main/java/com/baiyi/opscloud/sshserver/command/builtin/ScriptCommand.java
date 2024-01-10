package com.baiyi.opscloud.sshserver.command.builtin;

import com.baiyi.opscloud.sshserver.SshShellProperties;
import com.baiyi.opscloud.sshserver.command.SshShellComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.commands.Script;

/**
 * @Author baiyi
 * @Date 2023/4/4 15:59
 * @Version 1.0
 */
@Slf4j
@SshShellComponent
@ShellCommandGroup("Built-In Commands")
@ConditionalOnProperty(
        name = SshShellProperties.SSH_SHELL_PREFIX + ".commands." + ScriptCommand.GROUP + ".create",
        havingValue = "true", matchIfMissing = true
)
public class ScriptCommand implements Script.Command {

    public static final String GROUP = "script";

}