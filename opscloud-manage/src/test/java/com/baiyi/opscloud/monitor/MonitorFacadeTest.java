package com.baiyi.opscloud.monitor;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.facade.MonitorFacade;
import com.baiyi.opscloud.zabbix.entry.ZabbixHost;
import com.baiyi.opscloud.zabbix.server.ZabbixHostServer;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/11/24 3:02 下午
 * @Version 1.0
 */
public class MonitorFacadeTest extends BaseUnit {

    @Resource
    private MonitorFacade monitorFacade;

    @Resource
    private ZabbixHostServer zabbixHostServer;

    @Test
    void syncMonitorHostStatusTest() {
        monitorFacade.syncMonitorHostStatus();
    }

    @Test
    void getHostTest() {
        ZabbixHost host = zabbixHostServer.getHost("172.16.1.57");
        System.err.println(JSON.toJSON(host));
    }
}
