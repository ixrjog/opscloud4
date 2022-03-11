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

package com.baiyi.opscloud.sshserver.command;

import com.baiyi.opscloud.sshserver.SshShellCommandFactory;
import com.baiyi.opscloud.sshserver.SshShellHelper;
import com.baiyi.opscloud.sshserver.SshShellProperties;
import com.baiyi.opscloud.sshserver.auth.SshAuthentication;
import com.baiyi.opscloud.sshserver.command.properties.CommandProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.Availability;

import javax.annotation.Resource;
import java.util.List;

/**
 * Abstract command with availability
 */
@Slf4j
public class AbstractCommand {

    @Resource
    private SshShellHelper sshShellHelper;

    @Resource
    private SshShellProperties sshShellProperties;

    protected final CommandProperties commandProperties;

    public AbstractCommand(SshShellHelper helper, SshShellProperties properties, CommandProperties commandProperties) {
        this.sshShellHelper = helper;
        this.sshShellProperties = properties;
        this.commandProperties = commandProperties;
    }

    protected Availability availability(String commandGroup, String commandName) {
        try {
            preAvailability();
            if (!commandProperties.isEnable()) {
                return Availability.unavailable("command deactivated (please check property '" +
                        SshShellProperties.SSH_SHELL_PREFIX + ".commands." + commandGroup + ".enable" + "')");
            }
            if (commandProperties.getExcludes() != null && commandProperties.getExcludes().contains(commandName)) {
                return Availability.unavailable("command is excluded (please check property '" +
                        SshShellProperties.SSH_SHELL_PREFIX + ".commands." + commandGroup + ".excludes" + "')");
            }
            if (commandProperties.getIncludes() != null && !commandProperties.getIncludes().contains(commandName)) {
                return Availability.unavailable("command not included (please check property '" +
                        SshShellProperties.SSH_SHELL_PREFIX + ".commands." + commandGroup + ".includes" + "')");
            }
            if (sshShellHelper.isLocalPrompt()) {
                log.debug("Not an ssh session -> local prompt -> giving all rights");
                return Availability.available();
            }
            SshAuthentication auth = SshShellCommandFactory.SSH_THREAD_CONTEXT.get().getAuthentication();
            List<String> authorities = auth != null ? auth.getAuthorities() : null;
            if (commandProperties.isRestricted() && !sshShellHelper.checkAuthorities(commandProperties.getAuthorizedRoles(),
                    authorities, sshShellProperties.getAuthentication() == SshShellProperties.AuthenticationType.simple)) {
                return Availability.unavailable("command is forbidden for current user");
            }
            postAvailability();
            return Availability.available();
        } catch (AvailabilityException e) {
            return Availability.unavailable(e.getMessage());
        }
    }

    /**
     * Extends this to add behavior before the one in abstract
     *
     * @throws AvailabilityException if unavailable
     */
    protected void preAvailability() throws AvailabilityException {
        // nothing by default
    }


    /**
     * Extends this to add behavior after the one in abstract
     *
     * @throws AvailabilityException if unavailable
     */
    protected void postAvailability() throws AvailabilityException {
        // nothing by default
    }
}
