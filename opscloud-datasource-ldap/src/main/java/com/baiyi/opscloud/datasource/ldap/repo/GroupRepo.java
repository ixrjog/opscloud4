package com.baiyi.opscloud.datasource.ldap.repo;

import com.baiyi.opscloud.common.datasource.LdapConfig;
import com.baiyi.opscloud.datasource.ldap.entity.LdapGroup;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/24 4:33 下午
 * @Version 1.0
 */
public interface GroupRepo {

    List<LdapGroup.Group> getGroupList(LdapConfig.Ldap ldapConfig);

    /**
     * 查询用户组成员名列表
     *
     * @param groupName
     * @return
     */
    List<String> queryGroupMember(LdapConfig.Ldap ldapConfig, String groupName);

    List<LdapGroup.Group> searchGroupByUsername(LdapConfig.Ldap ldapConfig, String username);

    /**
     * 移除group中的用户
     *
     * @param groupName
     * @param username
     * @return
     */
    void removeGroupMember(LdapConfig.Ldap ldapConfig, String groupName, String username);

    void addGroupMember(LdapConfig.Ldap ldapConfig, String groupName, String username);

    void create(LdapConfig.Ldap ldapConfig, String groupName);

    void delete(LdapConfig.Ldap ldapConfig, String groupName);

}