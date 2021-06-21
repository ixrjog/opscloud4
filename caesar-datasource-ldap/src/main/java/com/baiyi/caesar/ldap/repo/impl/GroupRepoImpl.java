package com.baiyi.caesar.ldap.repo.impl;

import com.baiyi.caesar.common.datasource.config.LdapDsConfig;
import com.baiyi.caesar.ldap.entry.Group;
import com.baiyi.caesar.ldap.handler.LdapHandler;
import com.baiyi.caesar.ldap.repo.GroupRepo;
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
    public List<Group> getGroupList(LdapDsConfig.Ldap ldapConfig) {
        return ldapHandler.queryGroupList(ldapConfig);
    }

    @Override
    public List<String> queryGroupMember(LdapDsConfig.Ldap ldapConfig, String groupName) {
        return ldapHandler.queryGroupMember(ldapConfig, groupName);
    }

    @Override
    public List<Group> searchGroupByUsername(LdapDsConfig.Ldap ldapConfig, String username) {
        List<String> groupNames = ldapHandler.searchLdapGroup(ldapConfig, username);
        return groupNames.stream().map(e ->
                ldapHandler.getGroupWithDn(ldapConfig, ldapConfig.buildGroupDn(e))
        ).collect(Collectors.toList());
    }

    @Override
    public void removeGroupMember(LdapDsConfig.Ldap ldapConfig, String groupName, String username) {
        ldapHandler.removeGroupMember(ldapConfig, groupName, username);
    }

    @Override
    public void addGroupMember(LdapDsConfig.Ldap ldapConfig, String groupName, String username) {
        ldapHandler.addGroupMember(ldapConfig, groupName, username);
    }

    @Override
    public void create(LdapDsConfig.Ldap ldapConfig, String groupName) {

    }

    @Override
    public void delete(LdapDsConfig.Ldap ldapConfig, String groupName) {
    }

}
