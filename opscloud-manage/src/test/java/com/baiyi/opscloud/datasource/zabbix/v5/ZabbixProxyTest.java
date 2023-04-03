package com.baiyi.opscloud.datasource.zabbix.v5;

import com.baiyi.opscloud.datasource.zabbix.base.BaseZabbixTest;
import com.baiyi.opscloud.zabbix.v5.driver.ZabbixV5ProxyDriver;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixProxy;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/12/27 2:49 PM
 * @Version 1.0
 */
public class ZabbixProxyTest extends BaseZabbixTest {

    private static final String PROXY_NAME = "hangzhou.proxy.zabbix.chuanyinet.com";

    @Resource
    private ZabbixV5ProxyDriver zabbixV5ProxyDrive;

    @Test
    void getProxyTest() {
        ZabbixProxy.Proxy proxy = zabbixV5ProxyDrive.getProxy(getConfig().getZabbix(), PROXY_NAME);
        print(proxy);
    }

}

