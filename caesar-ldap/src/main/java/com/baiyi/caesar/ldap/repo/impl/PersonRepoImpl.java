package com.baiyi.caesar.ldap.repo.impl;

import com.baiyi.caesar.common.datasource.config.LdapDsConfig;
import com.baiyi.caesar.ldap.entry.Person;
import com.baiyi.caesar.ldap.handler.LdapHandler;
import com.baiyi.caesar.ldap.repo.PersonRepo;
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

    /**
     * 查询部分字段集合
     *
     * @return
     */
    @Override
    public List<String> getAllPersonNames(LdapDsConfig.Ldap ldapConfig) {
        return ldapHandler.queryPersonNameList(ldapConfig);
    }

    /**
     * 查询对象映射集合
     *
     * @return
     */
    @Override
    public List<Person> getPersonList(LdapDsConfig.Ldap ldapConfig) {
        return ldapHandler.queryPersonList(ldapConfig);
    }

    /**
     * 根据DN查询指定人员信息
     *
     * @param dn
     * @return
     */
    @Override
    public Person findPersonWithDn(LdapDsConfig.Ldap ldapConfig, String dn) {
        return ldapHandler.getPersonWithDn(ldapConfig, dn);
    }

    @Override
    public void create(LdapDsConfig.Ldap ldapConfig, Person person) {
        ldapHandler.bindPerson(ldapConfig, person);
    }

    @Override
    public void update(LdapDsConfig.Ldap ldapConfig, Person person) {
        ldapHandler.updatePerson(ldapConfig, person);
    }

    @Override
    public void delete(LdapDsConfig.Ldap ldapConfig, String username) {
        //  ldapHandler.unbind(ldapConfig.buildUserDN(username));
    }

    @Override
    public Boolean checkPersonInLdap(LdapDsConfig.Ldap ldapConfig, String username) {
        return ldapHandler.checkPersonInLdap(ldapConfig, username);

    }

    @Override
    public List<String> searchUserGroupByUsername(LdapDsConfig.Ldap ldapConfig, String username) {
        return ldapHandler.searchLdapGroup(ldapConfig, username);
    }


}
