package com.baiyi.opscloud.datasource.ldap.repo.impl;

import com.baiyi.opscloud.common.datasource.LdapConfig;
import com.baiyi.opscloud.datasource.ldap.drive.LdapDrive;
import com.baiyi.opscloud.datasource.ldap.entity.LdapGroup;
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
    private LdapDrive ldapHandler;

    @Override
    public List<LdapGroup.Group> getGroupList(LdapConfig.Ldap ldapConfig) {
        return ldapHandler.queryGroupList(ldapConfig);
    }

    @Override
    public List<String> queryGroupMember(LdapConfig.Ldap ldapConfig, String groupName) {
        return ldapHandler.queryGroupMember(ldapConfig, groupName);
    }

    @Override
    public List<LdapGroup.Group> searchGroupByUsername(LdapConfig.Ldap ldapConfig, String username) {
        List<String> groupNames = ldapHandler.searchLdapGroup(ldapConfig, username);
        return groupNames.stream().map(e ->
                ldapHandler.getGroupWithDn(ldapConfig, ldapConfig.buildGroupDn(e))
        ).collect(Collectors.toList());
    }

    @Override
    public void removeGroupMember(LdapConfig.Ldap ldapConfig, String groupName, String username) {
        ldapHandler.removeGroupMember(ldapConfig, groupName, username);
    }

    @Override
    public void addGroupMember(LdapConfig.Ldap ldapConfig, String groupName, String username) {
        ldapHandler.addGroupMember(ldapConfig, groupName, username);
    }

    @Override
    public void create(LdapConfig.Ldap ldapConfig, String groupName) {
        LdapGroup.Group group = LdapGroup.Group.builder()
                .groupName(groupName)
                .build();
        ldapHandler.bindGroup(ldapConfig, group);
    }

    @Override
    public void delete(LdapConfig.Ldap ldapConfig, String groupName) {
    }

}
