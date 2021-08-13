package com.baiyi.opscloud.zabbix.handler;

import com.baiyi.opscloud.common.datasource.config.DsZabbixConfig;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.util.ServerUtil;
import com.baiyi.opscloud.zabbix.entry.ZabbixHost;
import com.baiyi.opscloud.zabbix.entry.ZabbixHostGroup;
import com.baiyi.opscloud.zabbix.entry.ZabbixTemplate;
import com.baiyi.opscloud.zabbix.entry.ZabbixTrigger;
import com.baiyi.opscloud.zabbix.handler.base.BaseZabbixHandler;
import com.baiyi.opscloud.zabbix.http.*;
import com.baiyi.opscloud.zabbix.mapper.ZabbixMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.baiyi.opscloud.zabbix.handler.base.ZabbixServer.ApiConstant.*;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/1 2:07 下午
 * @Since 1.0
 */

@Component
public class ZabbixHostHandler extends BaseZabbixHandler<ZabbixHost> {

    private interface HostAPIMethod {
        String GET = "host.get";
        String CREATE = "host.create";
        String UPDATE = "host.update";
        String DELETE = "host.delete";
    }

    private SimpleZabbixRequestBuilder queryRequestBuilder() {
        return SimpleZabbixRequestBuilder.builder()
                .method(HostAPIMethod.GET)
                .paramEntry("selectInterfaces", "extend");
    }

    public List<ZabbixHost> list(DsZabbixConfig.Zabbix zabbix) {
        SimpleZabbixRequest request = queryRequestBuilder().build();
        JsonNode data = call(zabbix, request);
        return mapperList(data.get(RESULT), ZabbixHost.class);
    }

    public List<ZabbixHost> listByGroup(DsZabbixConfig.Zabbix zabbix, ZabbixHostGroup group) {
        SimpleZabbixRequest request = queryRequestBuilder()
                .paramEntry(HOST_GROUP_IDS, group.getGroupid())
                .build();
        JsonNode data = call(zabbix, request);
        return mapperList(data.get(RESULT), ZabbixHost.class);
    }

    public List<ZabbixHost> listByTemplate(DsZabbixConfig.Zabbix zabbix, ZabbixTemplate template) {
        SimpleZabbixRequest request = queryRequestBuilder()
                .paramEntry(TEMPLATE_IDS, template.getTemplateid())
                .build();
        JsonNode data = call(zabbix, request);
        return mapperList(data.get(RESULT), ZabbixHost.class);
    }

    public List<ZabbixHost> listByTrigger(DsZabbixConfig.Zabbix zabbix, ZabbixTrigger trigger) {
        SimpleZabbixRequest request = queryRequestBuilder()
                .paramEntry(TRIGGER_IDS, trigger.getTriggerid())
                .build();
        JsonNode data = call(zabbix, request);
        return mapperList(data.get(RESULT), ZabbixHost.class);
    }

    public ZabbixHost getById(DsZabbixConfig.Zabbix zabbix, String hostId) {
        SimpleZabbixRequest request = queryRequestBuilder()
                .paramEntry(HOST_IDS, hostId)
                .build();
        JsonNode data = call(zabbix, request);
        return mapperListGetOne(data.get(RESULT), ZabbixHost.class);
    }

    public List<ZabbixHost> listByNames(DsZabbixConfig.Zabbix zabbix, List<String> names) {
        ZabbixFilter filter = ZabbixFilterBuilder.builder()
                .putEntry("host", names)
                .build();
        JsonNode data = queryHostByFilter(zabbix, filter);
        return mapperList(data.get(RESULT), ZabbixHost.class);
    }

    public ZabbixHost getByName(DsZabbixConfig.Zabbix zabbix, String name) {
        ZabbixFilter filter = ZabbixFilterBuilder.builder()
                .putEntry("host", name)
                .build();
        JsonNode data = queryHostByFilter(zabbix, filter);
        return mapperOne(data.get(RESULT), ZabbixHost.class);
    }

    private JsonNode queryHostByFilter(DsZabbixConfig.Zabbix zabbix, ZabbixFilter filter) {
        SimpleZabbixRequest request = queryRequestBuilder()
                .filter(filter)
                .build();
        return call(zabbix, request);
    }

    public String createHost(DsZabbixConfig.Zabbix zabbix, Server server, Map<String, Object> paramMap) {
        ZabbixHost host = getByName(zabbix, ServerUtil.toServerName(server));
        if (host != null) {
            return host.getHostid();
        }
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(HostAPIMethod.CREATE)
                .paramEntry("host", ServerUtil.toServerName(server))
                .paramEntry(paramMap)
                .build();
        JsonNode data = call(zabbix, request);
        if (data.get(RESULT).get(HOST_IDS).isEmpty()) {
            throw new RuntimeException("ZabbixHost创建失败");
        }
        return ZabbixMapper.mapperList(data.get(RESULT).get(HOST_IDS), String.class).get(0);
    }

    public String updateHost(DsZabbixConfig.Zabbix zabbix, Server server, String hostId, Map<String, Object> paramMap) {
        ZabbixHost host = getById(zabbix, hostId);
        if (host == null) {
            return createHost(zabbix, server, paramMap);
        }
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(HostAPIMethod.UPDATE)
                .paramEntry(HOST_ID, hostId)
                .paramEntry("name", ServerUtil.toServerName(server))
                .paramEntry("host", ServerUtil.toServerName(server))
                .paramEntry(paramMap)
                .build();
        JsonNode data = call(zabbix, request);
        if (data.get(RESULT).get(HOST_IDS).isEmpty()) {
            throw new RuntimeException("ZabbixHost更新失败");
        }
        return ZabbixMapper.mapperList(data.get(RESULT).get(HOST_IDS), String.class).get(0);
    }

    public boolean deleteById(DsZabbixConfig.Zabbix zabbix, String hostId) {
        ZabbixHost host = getById(zabbix, hostId);
        if (host == null) {
            return true;
        }
        return delete(zabbix, host);
    }

    public boolean deleteByName(DsZabbixConfig.Zabbix zabbix, String name) {
        ZabbixHost host = getByName(zabbix, name);
        if (host == null) {
            return true;
        }
        return delete(zabbix, host);
    }

    private boolean delete(DsZabbixConfig.Zabbix zabbix, ZabbixHost host) {
        ZabbixDeleteRequest request = ZabbixDeleteRequest.builder()
                .method(HostAPIMethod.DELETE)
                .params(new String[]{host.getHostid()})
                .build();
        JsonNode data = call(zabbix, request);
        return host.getHostid().equals(ZabbixMapper.mapperList(data.get(RESULT).get(HOST_IDS), String.class).get(0));
    }

}
