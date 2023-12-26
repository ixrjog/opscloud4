package com.baiyi.opscloud.datasource.ldap.repo.impl;

import com.baiyi.opscloud.common.datasource.LdapConfig;
import com.baiyi.opscloud.datasource.ldap.driver.LdapDriver;
import com.baiyi.opscloud.datasource.ldap.entity.LdapGroup;
import com.baiyi.opscloud.datasource.ldap.repo.GroupRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/6/15 10:53 上午
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GroupRepoImpl implements GroupRepo {

    private final LdapDriver ldapDrive;

    @Override
    public List<LdapGroup.Group> getGroupList(LdapConfig.Ldap ldapConfig) {
        return ldapDrive.queryGroupList(ldapConfig);
    }

    @Override
    public List<String> queryGroupMember(LdapConfig.Ldap ldapConfig, String groupName) {
        return ldapDrive.queryGroupMember(ldapConfig, groupName);
    }

    @Override
    public List<LdapGroup.Group> searchGroupByUsername(LdapConfig.Ldap ldapConfig, String username) {
        List<String> groupNames = ldapDrive.searchLdapGroup(ldapConfig, username);
        return groupNames.stream().map(e ->
                ldapDrive.getGroupWithDN(ldapConfig, ldapConfig.buildGroupDn(e))
        ).collect(Collectors.toList());
    }

    @Override
    public void removeGroupMember(LdapConfig.Ldap ldapConfig, String groupName, String username) {
        ldapDrive.removeGroupMember(ldapConfig, groupName, username);
    }

    @Override
    public void addGroupMember(LdapConfig.Ldap ldapConfig, String groupName, String username) {
        ldapDrive.addGroupMember(ldapConfig, groupName, username);
    }

    @Override
    public void create(LdapConfig.Ldap ldapConfig, String groupName) {
        LdapGroup.Group group = LdapGroup.Group.builder()
                .groupName(groupName)
                .build();
        ldapDrive.bindGroup(ldapConfig, group);
    }

    @Override
    public void delete(LdapConfig.Ldap ldapConfig, String groupName) {
    }

}