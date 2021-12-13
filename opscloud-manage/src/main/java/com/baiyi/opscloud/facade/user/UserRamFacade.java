package com.baiyi.opscloud.facade.user;

import com.baiyi.opscloud.domain.param.user.UserRamParam;

/**
 * @Author baiyi
 * @Date 2021/12/12 4:10 PM
 * @Version 1.0
 */
public interface UserRamFacade {

    /**
     * 创建RAM账户
     * @param createRamUser
     */
    void createUser(UserRamParam.CreateRamUser createRamUser);
}
