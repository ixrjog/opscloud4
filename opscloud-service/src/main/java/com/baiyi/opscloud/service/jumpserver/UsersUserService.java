package com.baiyi.opscloud.service.jumpserver;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.jumpserver.UsersUser;
import com.baiyi.opscloud.domain.param.jumpserver.user.UsersUserPageParam;

/**
 * @Author baiyi
 * @Date 2020/3/8 4:41 下午
 * @Version 1.0
 */
public interface UsersUserService {

    DataTable<UsersUser> fuzzyQueryUsersUserPage(UsersUserPageParam.PageQuery pageQuery);

    DataTable<UsersUser> fuzzyQueryAdminUsersUserPage(UsersUserPageParam.PageQuery pageQuery);

    UsersUser queryUsersUserByUsername(String username);

    UsersUser queryUsersUserById(String id);

    UsersUser queryUsersUserByEmail(String email);

    void addUsersUser(UsersUser usersUser);

    void updateUsersUser(UsersUser usersUser);

    void delUsersUserById(String id);
}
