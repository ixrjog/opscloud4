package com.baiyi.opscloud.zabbix.server.impl;

import com.baiyi.opscloud.zabbix.entry.ZabbixHost;
import com.baiyi.opscloud.zabbix.entry.ZabbixHostInterface;
import com.baiyi.opscloud.zabbix.handler.ZabbixHandler;
import com.baiyi.opscloud.zabbix.http.ZabbixRequest;
import com.baiyi.opscloud.zabbix.http.ZabbixRequestBuilder;
import com.baiyi.opscloud.zabbix.http.ZabbixRequestParamsMap;
import com.baiyi.opscloud.zabbix.mapper.ZabbixHostInterfaceMapper;
import com.baiyi.opscloud.zabbix.mapper.ZabbixHostMapper;
import com.baiyi.opscloud.zabbix.server.ZabbixHostServer;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/1/8 1:10 下午
 * @Version 1.0
 */
@Component("ZabbixHostServer")
public class ZabbixHostServerImpl implements ZabbixHostServer {

    public static final int HOST_STATUS_ENABLE = 0;
    public static final int HOST_STATUS_DISABLE = 1;

    @Resource
    private ZabbixHandler zabbixHandler;

    @Override
    public ZabbixHost getHost(String mgmtIp) {
        try {
            Map<String, String> filter = Maps.newHashMap();
            filter.put("ip", mgmtIp);
            ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                    .method("host.get")
                    .paramEntry("filter", filter)
                    .build();
            JsonNode jsonNode = zabbixHandler.api(request);
            return new ZabbixHostMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT)).get(0);
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public List<ZabbixHost> getHostList() {
        try {
            ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                    .method("host.get")
                    .build();
            JsonNode jsonNode = zabbixHandler.api(request);
            return new ZabbixHostMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT));
        } catch (Exception e) {
        }
        return Collections.emptyList();
    }

    @Override
    public List<ZabbixHostInterface> getHostInterfaceList(String hostid) {
        ZabbixRequestParamsMap request = ZabbixRequestBuilder.newBuilder()
                .method("hostinterface.get")
                .paramEntry("output", "extend")
                .paramEntry("hostids", hostid)
                .build();
        try {
            JsonNode jsonNode = zabbixHandler.api(request);
            return new ZabbixHostInterfaceMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT));
        } catch (Exception e) {
        }
        return Collections.emptyList();
    }


    @Override
    public ZabbixHost getHostByHostid(String hostid) {
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("host.get")
                .paramEntry("hostids", hostid)
                .build();
        try {
            JsonNode jsonNode = zabbixHandler.api(request);
            return new ZabbixHostMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT)).get(0);
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public ZabbixHost updateHostStatus(String hostid, int status) {
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("host.update")
                .paramEntry("hostid", hostid)
                .paramEntry("status", status)
                .build();
        try {
            JsonNode jsonNode = zabbixHandler.api(request);
            return new ZabbixHostMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT)).get(0);
        } catch (Exception e) {
        }
        return null;
    }

}
