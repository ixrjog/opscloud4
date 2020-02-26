package com.baiyi.opscloud.service.user;

import com.baiyi.opscloud.domain.generator.OcUserApiToken;

/**
 * @Author baiyi
 * @Date 2020/2/26 5:34 下午
 * @Version 1.0
 */
public interface OcUserApiTokenService {

    OcUserApiToken queryOcUserApiTokenByTokenAndValid(String token);

    void  updateOcUserApiToken(OcUserApiToken ocUserApiToken);

    int checkUserHasResourceAuthorize(String token, String resourceName);
}
