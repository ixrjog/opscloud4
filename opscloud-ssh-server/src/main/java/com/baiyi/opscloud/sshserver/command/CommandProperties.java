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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.baiyi.opscloud.sshserver.SshShellProperties.ADMIN_ROLE;

/**
 * Command specific properties
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommandProperties {

    /**
     * Create command at startup
     */
    private boolean create = true;

    /**
     * Enable command at startup
     */
    private boolean enable = true;

    /**
     * Is command restricted
     */
    private boolean restricted = true;

    private List<String> authorizedRoles = new ArrayList<>(Collections.singletonList(ADMIN_ROLE));

    /**
     * Possibility to include some sub commands only
     */
    private List<String> includes;

    /**
     * Possibility to exclude some sub commands only
     */
    private List<String> excludes;

    /**
     * Create properties for disabled command
     *
     * @return disabled command
     */
    public static CommandProperties disabledByDefault() {
        CommandProperties properties = new CommandProperties();
        properties.setEnable(false);
        return properties;
    }

    /**
     * Create properties for command with authorized roles
     *
     * @return command with authorized roles
     */
    public static CommandProperties withAuthorizedRoles(List<String> authorizedRoles) {
        CommandProperties properties = new CommandProperties();
        properties.setAuthorizedRoles(authorizedRoles);
        return properties;
    }

    /**
     * Create properties for not restructed command
     *
     * @return not restructed command
     */
    public static CommandProperties notRestrictedByDefault() {
        CommandProperties properties = new CommandProperties();
        properties.setRestricted(false);
        properties.setAuthorizedRoles(null);
        return properties;
    }

    /**
     * Create properties for excluded by default
     *
     * @return excluded by default
     */
    public static CommandProperties withExcludedByDefault(List<String> excludes) {
        CommandProperties properties = new CommandProperties();
        properties.setExcludes(excludes);
        return properties;
    }

}