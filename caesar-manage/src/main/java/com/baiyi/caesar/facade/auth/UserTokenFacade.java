package com.baiyi.caesar.facade.auth;

import com.baiyi.caesar.domain.generator.caesar.User;
import com.baiyi.caesar.domain.generator.caesar.UserToken;
import com.baiyi.caesar.vo.auth.LogVO;

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
