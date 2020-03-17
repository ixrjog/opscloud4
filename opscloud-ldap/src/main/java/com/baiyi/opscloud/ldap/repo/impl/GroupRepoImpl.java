package com.baiyi.opscloud.ldap.repo.impl;

import com.baiyi.opscloud.ldap.entry.Group;
import com.baiyi.opscloud.ldap.handler.LdapHandler;
import com.baiyi.opscloud.ldap.repo.GroupRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/24 4:34 下午
 * @Version 1.0
 */
@Slf4j
@Component("GroupRepo")
public class GroupRepoImpl implements GroupRepo {

    @Resource
    private LdapHandler ldapHandler;

    @Override
    public List<Group> getGroupList() {
        return ldapHandler.queryGroupList();
    }

    @Override
    public List<String> queryGroupMember(String groupName) {
        return ldapHandler.queryGroupMember(groupName);
    }

    @Override
    public Boolean removeGroupMember(String groupName,String username){
        return ldapHandler.removeGroupMember(groupName, username);
    }

    @Override
    public Boolean addGroupMember(String groupName,String username){
        return ldapHandler.addGroupMember(groupName, username);
    }
}
