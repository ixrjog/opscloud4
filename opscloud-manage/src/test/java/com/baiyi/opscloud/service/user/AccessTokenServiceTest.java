package com.baiyi.opscloud.service.user;

import com.baiyi.opscloud.BaseUnit;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2021/8/5 3:33 下午
 * @Version 1.0
 */
class AccessTokenServiceTest extends BaseUnit {

    @Resource
    private AccessTokenService accessTokenService;

    @Test
    void checkUserHasResourceAuthorize() {
       int cnt = accessTokenService.checkUserHasResourceAuthorize("6d9fb9738068486f9ade2e0f4bf49dab","/api/auth/role/page/query");
       System.err.println(cnt);
    }
}