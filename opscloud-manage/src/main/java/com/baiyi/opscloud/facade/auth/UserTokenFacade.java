package com.baiyi.opscloud.facade.auth;

import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.generator.opscloud.UserToken;
import com.baiyi.opscloud.domain.vo.auth.LogVO;

/**
 * @Author baiyi
 * @Date 2021/5/15 9:59 上午
 * @Version 1.0
 */
public interface UserTokenFacade {

    LogVO.Login userLogin(User user);

    /**
     * 撤销用户令牌
     * @param username
     */
    void revokeUserToken(String username);

    UserToken grantUserToken(String username);
}
