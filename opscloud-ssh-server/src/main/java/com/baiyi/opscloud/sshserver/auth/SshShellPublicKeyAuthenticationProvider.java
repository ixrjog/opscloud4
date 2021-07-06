package com.baiyi.opscloud.sshserver.auth;

import org.apache.sshd.server.config.keys.AuthorizedKeysAuthenticator;
import org.apache.sshd.server.session.ServerSession;

import java.io.File;
import java.security.PublicKey;

import static com.baiyi.opscloud.sshserver.auth.SshShellAuthenticationProvider.AUTHENTICATION_ATTRIBUTE;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/10 11:10 上午
 * @Since 1.0
 */

public class SshShellPublicKeyAuthenticationProvider extends AuthorizedKeysAuthenticator {

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
        session.getIoSession().setAttribute(AUTHENTICATION_ATTRIBUTE, new SshAuthentication(username, username));
        return authenticated;
    }
}