package com.baiyi.opscloud.zabbix;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.service.server.OcServerGroupService;
import com.baiyi.opscloud.zabbix.entry.ZabbixAction;
import com.baiyi.opscloud.zabbix.http.ZabbixRequest;
import com.baiyi.opscloud.zabbix.http.ZabbixRequestBuilder;
import com.baiyi.opscloud.zabbix.server.ZabbixServer;
import com.google.common.collect.Maps;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/1/2 9:38 上午
 * @Version 1.0
 */
public class ZabbixServerTest extends BaseUnit {

    @Resource
    private ZabbixServer zabbixServer;

    @Resource
    private OcServerGroupService ocServerGroupService;

    @Test
    public void testZabbixRequestBuilder() {
        try {
            Map<String, String> filter = Maps.newHashMap();
            filter.put("ip", "192.168.1.1");
            System.err.println(filter);
            ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                    .method("host.get")
                    .paramEntry("filter", filter)
                    .paramEntry("aaa", "bbb")
                    .build();
            System.err.println(JSON.toJSONString(request));
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Test
    void testGetAction() {
        ZabbixAction action = zabbixServer.getAction("users_admin-job");
        System.err.println(JSON.toJSONString(action));
    }

    @Test
    void testCreateAction() {
        zabbixServer.createAction("users_test");

    }

}
