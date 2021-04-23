package com.baiyi.opscloud.ldap.repo;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.ldap.entry.Group;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/24 4:33 下午
 * @Version 1.0
 */
public interface GroupRepo {

    List<Group> getGroupList();

    /**
     * 查询用户组成员名列表
     * @param groupName
     * @return
     */
    List<String> queryGroupMember(String groupName);


    /**
     * 移除group中的用户
     * @param groupName
     * @param username
     * @return
     */
    Boolean removeGroupMember(String groupName,String username);

    Boolean addGroupMember(String groupName,String username);

    Boolean create(String groupName);

    BusinessWrapper<Boolean> delete(String groupName);
}
