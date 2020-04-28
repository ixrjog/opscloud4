package com.baiyi.opscloud.zabbix;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.server.IServer;
import com.baiyi.opscloud.server.factory.ServerFactory;
import com.baiyi.opscloud.service.server.OcServerService;
import com.baiyi.opscloud.zabbix.entry.ZabbixHost;
import com.baiyi.opscloud.zabbix.entry.ZabbixHostInterface;
import com.baiyi.opscloud.zabbix.server.ZabbixHostServer;
import com.google.common.collect.Maps;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/8 1:14 下午
 * @Version 1.0
 */
public class ZabbixHostServerTest extends BaseUnit {

    @Resource
    private ZabbixHostServer zabbixHostServer;

    @Resource
    private OcServerService ocServerService;

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

    @Test
    void testCreateHost() {
        IServer iServer = ServerFactory.getIServerByKey("ZabbixHost");
        OcServer ocServer= ocServerService.queryOcServerByPrivateIp("192.168.10.183");
        iServer.create(ocServer);
    }

    @Test
    void testRemoveHost() {
        IServer iServer = ServerFactory.getIServerByKey("ZabbixHost");
        OcServer ocServer= ocServerService.queryOcServerByPrivateIp("192.168.10.183");
        iServer.remove(ocServer);
    }


    @Test
    void testDisableHost() {
        IServer iServer = ServerFactory.getIServerByKey("ZabbixHost");
        OcServer ocServer= ocServerService.queryOcServerByPrivateIp("192.168.8.118");
        iServer.disable(ocServer);
    }

    @Test
    void testEnableHost() {
        IServer iServer = ServerFactory.getIServerByKey("ZabbixHost");
        OcServer ocServer= ocServerService.queryOcServerByPrivateIp("192.168.8.118");
        iServer.enable(ocServer);
    }

    @Test
    void testUpdateHost() {
        IServer iServer = ServerFactory.getIServerByKey("ZabbixHost");
        OcServer ocServer= ocServerService.queryOcServerByPrivateIp("192.168.8.118");
        iServer.update(ocServer);
    }



    @Test
    void testJSON() {
        LinkedHashMap<String, String> linkedMap = Maps.newLinkedHashMap();
        linkedMap.put("f1","xxx");
        linkedMap.put("f2","xxxx");
        linkedMap.put("f3","xxxxx");
        System.err.println(JSON.toJSONString(linkedMap));
    }

}
