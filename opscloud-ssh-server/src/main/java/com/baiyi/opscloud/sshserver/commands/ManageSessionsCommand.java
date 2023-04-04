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

package com.baiyi.opscloud.sshserver.commands;

import com.baiyi.opscloud.sshserver.SimpleTable;
import com.baiyi.opscloud.sshserver.SshShellHelper;
import com.baiyi.opscloud.sshserver.SshShellProperties;
import com.baiyi.opscloud.sshserver.manage.SshShellSessionManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.session.ServerSession;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Lazy;
import org.springframework.shell.Availability;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.baiyi.opscloud.sshserver.manage.SshShellSessionManager.sessionUserName;

/**
 * Command to manage ssh sessions, not available by default
 */
@SshShellComponent
@ShellCommandGroup("Manage Sessions Commands")
@ConditionalOnProperty(
        name = SshShellProperties.SSH_SHELL_PREFIX + ".commands." + ManageSessionsCommand.GROUP + ".create",
        havingValue = "true", matchIfMissing = true
)
public class ManageSessionsCommand extends AbstractCommand {

    public static final String GROUP = "manage-sessions";
    private static final String COMMAND_MANAGE_SESSIONS_LIST = GROUP + "-list";
    private static final String COMMAND_MANAGE_SESSIONS_INFO = GROUP + "-info";
    private static final String COMMAND_MANAGE_SESSIONS_STOP = GROUP + "-stop";

    private final SshShellSessionManager sessionManager;

    public ManageSessionsCommand(SshShellHelper helper, SshShellProperties properties,
                                 @Lazy SshShellSessionManager sessionManager) {
        super(helper, properties, properties.getCommands().getManageSessions());
        this.sessionManager = sessionManager;
    }

    @ShellMethod(key = COMMAND_MANAGE_SESSIONS_LIST, value = "Displays active sessions")
    @ShellMethodAvailability("manageSessionsListAvailability")
    public String manageSessionsList() {
        Map<Long, ChannelSession> sessions = sessionManager.listSessions();

        SimpleTable.SimpleTableBuilder builder = SimpleTable.builder()
                .column("Session Id").column("Local address").column("Remote address").column("Authenticated User");

        for (ChannelSession value : sessions.values()) {
            builder.line(Arrays.asList(
                    value.getServerSession().getIoSession().getId(),
                    value.getServerSession().getIoSession().getLocalAddress(),
                    value.getServerSession().getIoSession().getRemoteAddress(),
                    sessionUserName(value)
            ));
        }
        return helper.renderTable(builder.build());
    }

    @ShellMethod(key = COMMAND_MANAGE_SESSIONS_INFO, value = "Displays session")
    @ShellMethodAvailability("manageSessionsInfoAvailability")
    public String manageSessionsInfo(@ShellOption(help = "Session identifier", valueProvider = SessionsValuesProvider.class) long sessionId) {
        ChannelSession session = sessionManager.getSession(sessionId);
        if (session == null) {
            return helper.getError("Session [" + sessionId + "] not found");
        }
        return helper.getSuccess(sessionTable(session.getServerSession()));
    }

    @ShellMethod(key = COMMAND_MANAGE_SESSIONS_STOP, value = "Stop session")
    @ShellMethodAvailability("manageSessionsStopAvailability")
    public String manageSessionsStop(@ShellOption(help = "Session identifier", valueProvider = SessionsValuesProvider.class) long sessionId) {
        return sessionManager.stopSession(sessionId) ?
                helper.getSuccess("Session [" + sessionId + "] stopped") :
                helper.getWarning("Unable to stop session [" + sessionId + "], maybe it does not exist");
    }

    private String sessionTable(ServerSession session) {
        return helper.renderTable(SimpleTable.builder()
                .column("Property").column("Value")
                .line(Arrays.asList("Session id", session.getIoSession().getId()))
                .line(Arrays.asList("Local address", session.getIoSession().getLocalAddress()))
                .line(Arrays.asList("Remote address", session.getIoSession().getRemoteAddress()))
                .line(Arrays.asList("Server version", session.getServerVersion()))
                .line(Arrays.asList("Client version", session.getClientVersion()))
                .build());
    }

    private Availability manageSessionsListAvailability() {
        return availability(GROUP, COMMAND_MANAGE_SESSIONS_LIST);
    }

    private Availability manageSessionsInfoAvailability() {
        return availability(GROUP, COMMAND_MANAGE_SESSIONS_INFO);
    }

    private Availability manageSessionsStopAvailability() {
        return availability(GROUP, COMMAND_MANAGE_SESSIONS_STOP);
    }
}

@Slf4j
@Component
@AllArgsConstructor
class SessionsValuesProvider
        implements ValueProvider {

    @Lazy
    private final SshShellSessionManager sessionManager;

    @Override
    public List<CompletionProposal> complete(CompletionContext completionContext) {
        return sessionManager.listSessions().keySet().stream().map(id -> new CompletionProposal(id.toString())).collect(Collectors.toList());
    }
}
