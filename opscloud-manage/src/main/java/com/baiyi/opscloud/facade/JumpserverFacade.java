package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroup;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.vo.user.OcUserVO;

/**
 * @Author baiyi
 * @Date 2020/3/8 4:18 下午
 * @Version 1.0
 */
public interface JumpserverFacade {

    BusinessWrapper<Boolean> syncUsers();

    /**
     * 同步服务器到资产
     *
     * @param ocServer
     */
    void addAssets(OcServer ocServer);


    /**
     * 用户授权用户组
     *
     * @param ocUser
     * @param ocServerGroup
     */
    void grant(OcUser ocUser, OcServerGroup ocServerGroup);

    /**
     * 用户撤销用户组
     *
     * @param ocUser
     * @param ocServerGroup
     */
    void revoke(OcUser ocUser, OcServerGroup ocServerGroup);

    boolean activeUsersUser(String username, boolean active);

    boolean delUsersUser(String username);

    boolean updateUsersUser(OcUser ocUser);

    /**
     * 通过API推送用户公钥
     * @param user
     * @return
     */
    boolean pushKey(OcUserVO.User user);
}
