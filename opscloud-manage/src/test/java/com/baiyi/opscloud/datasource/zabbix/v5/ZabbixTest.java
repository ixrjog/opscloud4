package com.baiyi.opscloud.datasource.zabbix.v5;

import com.baiyi.opscloud.datasource.zabbix.base.BaseZabbixTest;
import com.baiyi.opscloud.zabbix.v5.datasource.base.SimpleZabbixAuth;
import com.baiyi.opscloud.zabbix.v5.datasource.ZabbixV5HostDatasource;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixHost;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/11/18 11:03 上午
 * @Version 1.0
 */
public class ZabbixTest extends BaseZabbixTest {

    @Resource
    private SimpleZabbixAuth simpleZabbixAuth;

    @Resource
    private ZabbixV5HostDatasource zabbixV5HostDatasource;

    @Test
    void authTest() {
        String auth = simpleZabbixAuth.getAuth(getConfig().getZabbix());
        print(auth);
    }

    @Test
    void listHostTest() {
        List<ZabbixHost.Host> hosts = zabbixV5HostDatasource.list(getConfig().getZabbix());
        print(hosts);
    }

}
