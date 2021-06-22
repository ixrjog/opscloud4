package com.baiyi.caesar.ldap.repo;

import com.baiyi.caesar.common.datasource.config.DsLdapConfig;
import com.baiyi.caesar.ldap.entry.Group;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/24 4:33 下午
 * @Version 1.0
 */
public interface GroupRepo {

    List<Group> getGroupList(DsLdapConfig.Ldap ldapConfig);

    /**
     * 查询用户组成员名列表
     *
     * @param groupName
     * @return
     */
    List<String> queryGroupMember(DsLdapConfig.Ldap ldapConfig, String groupName);

    List<Group> searchGroupByUsername(DsLdapConfig.Ldap ldapConfig, String username);

    /**
     * 移除group中的用户
     *
     * @param groupName
     * @param username
     * @return
     */
    void removeGroupMember(DsLdapConfig.Ldap ldapConfig, String groupName, String username);

    void addGroupMember(DsLdapConfig.Ldap ldapConfig, String groupName, String username);

    void create(DsLdapConfig.Ldap ldapConfig, String groupName);

    void delete(DsLdapConfig.Ldap ldapConfig, String groupName);
}
