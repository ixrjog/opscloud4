package com.baiyi.opscloud.zabbix.handler;

import com.baiyi.opscloud.common.datasource.config.DsZabbixConfig;
import com.baiyi.opscloud.zabbix.entry.ZabbixHost;
import com.baiyi.opscloud.zabbix.entry.ZabbixHostGroup;
import com.baiyi.opscloud.zabbix.entry.ZabbixTemplate;
import com.baiyi.opscloud.zabbix.http.ZabbixFilter;
import com.baiyi.opscloud.zabbix.http.ZabbixFilterBuilder;
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
 * @Date 2021/7/1 2:50 下午
 * @Since 1.0
 */

@Component
public class ZabbixTemplateHandler {

    @Resource
    private ZabbixHandler zabbixHandler;

    private interface Method {
        String QUERY_TEMPLATE = "template.get";
    }

    public List<ZabbixTemplate> listTemplates(DsZabbixConfig.Zabbix zabbix) {
        ZabbixRequest request = ZabbixRequestBuilder.builder()
                .method(Method.QUERY_TEMPLATE)
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        return ZabbixMapper.mapperList(data.get("result"), ZabbixTemplate.class);
    }

    public List<ZabbixTemplate> listTemplatesByHost(DsZabbixConfig.Zabbix zabbix, ZabbixHost host) {
        ZabbixRequest request = ZabbixRequestBuilder.builder()
                .method(Method.QUERY_TEMPLATE)
                .paramEntry("hostids", host.getHostId())
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        return ZabbixMapper.mapperList(data.get("result"), ZabbixTemplate.class);
    }

    public List<ZabbixTemplate> listTemplatesByGroup(DsZabbixConfig.Zabbix zabbix, ZabbixHostGroup group) {
        ZabbixRequest request = ZabbixRequestBuilder.builder()
                .method(Method.QUERY_TEMPLATE)
                .paramEntry("groupids", group.getGroupId())
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        return ZabbixMapper.mapperList(data.get("result"), ZabbixTemplate.class);
    }

    public ZabbixTemplate getTemplateById(DsZabbixConfig.Zabbix zabbix, String templateId) {
        ZabbixRequest request = ZabbixRequestBuilder.builder()
                .method(Method.QUERY_TEMPLATE)
                .paramEntry("templateids", templateId)
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        List<ZabbixTemplate> templates = ZabbixMapper.mapperList(data.get("result"), ZabbixTemplate.class);
        if (CollectionUtils.isEmpty(templates))
            throw new RuntimeException("ZabbixTemplate不存在");
        return templates.get(0);
    }

    public List<ZabbixTemplate> listTemplateByNames(DsZabbixConfig.Zabbix zabbix, List<String> names) {
        ZabbixFilter filter = ZabbixFilterBuilder.builder()
                .putEntry("host", names)
                .build();
        ZabbixRequest request = ZabbixRequestBuilder.builder()
                .method(Method.QUERY_TEMPLATE)
                .filter(filter)
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        return ZabbixMapper.mapperList(data.get("result"), ZabbixTemplate.class);
    }
}
