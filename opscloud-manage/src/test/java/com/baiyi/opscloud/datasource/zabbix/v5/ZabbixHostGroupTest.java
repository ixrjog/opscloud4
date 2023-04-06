package com.baiyi.opscloud.datasource.zabbix.v5;

import com.baiyi.opscloud.datasource.zabbix.base.BaseZabbixTest;
import com.baiyi.opscloud.zabbix.v5.driver.ZabbixV5HostDriver;
import com.baiyi.opscloud.zabbix.v5.driver.ZabbixV5HostGroupDriver;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixHost;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixHostGroup;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/11/22 10:45 上午
 * @Version 1.0
 */
public class ZabbixHostGroupTest extends BaseZabbixTest {

    @Resource
    private ZabbixV5HostDriver zabbixV5HostDatasource;

    @Resource
    private ZabbixV5HostGroupDriver zabbixV5HostGroupDatasource;

    @Test
    void listTest() {
        List<ZabbixHostGroup.HostGroup> hostGroups = zabbixV5HostGroupDatasource.list(getConfig().getZabbix());
        print(hostGroups);
    }

    @Test
    void getByNameTest() {
        ZabbixHostGroup.HostGroup hostGroup = zabbixV5HostGroupDatasource.getByName(getConfig().getZabbix(), "group_opscloud4");
        print(hostGroup);
    }

    @Test
    void listByHostTest() {
        ZabbixHost.Host host = zabbixV5HostDatasource.getByIp(getConfig().getZabbix(), "172.19.0.129");
        List<ZabbixHostGroup.HostGroup> hostGroups = zabbixV5HostGroupDatasource.listByHost(getConfig().getZabbix(), host);
        print(hostGroups);
    }


}
