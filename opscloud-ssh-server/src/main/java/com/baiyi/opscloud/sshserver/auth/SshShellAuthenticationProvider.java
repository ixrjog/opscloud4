package com.baiyi.opscloud.sshserver.auth;

import org.apache.sshd.server.auth.password.PasswordAuthenticator;

/**
 * @Author 修远
 * @Date 2021/6/10 11:09 上午
 * @Since 1.0
 */

@FunctionalInterface
public interface SshShellAuthenticationProvider extends PasswordAuthenticator {

    String AUTHENTICATION_ATTRIBUTE = "authentication";

}