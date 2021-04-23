package com.baiyi.opscloud.zabbix;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.server.IServer;
import com.baiyi.opscloud.server.factory.ServerFactory;
import com.baiyi.opscloud.service.server.OcServerService;
import com.baiyi.opscloud.zabbix.config.ZabbixConfig;
import com.baiyi.opscloud.zabbix.entry.ZabbixHost;
import com.baiyi.opscloud.zabbix.entry.ZabbixHostInterface;
import com.baiyi.opscloud.zabbix.entry.ZabbixMacro;
import com.baiyi.opscloud.zabbix.server.ZabbixHostServer;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    @Resource
    private ZabbixConfig zabbixConfig;

    @Test
    void testUpdateZabbixHost() {
        ocServerService.queryAllOcServer().forEach(e -> {
            try {
                IServer iServer = ServerFactory.getIServerByKey("ZabbixHost");
                iServer.update(e);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    @Test
    void testMassUpdateHostStatus() {
        ZabbixHost host1 = zabbixHostServer.getHost("172.16.4.69");
        ZabbixHost host2 = zabbixHostServer.getHost("172.16.3.16");
        List<String> hostids = Lists.newArrayList();
        hostids.add(host1.getHostid());
        hostids.add(host2.getHostid());
        BusinessWrapper<Boolean> wrapper = zabbixHostServer.massUpdateHostStatus(hostids, 0);
        System.err.println(JSON.toJSON(wrapper));
    }

    @Test
    void testGetHost() {
        ZabbixHost host = zabbixHostServer.getHost("172.16.3.16");
        System.err.println(JSON.toJSONString(host));
    }

    @Test
    void testGetHostMacros() {
        ZabbixHost host = zabbixHostServer.getHost("172.16.4.69");
        List<ZabbixMacro> macros = zabbixHostServer.getHostMacros(host.getHostid());
        System.err.println(JSON.toJSONString(macros));

        String x = "[{\"macro\":\"{$PORT}\",\"value\":\"8080\"},{\"macro\":\"{$PROJECT}\",\"value\":\"account\"},{\"macro\":\"{$WEBSTATUS}\",\"value\":\"webStatus\"}]";
        Gson gs = new GsonBuilder()
//                .setPrettyPrinting()
//                .disableHtmlEscaping()
                .create();

        ArrayList<Map<String, String>> list = gs.fromJson(x, new TypeToken<ArrayList<Map<String, String>>>() {
        }.getType());

        System.err.println(list);

    }

    @Test
    void testGetHostByHostid() {
        ZabbixHost host = zabbixHostServer.getHostByHostid("13218");
        System.err.println(JSON.toJSONString(host));
    }

    @Test
    void testGetHostInterfaceList() {
        List<ZabbixHostInterface> list = zabbixHostServer.getHostInterfaces("13218");
        System.err.println(JSON.toJSONString(list));
    }

    @Test
    void testCreateHost() {
        IServer iServer = ServerFactory.getIServerByKey("ZabbixHost");
        OcServer ocServer = ocServerService.queryOcServerByPrivateIp("172.16.210.244");
        iServer.create(ocServer);
    }

    @Test
    void testRemoveHost() {
        IServer iServer = ServerFactory.getIServerByKey("ZabbixHost");
        OcServer ocServer = ocServerService.queryOcServerByPrivateIp("192.168.10.183");
        iServer.remove(ocServer);
    }

    @Test
    void testDisableHost() {
        IServer iServer = ServerFactory.getIServerByKey("ZabbixHost");
        OcServer ocServer = ocServerService.queryOcServerByPrivateIp("192.168.8.118");
        iServer.disable(ocServer);
    }

    @Test
    void testEnableHost() {
        IServer iServer = ServerFactory.getIServerByKey("ZabbixHost");
        OcServer ocServer = ocServerService.queryOcServerByPrivateIp("192.168.8.118");
        iServer.enable(ocServer);
    }

    @Test
    void testUpdateHost() {
        IServer iServer = ServerFactory.getIServerByKey("ZabbixHost");
        OcServer ocServer = ocServerService.queryOcServerByPrivateIp("192.168.8.118");
        iServer.update(ocServer);
    }

    @Test
    void testJSON() {
        LinkedHashMap<String, String> linkedMap = Maps.newLinkedHashMap();
        linkedMap.put("f1", "xxx");
        linkedMap.put("f2", "xxxx");
        linkedMap.put("f3", "xxxxx");
        System.err.println(JSON.toJSONString(linkedMap));
    }

    @Test
    void configTest(){
        System.err.println(JSON.toJSON(zabbixConfig.getOperation().getMessage()));
    }

}
