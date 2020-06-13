package com.baiyi.opscloud.zabbix.server.impl;

import com.baiyi.opscloud.zabbix.entry.ZabbixHost;
import com.baiyi.opscloud.zabbix.entry.ZabbixHostInterface;
import com.baiyi.opscloud.zabbix.entry.ZabbixTemplate;
import com.baiyi.opscloud.zabbix.handler.ZabbixHandler;
import com.baiyi.opscloud.zabbix.http.ZabbixRequest;
import com.baiyi.opscloud.zabbix.http.ZabbixRequestBuilder;
import com.baiyi.opscloud.zabbix.http.ZabbixRequestParamsMap;
import com.baiyi.opscloud.zabbix.mapper.ZabbixHostInterfaceMapper;
import com.baiyi.opscloud.zabbix.mapper.ZabbixHostMapper;
import com.baiyi.opscloud.zabbix.mapper.ZabbixIdsMapper;
import com.baiyi.opscloud.zabbix.mapper.ZabbixTemplateMapper;
import com.baiyi.opscloud.zabbix.server.ZabbixHostServer;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
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
    public List<ZabbixTemplate> getHostTemplates(String hostid) {
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("host.get")
                .paramEntry("output", new String[]{"hostid"})
                .paramEntry("selectParentTemplates", new String[]{"templateid", "name"})
                .paramEntry("hostids", hostid)
                .build();
        try {
            JsonNode jsonNode = zabbixHandler.api(request);
            JsonNode result = jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT);


            JsonNode parentTemplates = result.get(0).get(ZabbixServerImpl.ZABBIX_PARENT_TEMPLATES);
            return new ZabbixTemplateMapper().mapFromJson(parentTemplates);
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
    public boolean updateHostStatus(String hostid, int status) {
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("host.update")
                .paramEntry("hostid", hostid)
                .paramEntry("status", status)
                .build();
        try {
            JsonNode jsonNode = zabbixHandler.api(request);
            String id = new ZabbixIdsMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT).get("hostids")).get(0);
            if (!StringUtils.isEmpty(id))
                return true;
        } catch (Exception e) {
        }
        return false;
    }

    @Override
    public boolean updateHostTemplates(String hostid, Map<String, String> templateMap) {
        if (templateMap.isEmpty()) return false;
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("host.update")
                .paramEntry("hostid", hostid)
                .paramEntrySkipEmpty("templates", getTemplateIds(templateMap))
                .build();
        try {
            JsonNode jsonNode = zabbixHandler.api(request);
            String id = new ZabbixIdsMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT).get("hostids")).get(0);
            if (!StringUtils.isEmpty(id))
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 只清理模版
     *
     * @param hostid
     * @param clearTemplateMap
     */
    @Override
    public boolean clearHostTemplates(String hostid, Map<String, String> clearTemplateMap) {
        if (clearTemplateMap.isEmpty()) return false;
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("host.update")
                .paramEntry("hostid", hostid)
                .paramEntrySkipEmpty("templates_clear", getTemplateIds(clearTemplateMap))
                .build();
        try {
            JsonNode jsonNode = zabbixHandler.api(request);
            String id = new ZabbixIdsMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT).get("hostids")).get(0);
            if (!StringUtils.isEmpty(id))
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateHostMacros(String hostid, HashMap<String, String> macrosMap) {
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("host.update")
                .paramEntry("hostid", hostid)
                .paramEntry("macros", getMacros(macrosMap))
                .build();
        try {
            JsonNode jsonNode = zabbixHandler.api(request);
            String id = new ZabbixIdsMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT).get("hostids")).get(0);
            if (!StringUtils.isEmpty(id))
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private List<Map<String, String>> getMacros(HashMap<String, String> macrosMap) {
        List<Map<String, String>> macros = Lists.newArrayList();
        for (String key : macrosMap.keySet()) {
            Map<String, String> macro = Maps.newHashMap();
            macro.put("macro", key);
            macro.put("value", macrosMap.get(key));
            macros.add(macro);
        }
        return macros;
    }

    private List<String> getTemplateIds(Map<String, String> templateMap) {
        List<String> templateIds = Lists.newArrayList();
        if (templateMap.isEmpty()) return templateIds;
        for (String key : templateMap.keySet())
            templateIds.add(templateMap.get(key));
        return templateIds;
    }

}
