package com.baiyi.opscloud.service;

import com.baiyi.opscloud.domain.generator.OcUserToken;

/**
 * @Author baiyi
 * @Date 2020/1/16 10:22 上午
 * @Version 1.0
 */
public interface OcUserTokenService {

    void addOcUserToken(OcUserToken ocUserToken);

    /**
     * 查询有效的Token
     * @param token
     * @return
     */
    OcUserToken queryOcUserTokenByTokenAndValid(String token);
}
