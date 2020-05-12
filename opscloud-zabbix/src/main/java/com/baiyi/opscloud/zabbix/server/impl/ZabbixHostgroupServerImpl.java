package com.baiyi.opscloud.zabbix.server.impl;

import com.baiyi.opscloud.zabbix.builder.ZabbixHostgroupBO;
import com.baiyi.opscloud.zabbix.builder.ZabbixHostgroupBuilder;
import com.baiyi.opscloud.zabbix.entry.ZabbixHostgroup;
import com.baiyi.opscloud.zabbix.handler.ZabbixHandler;
import com.baiyi.opscloud.zabbix.http.ZabbixRequest;
import com.baiyi.opscloud.zabbix.http.ZabbixRequestBuilder;
import com.baiyi.opscloud.zabbix.mapper.ZabbixHostgroupMapper;
import com.baiyi.opscloud.zabbix.mapper.ZabbixIdsMapper;
import com.baiyi.opscloud.zabbix.server.ZabbixHostgroupServer;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/1/8 1:06 下午
 * @Version 1.0
 */
@Component("ZabbixHostgroupServer")
public class ZabbixHostgroupServerImpl implements ZabbixHostgroupServer {

    @Resource
    private ZabbixHandler zabbixHandler;

    @Override
    public ZabbixHostgroup createHostgroup(String hostgroup) {
        ZabbixHostgroup zabbixHostgroup = getHostgroup(hostgroup);
        if (zabbixHostgroup != null) return zabbixHostgroup;
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("hostgroup.create")
                .paramEntry("name", hostgroup)
                .build();
        try {
            JsonNode jsonNode = zabbixHandler.api(request);
            return ZabbixHostgroupBuilder.buildOcCloudserver(ZabbixHostgroupBO.builder()
                    .groupid(new ZabbixIdsMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT).get("groupids")).get(0))
                    .name(hostgroup)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ZabbixHostgroup getHostgroup(String name) {
        Map<String, String> filter = Maps.newHashMap();
        filter.put("name", name);
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("hostgroup.get")
                .paramEntry("filter", filter)
                .paramEntry("output", "extend")
                .build();
        try {
            JsonNode jsonNode = zabbixHandler.api(request);
            return new ZabbixHostgroupMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT)).get(0);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return null;
    }


}
