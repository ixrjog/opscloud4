package com.baiyi.opscloud.service.user;

import com.baiyi.opscloud.domain.generator.OcUserApiToken;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/26 5:34 下午
 * @Version 1.0
 */
public interface OcUserApiTokenService {

    List<OcUserApiToken> queryOcUserApiTokenByUsername(String username);

    OcUserApiToken queryOcUserApiTokenByTokenAndValid(String token);

    OcUserApiToken addOcUserApiToken(OcUserApiToken ocUserApiToken);

    void  updateOcUserApiToken(OcUserApiToken ocUserApiToken);

    int checkUserHasResourceAuthorize(String token, String resourceName);
}
