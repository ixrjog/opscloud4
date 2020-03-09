package com.baiyi.opscloud.service.jumpserver;

import com.baiyi.opscloud.domain.generator.jumpserver.UsersUserGroups;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/8 5:25 下午
 * @Version 1.0
 */
public interface UsersUserGroupsService {

    UsersUserGroups queryUsersUserGroupsByUniqueKey(UsersUserGroups usersUserGroups);

    void addUsersUserGroups(UsersUserGroups usersUserGroups);

    void delUsersUserGroupsById(int id);

    List<UsersUserGroups> queryUsersUserGroupsByUserId(String userId);

    void delUsersUserGroupsByUserId(String userId);
}
