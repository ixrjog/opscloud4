package com.baiyi.opscloud.datasource.zabbix.v5;

import com.baiyi.opscloud.datasource.zabbix.base.BaseZabbixTest;
import com.baiyi.opscloud.zabbix.v5.drive.ZabbixV5HostDrive;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixHost;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/11/22 10:14 上午
 * @Version 1.0
 */
public class ZabbixHostTest extends BaseZabbixTest {

    @Resource
    private ZabbixV5HostDrive zabbixV5HostDatasource;

    @Test
    void listHostTest() {
        List<ZabbixHost.Host> hosts = zabbixV5HostDatasource.list(getConfig().getZabbix());
        print(hosts);
    }

    @Test
    void getByIpTest() {
        ZabbixHost.Host host = zabbixV5HostDatasource.getByIp(getConfig().getZabbix(), "172.19.0.129");
        print(host);
    }

    @Test
    void getByIpTest2() {
        ZabbixHost.Host host1 = zabbixV5HostDatasource.getByIp(getConfig().getZabbix(), "172.19.0.129");
        ZabbixHost.Host host2 = zabbixV5HostDatasource.getById(getConfig().getZabbix(), host1.getHostid());
        print(host2);
    }

    @Test
    void updateHostNameTest() {
        ZabbixHost.Host host = zabbixV5HostDatasource.getByIp(getConfig().getZabbix(), "172.19.0.129");
        zabbixV5HostDatasource.updateHostName(getConfig().getZabbix(), host, "opscloud4-1");
    }

}
