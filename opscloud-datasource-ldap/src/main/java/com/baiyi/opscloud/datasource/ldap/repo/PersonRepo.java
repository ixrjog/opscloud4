package com.baiyi.opscloud.datasource.ldap.repo;

import com.baiyi.opscloud.common.datasource.LdapConfig;
import com.baiyi.opscloud.datasource.ldap.entity.LdapPerson;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2019/12/27 4:16 下午
 * @Version 1.0
 */
public interface PersonRepo {

    /**
     * 查询用户组成员名列表
     *
     * @param groupName
     * @return
     */
    List<LdapPerson.Person> queryGroupMember(LdapConfig.Ldap ldapConfig, String groupName);

    List<String> getAllPersonNames(LdapConfig.Ldap ldapConfig);

    List<LdapPerson.Person> getPersonList(LdapConfig.Ldap ldapConfig);

    LdapPerson.Person findPersonWithDn(LdapConfig.Ldap ldapConfig, String dn);

    void create(LdapConfig.Ldap ldapConfig, LdapPerson.Person person);

    void update(LdapConfig.Ldap ldapConfig, LdapPerson.Person person);

    void delete(LdapConfig.Ldap ldapConfig, String username);

    Boolean checkPersonInLdap(LdapConfig.Ldap ldapConfig, String username);

    List<String> searchUserGroupByUsername(LdapConfig.Ldap ldapConfig, String username);

}