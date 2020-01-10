package com.baiyi.opscloud.zabbix;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.zabbix.entry.ZabbixHost;
import com.baiyi.opscloud.zabbix.entry.ZabbixHostInterface;
import com.baiyi.opscloud.zabbix.server.ZabbixHostServer;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/8 1:14 下午
 * @Version 1.0
 */
public class ZabbixHostServerTest extends BaseUnit {

    @Resource
    private ZabbixHostServer zabbixHostServer;

    @Test
    void testGetHost() {
        ZabbixHost host = zabbixHostServer.getHost("192.168.105.9");
        System.err.println(JSON.toJSONString(host));
    }

    @Test
    void testGetHostByHostid() {
        ZabbixHost host = zabbixHostServer.getHostByHostid("13218");
        System.err.println(JSON.toJSONString(host));
    }

    @Test
    void testGetHostInterfaceList() {
        List<ZabbixHostInterface> list = zabbixHostServer.getHostInterfaceList("13218");
        System.err.println(JSON.toJSONString(list ));
    }


}
