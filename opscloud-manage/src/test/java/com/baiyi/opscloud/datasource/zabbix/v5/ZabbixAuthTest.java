package com.baiyi.opscloud.datasource.zabbix.v5;

import com.baiyi.opscloud.datasource.zabbix.base.BaseZabbixTest;
import com.baiyi.opscloud.zabbix.v5.driver.base.SimpleZabbixAuth;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/11/18 11:03 上午
 * @Version 1.0
 */
public class ZabbixAuthTest extends BaseZabbixTest {

    @Resource
    private SimpleZabbixAuth simpleZabbixAuth;

    @Test
    void authTest() {
        String auth = simpleZabbixAuth.getAuth(getConfig().getZabbix());
        print(auth);
    }

}
