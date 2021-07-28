package com.baiyi.opscloud.zabbix.handler;

import com.baiyi.opscloud.common.datasource.config.DsZabbixConfig;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.util.ServerUtil;
import com.baiyi.opscloud.zabbix.entry.ZabbixHost;
import com.baiyi.opscloud.zabbix.entry.ZabbixHostGroup;
import com.baiyi.opscloud.zabbix.entry.ZabbixTemplate;
import com.baiyi.opscloud.zabbix.entry.ZabbixTrigger;
import com.baiyi.opscloud.zabbix.http.*;
import com.baiyi.opscloud.zabbix.mapper.ZabbixMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static com.baiyi.opscloud.zabbix.handler.ZabbixHandler.ApiConstant.*;

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
        String CREATE_HOST = "host.create";
        String UPDATE_HOST = "host.update";
        String DELETE_HOST = "host.delete";
    }

    public List<ZabbixHost> listHosts(DsZabbixConfig.Zabbix zabbix) {
        ZabbixCommonRequest request = ZabbixCommonRequestBuilder.builder()
                .method(Method.QUERY_HOST)
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        return ZabbixMapper.mapperList(data.get(RESULT), ZabbixHost.class);
    }

    public List<ZabbixHost> listHostsByGroup(DsZabbixConfig.Zabbix zabbix, ZabbixHostGroup group) {
        ZabbixCommonRequest request = ZabbixCommonRequestBuilder.builder()
                .method(Method.QUERY_HOST)
                .paramEntry(HOST_GROUP_IDS, group.getGroupId())
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        return ZabbixMapper.mapperList(data.get(RESULT), ZabbixHost.class);
    }

    public List<ZabbixHost> listHostsByTemplate(DsZabbixConfig.Zabbix zabbix, ZabbixTemplate template) {
        ZabbixCommonRequest request = ZabbixCommonRequestBuilder.builder()
                .method(Method.QUERY_HOST)
                .paramEntry(TEMPLATE_IDS, template.getTemplateId())
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        return ZabbixMapper.mapperList(data.get(RESULT), ZabbixHost.class);
    }

    public List<ZabbixHost> listHostsByTrigger(DsZabbixConfig.Zabbix zabbix, ZabbixTrigger trigger) {
        ZabbixCommonRequest request = ZabbixCommonRequestBuilder.builder()
                .method(Method.QUERY_HOST)
                .paramEntry(TRIGGER_IDS, trigger.getTriggerId())
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        return ZabbixMapper.mapperList(data.get(RESULT), ZabbixHost.class);
    }

    public ZabbixHost getHostById(DsZabbixConfig.Zabbix zabbix, String hostId) {
        ZabbixCommonRequest request = ZabbixCommonRequestBuilder.builder()
                .method(Method.QUERY_HOST)
                .paramEntry(HOST_IDS, hostId)
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        List<ZabbixHost> hosts = ZabbixMapper.mapperList(data.get(RESULT), ZabbixHost.class);
        if (CollectionUtils.isEmpty(hosts)) {
            return null;
        }
        return hosts.get(0);
    }

    public List<ZabbixHost> listHostByNames(DsZabbixConfig.Zabbix zabbix, List<String> names) {
        ZabbixFilter filter = ZabbixFilterBuilder.builder()
                .putEntry("host", names)
                .build();
        JsonNode data = queryHostByFilter(zabbix, filter);
        return ZabbixMapper.mapperList(data.get(RESULT), ZabbixHost.class);
    }

    public ZabbixHost getHostByName(DsZabbixConfig.Zabbix zabbix, String name) {
        ZabbixFilter filter = ZabbixFilterBuilder.builder()
                .putEntry("host", name)
                .build();
        JsonNode data = queryHostByFilter(zabbix, filter);
        return ZabbixMapper.mapper(data.get(RESULT), ZabbixHost.class);
    }

    private JsonNode queryHostByFilter(DsZabbixConfig.Zabbix zabbix, ZabbixFilter filter) {
        ZabbixCommonRequest request = ZabbixCommonRequestBuilder.builder()
                .method(Method.QUERY_HOST)
                .filter(filter)
                .build();
        return zabbixHandler.call(zabbix, request);
    }

    public String createHost(DsZabbixConfig.Zabbix zabbix, Server server, Map<String, Object> paramMap) {
        ZabbixHost host = getHostByName(zabbix, ServerUtil.toServerName(server));
        if (host != null) {
            return host.getHostId();
        }
        ZabbixCommonRequest request = ZabbixCommonRequestBuilder.builder()
                .method(Method.CREATE_HOST)
                .paramEntry("host", ServerUtil.toServerName(server))
                .paramEntry(paramMap)
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        if (data.get(RESULT).get(HOST_IDS).isEmpty()) {
            throw new RuntimeException("ZabbixHost创建失败");
        }
        return ZabbixMapper.mapperList(data.get(RESULT).get(HOST_IDS), String.class).get(0);
    }

    public String updateHost(DsZabbixConfig.Zabbix zabbix, Server server, String hostId, Map<String, Object> paramMap) {
        ZabbixHost host = getHostById(zabbix, hostId);
        if (host == null) {
            return createHost(zabbix, server, paramMap);
        }
        ZabbixCommonRequest request = ZabbixCommonRequestBuilder.builder()
                .method(Method.UPDATE_HOST)
                .paramEntry(HOST_ID, hostId)
                .paramEntry("name", ServerUtil.toServerName(server))
                .paramEntry("host", ServerUtil.toServerName(server))
                .paramEntry(paramMap)
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        if (data.get(RESULT).get(HOST_IDS).isEmpty()) {
            throw new RuntimeException("ZabbixHost更新失败");
        }
        return ZabbixMapper.mapperList(data.get(RESULT).get(HOST_IDS), String.class).get(0);
    }

    public Boolean deleteHostById(DsZabbixConfig.Zabbix zabbix, String hostId) {
        ZabbixHost host = getHostById(zabbix, hostId);
        if (host == null) {
            return true;
        }
        return deleteHost(zabbix, host);
    }

    public Boolean deleteHostByName(DsZabbixConfig.Zabbix zabbix, String name) {
        ZabbixHost host = getHostByName(zabbix, name);
        if (host == null) {
            return true;
        }
        return deleteHost(zabbix, host);
    }

    private Boolean deleteHost(DsZabbixConfig.Zabbix zabbix, ZabbixHost host) {
        ZabbixDeleteRequest request = ZabbixDeleteRequest.builder()
                .method(Method.DELETE_HOST)
                .params(new String[]{host.getHostId()})
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        return host.getHostId().equals(ZabbixMapper.mapperList(data.get(RESULT).get(HOST_IDS), String.class).get(0));
    }

}
