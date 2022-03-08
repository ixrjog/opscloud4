package com.baiyi.opscloud.datasource.ldap.repo.impl;

import com.baiyi.opscloud.common.datasource.LdapConfig;
import com.baiyi.opscloud.datasource.ldap.entity.LdapPerson;
import com.baiyi.opscloud.datasource.ldap.driver.LdapDriver;
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
    private LdapDriver ldapDrive;

    @Override
    public List<LdapPerson.Person> queryGroupMember(LdapConfig.Ldap ldapConfig, String groupName) {
        List<String> usernames = ldapDrive.queryGroupMember(ldapConfig, groupName);
        List<LdapPerson.Person> people = Lists.newArrayList();
        for (String username : usernames) {
            try {
                people.add(ldapDrive.getPersonWithDn(ldapConfig, ldapConfig.buildUserDn(username)));
            } catch (Exception e) {
                log.error("未找到 usernmae = {} 对应的 Person", username);
            }
        }
        return people;
    }

    /**
     * 查询部分字段集合
     *
     * @return
     */
    @Override
    public List<String> getAllPersonNames(LdapConfig.Ldap ldapConfig) {
        return ldapDrive.queryPersonNameList(ldapConfig);
    }

    /**
     * 查询对象映射集合
     *
     * @return
     */
    @Override
    public List<LdapPerson.Person> getPersonList(LdapConfig.Ldap ldapConfig) {
        return ldapDrive.queryPersonList(ldapConfig);
    }

    /**
     * 根据DN查询指定人员信息
     *
     * @param dn
     * @return
     */
    @Override
    public LdapPerson.Person findPersonWithDn(LdapConfig.Ldap ldapConfig, String dn) {
        return ldapDrive.getPersonWithDn(ldapConfig, dn);
    }

    @Override
    public void create(LdapConfig.Ldap ldapConfig, LdapPerson.Person person) {
        ldapDrive.bindPerson(ldapConfig, person);
    }

    @Override
    public void update(LdapConfig.Ldap ldapConfig, LdapPerson.Person person) {
        ldapDrive.updatePerson(ldapConfig, person);
    }

    @Override
    public void delete(LdapConfig.Ldap ldapConfig, String username) {
        ldapDrive.unbind(ldapConfig, ldapConfig.buildUserDn(username));
    }

    @Override
    public Boolean checkPersonInLdap(LdapConfig.Ldap ldapConfig, String username) {
        return ldapDrive.checkPersonInLdap(ldapConfig, username);

    }

    @Override
    public List<String> searchUserGroupByUsername(LdapConfig.Ldap ldapConfig, String username) {
        return ldapDrive.searchLdapGroup(ldapConfig, username);
    }

}
