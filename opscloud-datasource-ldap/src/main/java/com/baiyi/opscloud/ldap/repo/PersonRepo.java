package com.baiyi.opscloud.ldap.repo;

import com.baiyi.opscloud.common.datasource.config.DsLdapConfig;
import com.baiyi.opscloud.ldap.entry.Person;

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
    List<Person> queryGroupMember(DsLdapConfig.Ldap ldapConfig, String groupName);

    List<String> getAllPersonNames(DsLdapConfig.Ldap ldapConfig);

    List<Person> getPersonList(DsLdapConfig.Ldap ldapConfig);

    Person findPersonWithDn(DsLdapConfig.Ldap ldapConfig, String dn);

    void create(DsLdapConfig.Ldap ldapConfig, Person person);

    void update(DsLdapConfig.Ldap ldapConfig, Person person);

    void delete(DsLdapConfig.Ldap ldapConfig, String username);

    Boolean checkPersonInLdap(DsLdapConfig.Ldap ldapConfig, String username);

    List<String> searchUserGroupByUsername(DsLdapConfig.Ldap ldapConfig, String username);

}
