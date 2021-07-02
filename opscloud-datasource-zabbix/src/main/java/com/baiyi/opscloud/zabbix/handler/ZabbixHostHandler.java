package com.baiyi.opscloud.zabbix.handler;

import com.baiyi.opscloud.common.datasource.config.DsZabbixConfig;
import com.baiyi.opscloud.zabbix.entry.*;
import com.baiyi.opscloud.zabbix.http.ZabbixRequest;
import com.baiyi.opscloud.zabbix.http.ZabbixRequestBuilder;
import com.baiyi.opscloud.zabbix.mapper.ZabbixMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/1 2:07 下午
 * @Since 1.0
 */

@Component
public class ZabbixHostHandler {

    @Resource
    private ZabbixHandler zabbixHandler;

    private interface Method {
        String QUERY_HOST = "host.get";
    }

    public List<ZabbixHost> listHosts(DsZabbixConfig.Zabbix zabbix) {
        ZabbixRequest request = ZabbixRequestBuilder.builder()
                .method(Method.QUERY_HOST)
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        return ZabbixMapper.mapperList(data.get("result"), ZabbixHost.class);
    }

    public List<ZabbixHost> listHostsByGroup(DsZabbixConfig.Zabbix zabbix, ZabbixHostGroup group) {
        ZabbixRequest request = ZabbixRequestBuilder.builder()
                .method(Method.QUERY_HOST)
                .paramEntry("groupids", group.getGroupId())
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        return ZabbixMapper.mapperList(data.get("result"), ZabbixHost.class);
    }

    public List<ZabbixHost> listHostsByTemplate(DsZabbixConfig.Zabbix zabbix, ZabbixTemplate template) {
        ZabbixRequest request = ZabbixRequestBuilder.builder()
                .method(Method.QUERY_HOST)
                .paramEntry("templateids", template.getTemplateId())
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        return ZabbixMapper.mapperList(data.get("result"), ZabbixHost.class);
    }

    public List<ZabbixHost> listHostsByTrigger(DsZabbixConfig.Zabbix zabbix, ZabbixTrigger trigger) {
        ZabbixRequest request = ZabbixRequestBuilder.builder()
                .method(Method.QUERY_HOST)
                .paramEntry("triggerids", trigger.getTriggerId())
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        return ZabbixMapper.mapperList(data.get("result"), ZabbixHost.class);
    }

    public ZabbixHost getHostById(DsZabbixConfig.Zabbix zabbix, String hostId) {
        ZabbixRequest request = ZabbixRequestBuilder.builder()
                .method(Method.QUERY_HOST)
                .paramEntry("hostids", hostId)
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        List<ZabbixHost> hosts = ZabbixMapper.mapperList(data.get("result"), ZabbixHost.class);
        if (CollectionUtils.isEmpty(hosts))
            throw new RuntimeException("ZabbixHost不存在");
        return hosts.get(0);
    }
}
