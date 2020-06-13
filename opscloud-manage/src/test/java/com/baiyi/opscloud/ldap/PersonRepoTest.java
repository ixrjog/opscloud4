package com.baiyi.opscloud.ldap;


import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.ldap.credential.PersonCredential;
import com.baiyi.opscloud.ldap.entry.Person;
import com.baiyi.opscloud.ldap.handler.LdapHandler;
import com.baiyi.opscloud.ldap.repo.PersonRepo;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2019/12/27 3:12 下午
 * @Version 1.0
 */
public class PersonRepoTest extends BaseUnit {

    @Resource
    private LdapHandler ldapHandler;

    @Resource
    private PersonRepo personRepo;

    @Test
    void testQueryPersonList() {
        List<Person> personList = ldapHandler.queryPersonList();
        for (Person person : personList) {
            System.err.println(person);
        }
    }

    @Test
    void testGetPersonList() {
        List<Person> personList = personRepo.getPersonList();
        for (Person person : personList) {
            System.err.println(person);
        }
    }

    @Test
    void testLoginCheck() {
        PersonCredential credential = PersonCredential.builder()
                .username("baiyi")
                .password("password")
                .build();
        boolean checkAuth = ldapHandler.loginCheck(credential);
        System.err.println(checkAuth);
    }

    @Test
    void testCheckPersonInLdap() {
        boolean check = personRepo.checkPersonInLdap("baiyi");
        System.err.println(check);
    }

    @Test
    void testSearchUserGroupByUsername() {
        List<String> list = personRepo.searchUserGroupByUsername("baiyi");
        for (String group : list) {
            System.err.println(group);
        }
    }

}
