package com.baiyi.opscloud.zabbix.server.impl;

import com.baiyi.opscloud.common.config.CachingConfig;
import com.baiyi.opscloud.common.util.ZabbixUtils;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.zabbix.api.HostAPI;
import com.baiyi.opscloud.zabbix.builder.ZabbixFilterBuilder;
import com.baiyi.opscloud.zabbix.builder.ZabbixRequestBuilder;
import com.baiyi.opscloud.zabbix.entry.*;
import com.baiyi.opscloud.zabbix.handler.ZabbixHandler;
import com.baiyi.opscloud.zabbix.http.ZabbixBaseRequest;
import com.baiyi.opscloud.zabbix.mapper.*;
import com.baiyi.opscloud.zabbix.param.ZabbixDefaultParam;
import com.baiyi.opscloud.zabbix.param.ZabbixFilter;
import com.baiyi.opscloud.zabbix.server.ZabbixHostServer;
import com.baiyi.opscloud.zabbix.server.ZabbixServer;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/1/8 1:10 下午
 * @Version 1.0
 */
@Slf4j
@Component("ZabbixHostServer")
public class ZabbixHostServerImpl implements ZabbixHostServer {

    public static final int HOST_STATUS_ENABLE = 0;
    public static final int HOST_STATUS_DISABLE = 1;

    public static final String ZABBIX_MACROS = "macros";

    @Resource
    private ZabbixHandler zabbixHandler;

    @Resource
    private ZabbixServer zabbixServer;


    @CacheEvict(cacheNames = CachingConfig.CACHE_NAME_ZABBIX_REPO, key = "'host_ip_' + #mgmtIp")
    @Override
    public void evictHost(String mgmtIp) {
    }


    @Override
    @Cacheable(cacheNames = CachingConfig.CACHE_NAME_ZABBIX_REPO, key = "'host_ip_' + #mgmtIp")
    public ZabbixHost getHost(String mgmtIp) {
        ZabbixFilter filter = ZabbixFilterBuilder.newBuilder()
                .putEntry("ip", mgmtIp)
                .build();

        ZabbixBaseRequest request = ZabbixRequestBuilder.newBuilder()
                .method(HostAPI.GET)
                .paramEntryByFilter(filter)
                .build();
        JsonNode jsonNode = null;
        try {
            jsonNode = zabbixHandler.api(request);
            List<ZabbixHost> hosts = new ZabbixHostMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT));
            if (CollectionUtils.isEmpty(hosts)) {
                return new ZabbixHost();
            } else {
                return hosts.get(0);
            }
        } catch (Exception ignored) {
        }
        if (jsonNode != null) {
            try {
                ZabbixError zabbixError = new ZabbixErrorMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_ERROR));
                log.error("查询Zabbix主机错误! ip = {}, message = {}", mgmtIp, zabbixError.toString());
            } catch (Exception ignored) {
            }
        }
        return new ZabbixHost();
    }

    @Override
    public List<ZabbixTemplate> getHostTemplates(String hostid) {
        ZabbixBaseRequest request = ZabbixRequestBuilder.newBuilder()
                .method(HostAPI.GET)
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
        return Collections.emptyList();
    }

    @Override
    public List<ZabbixMacro> getHostMacros(String hostid) {
        ZabbixBaseRequest request = ZabbixRequestBuilder.newBuilder()
                .method(HostAPI.GET)
                .paramEntry("output", new String[]{"hostid"})
                .paramEntry("selectMacros", "extend")
                .paramEntry("hostids", hostid)
                .build();
        try {
            JsonNode jsonNode = zabbixHandler.api(request);
            JsonNode result = jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT);
            JsonNode macros = result.get(0).get(ZABBIX_MACROS);
            return new ZabbixMacroMapper().mapFromJson(macros);
        } catch (Exception ignored) {
        }
        return Collections.emptyList();
    }

    @Override
    public List<ZabbixHost> getHostList() {
        try {
            ZabbixBaseRequest request = ZabbixRequestBuilder.newBuilder()
                    .method(HostAPI.GET)
                    .build();
            JsonNode jsonNode = zabbixHandler.api(request);
            return new ZabbixHostMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT));
        } catch (Exception e) {
        }
        return Collections.emptyList();
    }

    @Override
    public List<ZabbixHostInterface> getHostInterfaces(String hostid) {
        ZabbixBaseRequest request = ZabbixRequestBuilder.newBuilder()
                .method(HostAPI.INTERFACE_GET)
                .paramEntry("output", "extend")
                .paramEntry("hostids", hostid)
                .build();
        try {
            JsonNode jsonNode = zabbixHandler.api(request);
            return new ZabbixHostInterfaceMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public ZabbixHost getHostByHostid(String hostid) {
        ZabbixBaseRequest request = ZabbixRequestBuilder.newBuilder()
                .method(HostAPI.GET)
                .paramEntry("hostids", hostid)
                .build();
        try {
            JsonNode jsonNode = zabbixHandler.api(request);
            return new ZabbixHostMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT)).get(0);
        } catch (Exception ignored) {
        }
        return null;
    }

    @Override
    public BusinessWrapper<Boolean> updateHostStatus(String hostid, int status) {
        ZabbixBaseRequest request = ZabbixRequestBuilder.newBuilder()
                .method(HostAPI.UPDATE)
                .paramEntry("hostid", hostid)
                .paramEntry("status", status)
                .build();
        JsonNode jsonNode = null;
        try {
            jsonNode = zabbixHandler.api(request);
            String id = new ZabbixIdsMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT).get("hostids")).get(0);
            if (!StringUtils.isEmpty(id))
                return BusinessWrapper.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zabbixServer.result(jsonNode);
    }

    @Override
    public BusinessWrapper<Boolean> massUpdateHostStatus(List<String> hostids, int status) {
        ZabbixBaseRequest request = ZabbixRequestBuilder.newBuilder()
                .method(HostAPI.MASSUPDATE)
                .paramEntry("hosts", ZabbixUtils.convertHosts(hostids))
                .paramEntry("status", status)
                .build();
        JsonNode jsonNode = null;
        try {
            jsonNode = zabbixHandler.api(request);
            List<String> ids = new ZabbixIdsMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT).get("hostids"));
            if (ids != null && ids.size() == hostids.size()) {
                return BusinessWrapper.SUCCESS;
            } else {
                return new BusinessWrapper<>(ErrorEnum.ZABBIX_MASSUPDATE_HOST_STATUS_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zabbixServer.result(jsonNode);
    }

    @Override
    public BusinessWrapper<Boolean> updateHostTemplates(String hostid, Map<String, String> templateMap) {
        if (templateMap.isEmpty()) return BusinessWrapper.SUCCESS;
        ZabbixBaseRequest request = ZabbixRequestBuilder.newBuilder()
                .method(HostAPI.UPDATE)
                .paramEntry("hostid", hostid)
                .paramEntrySkipEmpty("templates", getTemplateIds(templateMap))
                .build();
        JsonNode jsonNode = null;
        try {
            jsonNode = zabbixHandler.api(request);
            String id = new ZabbixIdsMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT).get("hostids")).get(0);
            if (!StringUtils.isEmpty(id))
                return BusinessWrapper.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zabbixServer.result(jsonNode);
    }

    @Override
    public boolean updateHostTags(String hostid, List<ZabbixDefaultParam> tags) {
        ZabbixBaseRequest request = ZabbixRequestBuilder.newBuilder()
                .method(HostAPI.UPDATE)
                .paramEntry("hostid", hostid)
                .paramEntry("tags", tags)
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
        ZabbixBaseRequest request = ZabbixRequestBuilder.newBuilder()
                .method(HostAPI.UPDATE)
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
    public boolean updateHostMacros(String hostid, String macros) {
        ZabbixBaseRequest request = ZabbixRequestBuilder.newBuilder()
                .method(HostAPI.UPDATE)
                .paramEntry("hostid", hostid)
                .paramEntry("macros", getMacros(macros))
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

    private ArrayList<ZabbixMacro> getMacros(String macros) {
        return new GsonBuilder().create().fromJson(macros, new TypeToken<ArrayList<ZabbixMacro>>() {
        }.getType());
    }

    private List<Map<String, String>> getTemplateIds(Map<String, String> templateMap) {
        List<Map<String, String>> templateIds = Lists.newArrayList();
        if (templateMap.isEmpty()) return templateIds;
        templateMap.forEach((k, v) -> {
            Map<String, String> id = Maps.newHashMap();
            id.put("templateid", v);
            templateIds.add(id);
        });
        return templateIds;
    }

}
