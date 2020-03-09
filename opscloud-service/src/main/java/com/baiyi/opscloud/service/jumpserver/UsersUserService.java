package com.baiyi.opscloud.service.jumpserver;

import com.baiyi.opscloud.domain.generator.jumpserver.UsersUser;

/**
 * @Author baiyi
 * @Date 2020/3/8 4:41 下午
 * @Version 1.0
 */
public interface UsersUserService {
    UsersUser queryUsersUserByUsername(String username);

    void addUsersUser(UsersUser usersUser);

    void updateUsersUser(UsersUser usersUser);

    void delUsersUserById(String id);
}
