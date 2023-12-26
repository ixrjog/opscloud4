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

import com.baiyi.opscloud.sshserver.SshShellHelper;
import com.baiyi.opscloud.sshserver.SshShellProperties;
import com.baiyi.opscloud.sshserver.command.AbstractCommand;
import com.baiyi.opscloud.sshserver.command.SshShellComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.commands.History;

/**
 * Override history command to get history per user if not shared
 */
@Slf4j
@SshShellComponent
@ShellCommandGroup("Built-In Commands")
@ConditionalOnProperty(
        name = SshShellProperties.SSH_SHELL_PREFIX + ".commands." + HistoryCommand.GROUP + ".create",
        havingValue = "true", matchIfMissing = true
)
public class HistoryCommand extends AbstractCommand implements History.Command {

    public static final String GROUP = "history";
    public static final String COMMAND_HISTORY = GROUP;

    public HistoryCommand(SshShellProperties properties, SshShellHelper helper) {
        super(helper, properties, properties.getCommands().getHistory());
    }

    private Availability historyAvailability() {
        return availability(GROUP, COMMAND_HISTORY);
    }

}