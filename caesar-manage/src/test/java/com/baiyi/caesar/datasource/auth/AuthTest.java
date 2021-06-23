package com.baiyi.caesar.datasource.auth;

import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.ldap.provider.LdapAuthProvider;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/6/23 11:34 上午
 * @Version 1.0
 */
@Slf4j
public class AuthTest extends BaseUnit {

    @Resource
    private LdapAuthProvider ldapAuthProvider;

    @Test
    void authTest() {
//        Authorization.Credential credential = Authorization.Credential.builder()
//                .username("baiyi")
//                .password("1111")
//                .build();
//        boolean pass = ldapAuthProvider.login(credential);
//        log.info("pass = {}", pass);
    }
}
