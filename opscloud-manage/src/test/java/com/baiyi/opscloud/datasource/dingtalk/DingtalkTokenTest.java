package com.baiyi.opscloud.datasource.dingtalk;

import com.baiyi.opscloud.datasource.dingtalk.base.BaseDingtalkTest;
import com.baiyi.opscloud.datasource.dingtalk.driver.DingtalkTokenDriver;
import com.baiyi.opscloud.datasource.dingtalk.entity.DingtalkToken;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/11/29 3:38 下午
 * @Version 1.0
 */
public class DingtalkTokenTest extends BaseDingtalkTest {

    @Resource
    private DingtalkTokenDriver dingtalkTokenDrive;

    @Test
    void getTokenTest() {
        // {"accessToken":"d61eff7dd04a3a36894dd3a02cb84ee0","errcode":0,"errmsg":"ok","expiresIn":7200}
        DingtalkToken.TokenResponse tokenResponse = dingtalkTokenDrive.getToken(getConfig().getDingtalk());
        print(tokenResponse);
    }

}
