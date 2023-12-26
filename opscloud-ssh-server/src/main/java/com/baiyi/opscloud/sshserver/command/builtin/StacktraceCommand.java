/*
 * Copyright (c) 2020 François Onimus
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.baiyi.opscloud.sshserver.command.builtin;

import com.baiyi.opscloud.sshserver.command.AbstractCommand;
import com.baiyi.opscloud.sshserver.command.SshShellComponent;
import com.baiyi.opscloud.sshserver.postprocess.ExtendedResultHandlerService;
import com.baiyi.opscloud.sshserver.SshShellHelper;
import com.baiyi.opscloud.sshserver.SshShellProperties;
import org.jline.terminal.Terminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Lazy;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.commands.Stacktrace;

/**
 * Override stacktrace command to get error per thread
 */
@SshShellComponent
@ShellCommandGroup("Built-In Commands")
@ConditionalOnProperty(
        name = SshShellProperties.SSH_SHELL_PREFIX + ".commands." + StacktraceCommand.GROUP + ".create",
        havingValue = "true", matchIfMissing = true
)
@SuppressWarnings("SpringShellCommandInspection")
public class StacktraceCommand extends AbstractCommand implements Stacktrace.Command {

    public static final String GROUP = "stacktrace";
    public static final String COMMAND_STACKTRACE = GROUP;

    private Terminal terminal;

    public StacktraceCommand(SshShellHelper helper, SshShellProperties properties) {
        super(helper, properties, properties.getCommands().getStacktrace());
    }

    @ShellMethod(key = COMMAND_STACKTRACE, value = "Display the full stacktrace of the last error.")
    @ShellMethodAvailability("stacktraceAvailability")
    public void stacktrace() {
        Throwable lastError = ExtendedResultHandlerService.THREAD_CONTEXT.get();
        if (lastError != null) {
            lastError.printStackTrace(this.terminal.writer());
        }
    }

    @Autowired
    @Lazy
    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }

    private Availability stacktraceAvailability() {
        return availability(GROUP, COMMAND_STACKTRACE);
    }

}