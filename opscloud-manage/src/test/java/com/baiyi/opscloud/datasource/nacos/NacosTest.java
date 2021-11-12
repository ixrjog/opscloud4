package com.baiyi.opscloud.datasource.nacos;

import com.baiyi.opscloud.datasource.nacos.base.BaseNacosTest;
import com.baiyi.opscloud.datasource.nacos.entry.NacosLogin;
import com.baiyi.opscloud.datasource.nacos.handler.NacosAuthHandler;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/11/11 5:20 下午
 * @Version 1.0
 */
public class NacosTest extends BaseNacosTest {

    @Resource
    private NacosAuthHandler nacosAuthHandler;

    @Test
    void authLoginTest() {
        NacosLogin.AccessToken accessToken = nacosAuthHandler.login(getConfig().getNacos());
        System.err.println(accessToken.toString());
    }

}