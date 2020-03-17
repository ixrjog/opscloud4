package com.baiyi.opscloud.ldap;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.ldap.credential.PersonCredential;
import com.baiyi.opscloud.ldap.handler.LdapHandler;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/1/7 5:03 下午
 * @Version 1.0
 */
public class LdapTest extends BaseUnit {

    @Resource
    private LdapHandler ldapHandler;

    @Test
    void testLoginCheck() {
        com.baiyi.opscloud.ldap.credential.PersonCredential credential = PersonCredential.builder()
                .username("oc3-test")
                .password("+T8qKphhEz1")
                .build();
        System.err.println(ldapHandler.loginCheck(credential));
    }

}
