package com.baiyi.opscloud.service.jumpserver;

import com.baiyi.opscloud.domain.generator.jumpserver.UsersUserGroups;

/**
 * @Author baiyi
 * @Date 2020/3/8 5:25 下午
 * @Version 1.0
 */
public interface UsersUserGroupsService {

    UsersUserGroups queryUsersUserGroupsByUniqueKey(UsersUserGroups usersUserGroups);

    void addUsersUserGroups(UsersUserGroups usersUserGroups);
}
