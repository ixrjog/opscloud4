package com.baiyi.opscloud.datasource.nacos;

import com.baiyi.opscloud.datasource.nacos.base.BaseNacosTest;
import com.baiyi.opscloud.datasource.nacos.entry.NacosCluster;
import com.baiyi.opscloud.datasource.nacos.entry.NacosLogin;
import com.baiyi.opscloud.datasource.nacos.handler.NacosAuthHandler;
import com.baiyi.opscloud.datasource.nacos.handler.NacosClusterHandler;
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


    @Resource
    private NacosClusterHandler nacosClusterHandler;

    @Test
    void authLoginTest() {
        NacosLogin.AccessToken accessToken = nacosAuthHandler.login(getConfig().getNacos());
        System.err.println(accessToken.toString());
    }

    @Test
    void listNodeTest() {
        NacosCluster.NodesResponse nr= nacosClusterHandler.listNode(getConfig().getNacos());
        System.err.println(nr.getData());
    }
}