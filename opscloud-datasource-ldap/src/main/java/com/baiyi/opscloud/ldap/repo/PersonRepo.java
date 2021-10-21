package com.baiyi.opscloud.ldap.repo;

import com.baiyi.opscloud.common.datasource.LdapDsInstanceConfig;
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
    List<Person> queryGroupMember(LdapDsInstanceConfig.Ldap ldapConfig, String groupName);

    List<String> getAllPersonNames(LdapDsInstanceConfig.Ldap ldapConfig);

    List<Person> getPersonList(LdapDsInstanceConfig.Ldap ldapConfig);

    Person findPersonWithDn(LdapDsInstanceConfig.Ldap ldapConfig, String dn);

    void create(LdapDsInstanceConfig.Ldap ldapConfig, Person person);

    void update(LdapDsInstanceConfig.Ldap ldapConfig, Person person);

    void delete(LdapDsInstanceConfig.Ldap ldapConfig, String username);

    Boolean checkPersonInLdap(LdapDsInstanceConfig.Ldap ldapConfig, String username);

    List<String> searchUserGroupByUsername(LdapDsInstanceConfig.Ldap ldapConfig, String username);

}
