package com.baiyi.opscloud.sshserver.auth;

import org.apache.sshd.server.auth.pubkey.PublickeyAuthenticator;

/**
 * @Author 修远
 * @Date 2021/6/10 2:20 下午
 * @Since 1.0
 */

@FunctionalInterface
public interface PublickeyAuthenticatorProvider extends PublickeyAuthenticator {

}
