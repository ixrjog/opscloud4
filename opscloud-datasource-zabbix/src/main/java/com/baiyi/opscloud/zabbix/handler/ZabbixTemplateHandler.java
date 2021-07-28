package com.baiyi.opscloud.zabbix.handler;

import com.baiyi.opscloud.common.datasource.config.DsZabbixConfig;
import com.baiyi.opscloud.zabbix.entry.ZabbixHost;
import com.baiyi.opscloud.zabbix.entry.ZabbixHostGroup;
import com.baiyi.opscloud.zabbix.entry.ZabbixTemplate;
import com.baiyi.opscloud.zabbix.http.ZabbixFilter;
import com.baiyi.opscloud.zabbix.http.ZabbixFilterBuilder;
import com.baiyi.opscloud.zabbix.http.ZabbixCommonRequest;
import com.baiyi.opscloud.zabbix.http.ZabbixCommonRequestBuilder;
import com.baiyi.opscloud.zabbix.mapper.ZabbixMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

import static com.baiyi.opscloud.zabbix.handler.ZabbixHandler.ApiConstant.*;

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
        ZabbixCommonRequest request = ZabbixCommonRequestBuilder.builder()
                .method(Method.QUERY_TEMPLATE)
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        return ZabbixMapper.mapperList(data.get(RESULT), ZabbixTemplate.class);
    }

    public List<ZabbixTemplate> listTemplatesByHost(DsZabbixConfig.Zabbix zabbix, ZabbixHost host) {
        ZabbixCommonRequest request = ZabbixCommonRequestBuilder.builder()
                .method(Method.QUERY_TEMPLATE)
                .paramEntry(HOST_IDS, host.getHostId())
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        return ZabbixMapper.mapperList(data.get(RESULT), ZabbixTemplate.class);
    }

    public List<ZabbixTemplate> listTemplatesByGroup(DsZabbixConfig.Zabbix zabbix, ZabbixHostGroup group) {
        ZabbixCommonRequest request = ZabbixCommonRequestBuilder.builder()
                .method(Method.QUERY_TEMPLATE)
                .paramEntry(HOST_GROUP_IDS, group.getGroupId())
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        return ZabbixMapper.mapperList(data.get(RESULT), ZabbixTemplate.class);
    }

    public ZabbixTemplate getTemplateById(DsZabbixConfig.Zabbix zabbix, String templateId) {
        ZabbixCommonRequest request = ZabbixCommonRequestBuilder.builder()
                .method(Method.QUERY_TEMPLATE)
                .paramEntry(TEMPLATE_IDS, templateId)
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        List<ZabbixTemplate> templates = ZabbixMapper.mapperList(data.get(RESULT), ZabbixTemplate.class);
        if (CollectionUtils.isEmpty(templates))
            throw new RuntimeException("ZabbixTemplate不存在");
        return templates.get(0);
    }

    public List<ZabbixTemplate> listTemplateByNames(DsZabbixConfig.Zabbix zabbix, List<String> names) {
        ZabbixFilter filter = ZabbixFilterBuilder.builder()
                .putEntry("host", names)
                .build();
        ZabbixCommonRequest request = ZabbixCommonRequestBuilder.builder()
                .method(Method.QUERY_TEMPLATE)
                .filter(filter)
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        return ZabbixMapper.mapperList(data.get(RESULT), ZabbixTemplate.class);
    }
}
