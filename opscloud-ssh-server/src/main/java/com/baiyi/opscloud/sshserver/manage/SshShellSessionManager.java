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

package com.baiyi.opscloud.sshserver.manage;

import com.baiyi.opscloud.sshserver.SshShellCommandFactory;
import com.baiyi.opscloud.sshserver.auth.SshAuthentication;
import org.apache.sshd.server.channel.ChannelSession;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Session manager
 */
@Component
public class SshShellSessionManager {

    private final SshShellCommandFactory commandFactory;

    /**
     * Ssh shell session manager
     *
     * @param commandFactory ssh shell command factory
     */
    public SshShellSessionManager(SshShellCommandFactory commandFactory) {
        this.commandFactory = commandFactory;
    }

    /**
     * List active sessions
     *
     * @return active sessions
     */
    public Map<Long, ChannelSession> listSessions() {
        return this.commandFactory.listSessions();
    }

    /**
     * @param id session id
     * @return found session, or null if not existing
     */
    public ChannelSession getSession(long id) {
        return listSessions().get(id);
    }

    /**
     * Stop active session
     *
     * @param id session id
     * @return true if session found and stopped, false otherwise
     */
    public boolean stopSession(long id) {
        ChannelSession session = getSession(id);
        if (session != null) {
            this.commandFactory.destroy(session);
            return true;
        }
        return false;
    }

    /**
     * Search for authenticated user in session
     *
     * @param session ssh session
     * @return user name
     */
    public static String sessionUserName(ChannelSession session) {
        SshAuthentication authentication =
                (SshAuthentication) session.getServerSession().getIoSession().getAttribute("authentication");
        if (authentication == null) {
            return null;
        }
        return authentication.getName();
    }

}