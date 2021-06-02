package com.baiyi.caesar.facade.auth;

import com.baiyi.caesar.domain.param.auth.LoginParam;
import com.baiyi.caesar.domain.vo.auth.LogVO;

/**
 * @Author baiyi
 * @Date 2021/5/14 4:07 下午
 * @Version 1.0
 */
public interface UserAuthFacade {

    void tryUserHasResourceAuthorize(String token, String resourceName);

    /**
     * 用户登录认证
     * @param loginParam
     * @return
     */
    LogVO.Login login(LoginParam.Login loginParam);
}
