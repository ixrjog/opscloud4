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

package com.baiyi.opscloud.sshserver.auth;

import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.server.auth.password.PasswordChangeRequiredException;
import org.apache.sshd.server.session.ServerSession;

import java.util.UUID;

/**
 * Password implementation
 */
@Slf4j
public class SshShellPasswordAuthenticationProvider
        implements SshShellAuthenticationProvider {

    private final String user;

    private final String password;

    public SshShellPasswordAuthenticationProvider(String user, String password) {
        this.user = user;
        String pass = password;
        if (pass == null) {
            pass = UUID.randomUUID().toString();
            log.info(" --- Generating password for ssh connection: {}", pass);
        }
        this.password = pass;
    }

    @Override
    public boolean authenticate(String username, String pass,
                                ServerSession serverSession) throws PasswordChangeRequiredException {

        serverSession.getIoSession().setAttribute(AUTHENTICATION_ATTRIBUTE, new SshAuthentication(username, username));

        return username.equals(this.user) && pass.equals(this.password);
    }

}