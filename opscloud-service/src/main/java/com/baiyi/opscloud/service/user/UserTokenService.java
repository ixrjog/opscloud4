package com.baiyi.opscloud.service.user;

import com.baiyi.opscloud.domain.generator.opscloud.UserToken;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/14 4:04 下午
 * @Version 1.0
 */
public interface UserTokenService {

    int checkUserHasResourceAuthorize(String token, String resourceName);

    int checkUserHasRole(String token, String roleName);

    UserToken getByValidToken(String token);

    List<UserToken> queryByValidTokenByUsername(String username);

    void update(UserToken userToken);

    void add(UserToken userToken);

}