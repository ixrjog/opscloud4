package com.baiyi.opscloud.service.user;

import com.baiyi.opscloud.domain.generator.opscloud.OcUserToken;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/16 10:22 上午
 * @Version 1.0
 */
public interface OcUserTokenService {

    void addOcUserToken(OcUserToken ocUserToken);

    /**
     * 查询有效的Token
     *
     * @param token
     * @return
     */
    OcUserToken queryOcUserTokenByTokenAndValid(String token);

    /**
     * 查询用户有效的Token
     * @param username
     * @return
     */
    List<OcUserToken> queryOcUserTokenByUsername(String username);

    /**
     * 吊销Token
     * @param ocUserToken
     */
    void updateOcUserTokenInvalid(OcUserToken ocUserToken);

    /**
     * 判断用户是否可访问某个资源
     *
     * @param token
     * @param resourceName
     * @return
     */
    int checkUserHasResourceAuthorize(String token, String resourceName);

    /**
     * 判断用户是否为 角色
     * @param token
     * @param roleName
     * @return
     */
    int checkUserHasRole(String token, String roleName);
}
