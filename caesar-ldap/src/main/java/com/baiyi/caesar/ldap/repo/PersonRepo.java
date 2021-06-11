package com.baiyi.caesar.ldap.repo;

import com.baiyi.caesar.common.datasource.config.LdapDsConfig;
import com.baiyi.caesar.ldap.entry.Person;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2019/12/27 4:16 下午
 * @Version 1.0
 */
public interface PersonRepo {

    List<String> getAllPersonNames(LdapDsConfig.Ldap ldapConfig);

    List<Person> getPersonList(LdapDsConfig.Ldap ldapConfig);

    Person findPersonWithDn(LdapDsConfig.Ldap ldapConfig,String dn);

    void create(LdapDsConfig.Ldap ldapConfig,Person person);

    void update(LdapDsConfig.Ldap ldapConfig,Person person);

    void delete(LdapDsConfig.Ldap ldapConfig,String username);

    Boolean checkPersonInLdap(LdapDsConfig.Ldap ldapConfig,String username);

    List<String> searchUserGroupByUsername(LdapDsConfig.Ldap ldapConfig,String username);

}
