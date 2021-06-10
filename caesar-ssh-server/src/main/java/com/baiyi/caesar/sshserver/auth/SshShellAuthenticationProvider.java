package com.baiyi.caesar.sshserver.auth;

import org.apache.sshd.server.auth.password.PasswordAuthenticator;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/10 11:09 上午
 * @Since 1.0
 */

@FunctionalInterface
public interface SshShellAuthenticationProvider extends PasswordAuthenticator {

    String AUTHENTICATION_ATTRIBUTE = "authentication";

}