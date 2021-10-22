package com.baiyi.opscloud.datasource.ldap.repo.impl;

import com.baiyi.opscloud.common.datasource.LdapDsInstanceConfig;
import com.baiyi.opscloud.datasource.ldap.entry.Group;
import com.baiyi.opscloud.datasource.ldap.handler.LdapHandler;
import com.baiyi.opscloud.datasource.ldap.repo.GroupRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/6/15 10:53 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class GroupRepoImpl implements GroupRepo {

    @Resource
    private LdapHandler ldapHandler;

    @Override
    public List<Group> getGroupList(LdapDsInstanceConfig.Ldap ldapConfig) {
        return ldapHandler.queryGroupList(ldapConfig);
    }

    @Override
    public List<String> queryGroupMember(LdapDsInstanceConfig.Ldap ldapConfig, String groupName) {
        return ldapHandler.queryGroupMember(ldapConfig, groupName);
    }

    @Override
    public List<Group> searchGroupByUsername(LdapDsInstanceConfig.Ldap ldapConfig, String username) {
        List<String> groupNames = ldapHandler.searchLdapGroup(ldapConfig, username);
        return groupNames.stream().map(e ->
                ldapHandler.getGroupWithDn(ldapConfig, ldapConfig.buildGroupDn(e))
        ).collect(Collectors.toList());
    }

    @Override
    public void removeGroupMember(LdapDsInstanceConfig.Ldap ldapConfig, String groupName, String username) {
        ldapHandler.removeGroupMember(ldapConfig, groupName, username);
    }

    @Override
    public void addGroupMember(LdapDsInstanceConfig.Ldap ldapConfig, String groupName, String username) {
        ldapHandler.addGroupMember(ldapConfig, groupName, username);
    }

    @Override
    public void create(LdapDsInstanceConfig.Ldap ldapConfig, String groupName) {

    }

    @Override
    public void delete(LdapDsInstanceConfig.Ldap ldapConfig, String groupName) {
    }

}
