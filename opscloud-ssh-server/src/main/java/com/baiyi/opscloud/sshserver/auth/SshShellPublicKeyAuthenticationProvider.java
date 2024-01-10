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
import org.apache.sshd.server.config.keys.AuthorizedKeysAuthenticator;
import org.apache.sshd.server.session.ServerSession;

import java.io.File;
import java.security.PublicKey;

/**
 * Authorized keys authenticator extension to set authentication attribute
 */
@Slf4j
public class SshShellPublicKeyAuthenticationProvider
        extends AuthorizedKeysAuthenticator {

    /**
     * Default constructor
     *
     * @param publicKeysFile public keys file
     */
    public SshShellPublicKeyAuthenticationProvider(File publicKeysFile) {
        super(publicKeysFile.toPath());
    }

    @Override
    public boolean authenticate(String username, PublicKey key, ServerSession session) {
        boolean authenticated = super.authenticate(username, key, session);
        session.getIoSession().setAttribute(SshShellAuthenticationProvider.AUTHENTICATION_ATTRIBUTE, new SshAuthentication(username, username));
        return authenticated;
    }

}