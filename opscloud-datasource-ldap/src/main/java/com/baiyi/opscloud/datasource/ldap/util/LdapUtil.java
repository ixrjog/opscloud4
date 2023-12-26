package com.baiyi.opscloud.datasource.ldap.util;

import com.baiyi.opscloud.common.datasource.LdapConfig;
import com.baiyi.opscloud.datasource.ldap.entity.LdapGroup;
import com.baiyi.opscloud.datasource.ldap.entity.LdapPerson;
import com.google.common.base.Joiner;

/**
 * @Author baiyi
 * @Date 2022/9/20 10:51
 * @Version 1.0
 */
public class LdapUtil {

    private LdapUtil() {
    }

    public static String toUserRDN(LdapConfig.Ldap ldapConfig, LdapPerson.Person person) {
        return Joiner.on("=").join(ldapConfig.getUser().getId(), person.getUsername());
    }

    public static String toGroupRDN(LdapConfig.Ldap ldapConfig, LdapGroup.Group group) {
        return Joiner.on("=").join(ldapConfig.getUser().getId(), group.getGroupName());
    }

    public static String toUserDN(LdapConfig.Ldap ldapConfig, LdapPerson.Person person) {
        String rdn = toUserRDN(ldapConfig, person);
        return Joiner.on(",").join(rdn, ldapConfig.getUser().getDn());
    }

    public static String toGroupRDN(LdapConfig.Ldap ldapConfig, String groupName) {
        return Joiner.on("=").join(ldapConfig.getGroup().getId(), groupName);
    }

    public static String toGroupDN(LdapConfig.Ldap ldapConfig, String groupName) {
        String rdn = toGroupRDN(ldapConfig, groupName);
        return Joiner.on(",").join(rdn, ldapConfig.getGroup().getDn());
    }

}