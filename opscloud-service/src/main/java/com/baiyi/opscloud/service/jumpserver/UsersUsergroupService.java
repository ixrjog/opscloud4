package com.baiyi.opscloud.service.jumpserver;

import com.baiyi.opscloud.domain.generator.jumpserver.UsersUsergroup;

/**
 * @Author baiyi
 * @Date 2020/3/8 4:56 下午
 * @Version 1.0
 */
public interface UsersUsergroupService {

    UsersUsergroup queryUsersUsergroupByName(String name);

    void addUsersUsergroup(UsersUsergroup usersUsergroup);
}
