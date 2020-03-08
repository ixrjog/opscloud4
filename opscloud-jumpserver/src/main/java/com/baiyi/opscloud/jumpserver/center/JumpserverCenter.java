package com.baiyi.opscloud.jumpserver.center;

import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroup;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.generator.jumpserver.UsersUser;

/**
 * @Author baiyi
 * @Date 2020/1/9 11:36 上午
 * @Version 1.0
 */
public interface JumpserverCenter {

    /**
     * 用户绑定服务器组
     * @param usersUser
     * @param ocServerGroup
     */
    void bindUserGroups(UsersUser usersUser, OcServerGroup ocServerGroup);

    /**
     * 创建用户
     * @param ocUser
     * @return
     */
    UsersUser createUsersUser(OcUser ocUser);
}
