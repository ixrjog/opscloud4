package com.baiyi.opscloud.datasource.ldap.repo.impl;

import com.baiyi.opscloud.common.datasource.LdapConfig;
import com.baiyi.opscloud.datasource.ldap.entry.Person;
import com.baiyi.opscloud.datasource.ldap.handler.LdapHandler;
import com.baiyi.opscloud.datasource.ldap.repo.PersonRepo;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;


/**
 * @Author baiyi
 * @Date 2019/12/27 5:37 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class PersonRepoImpl implements PersonRepo {

    @Resource
    private LdapHandler ldapHandler;

    @Override
    public List<Person> queryGroupMember(LdapConfig.Ldap ldapConfig, String groupName) {
        List<String> usernames = ldapHandler.queryGroupMember(ldapConfig, groupName);
        List<Person> people = Lists.newArrayList();
        for (String username : usernames) {
            try {
                people.add(ldapHandler.getPersonWithDn(ldapConfig, ldapConfig.buildUserDn(username)));
            } catch (Exception e) {
                log.error("未找到 usernmae = {} 对应的 Person", username);
            }
        }
//
//        return usernames.stream().map(e->
//                ldapHandler.getPersonWithDn(ldapConfig,ldapConfig.buildUserDn(e))
//        ).collect(Collectors.toList());
        return people;
    }

    /**
     * 查询部分字段集合
     *
     * @return
     */
    @Override
    public List<String> getAllPersonNames(LdapConfig.Ldap ldapConfig) {
        return ldapHandler.queryPersonNameList(ldapConfig);
    }

    /**
     * 查询对象映射集合
     *
     * @return
     */
    @Override
    public List<Person> getPersonList(LdapConfig.Ldap ldapConfig) {
        return ldapHandler.queryPersonList(ldapConfig);
    }

    /**
     * 根据DN查询指定人员信息
     *
     * @param dn
     * @return
     */
    @Override
    public Person findPersonWithDn(LdapConfig.Ldap ldapConfig, String dn) {
        return ldapHandler.getPersonWithDn(ldapConfig, dn);
    }

    @Override
    public void create(LdapConfig.Ldap ldapConfig, Person person) {
        ldapHandler.bindPerson(ldapConfig, person);
    }

    @Override
    public void update(LdapConfig.Ldap ldapConfig, Person person) {
        ldapHandler.updatePerson(ldapConfig, person);
    }

    @Override
    public void delete(LdapConfig.Ldap ldapConfig, String username) {
        ldapHandler.unbind(ldapConfig, ldapConfig.buildUserDn(username));
    }

    @Override
    public Boolean checkPersonInLdap(LdapConfig.Ldap ldapConfig, String username) {
        return ldapHandler.checkPersonInLdap(ldapConfig, username);

    }

    @Override
    public List<String> searchUserGroupByUsername(LdapConfig.Ldap ldapConfig, String username) {
        return ldapHandler.searchLdapGroup(ldapConfig, username);
    }


}
