package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;

/**
 * @Author baiyi
 * @Date 2020/1/16 10:04 上午
 * @Version 1.0
 */
public interface AuthBaseFacade {

    /**
     * 判断用户请求资源是否授权
     *
     * @param token
     * @param resourceName
     * @return
     */
    BusinessWrapper<Boolean> checkUserHasResourceAuthorize(String token, String resourceName);

    String getUserByToken(String token);

    /**
     * 撤销用户Token
     * @param username
     */
    void revokeUserToken(String username);

    void setUserToken(String username, String token);

    /**
     * 填充用户密码
     * @param ocUser
     * @param password
     */
    void setOcUserPassword(OcUser ocUser, String password);


    /**
     * 授权管理员所有角色
     *
     * @param ocUser
     */
    void authorizedAdminAllRole(OcUser ocUser);
}
