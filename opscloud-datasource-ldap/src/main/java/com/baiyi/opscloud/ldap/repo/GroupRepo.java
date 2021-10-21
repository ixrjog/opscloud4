package com.baiyi.opscloud.ldap.repo;

import com.baiyi.opscloud.common.datasource.LdapDsInstanceConfig;
import com.baiyi.opscloud.ldap.entry.Group;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/24 4:33 下午
 * @Version 1.0
 */
public interface GroupRepo {

    List<Group> getGroupList(LdapDsInstanceConfig.Ldap ldapConfig);

    /**
     * 查询用户组成员名列表
     *
     * @param groupName
     * @return
     */
    List<String> queryGroupMember(LdapDsInstanceConfig.Ldap ldapConfig, String groupName);

    List<Group> searchGroupByUsername(LdapDsInstanceConfig.Ldap ldapConfig, String username);

    /**
     * 移除group中的用户
     *
     * @param groupName
     * @param username
     * @return
     */
    void removeGroupMember(LdapDsInstanceConfig.Ldap ldapConfig, String groupName, String username);

    void addGroupMember(LdapDsInstanceConfig.Ldap ldapConfig, String groupName, String username);

    void create(LdapDsInstanceConfig.Ldap ldapConfig, String groupName);

    void delete(LdapDsInstanceConfig.Ldap ldapConfig, String groupName);
}
