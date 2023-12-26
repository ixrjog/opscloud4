package com.baiyi.opscloud.sshserver.auth;

import org.apache.sshd.server.auth.pubkey.PublickeyAuthenticator;


@FunctionalInterface
public interface PublickeyAuthenticatorProvider extends PublickeyAuthenticator {

}