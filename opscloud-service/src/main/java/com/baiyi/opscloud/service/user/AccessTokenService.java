package com.baiyi.opscloud.service.user;

import com.baiyi.opscloud.domain.generator.opscloud.AccessToken;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/8/5 9:46 上午
 * @Version 1.0
 */
public interface AccessTokenService {

    void add(AccessToken accessToken);

    void update(AccessToken accessToken);

    /**
     * 查询有效的AccessToken
     *
     * @param token
     * @return
     */
    AccessToken getByToken(String token);

    AccessToken getByTokenId(String tokenId);

    /**
     * 按用户名查询有效的AccessToken
     *
     * @param username
     * @return
     */
    List<AccessToken> queryByUsername(String username);

    int checkUserHasResourceAuthorize(String accessToken, String resourceName);

}