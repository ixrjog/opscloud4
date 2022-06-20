package com.baiyi.opscloud.facade.auth;

import com.baiyi.opscloud.domain.param.auth.LoginParam;
import com.baiyi.opscloud.domain.vo.auth.LogVO;

/**
 * @Author baiyi
 * @Date 2021/5/14 4:07 下午
 * @Version 1.0
 */
public interface UserAuthFacade {

    void tryUserHasResourceAuthorize(String token, String resourceName);

    void tryUserHasResourceAuthorizeByAccessToken(String accessToken, String resourceName);

    /**
     * 用户登录认证
     * @param loginParam
     * @return
     */
    LogVO.Login login(LoginParam.Login loginParam);

    LogVO.Login simpleLogin(LoginParam.Login loginParam);

    /**
     * 用户登出
     */
    void logout();

}
