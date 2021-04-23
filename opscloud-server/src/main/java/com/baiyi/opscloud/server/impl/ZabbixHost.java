package com.baiyi.opscloud.server.impl;

import com.baiyi.opscloud.common.base.Global;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.server.IServer;
import com.baiyi.opscloud.server.bo.ZabbixEventBO;
import com.baiyi.opscloud.server.event.handler.ZabbixUpdateHostEventHandler;
import com.baiyi.opscloud.server.utils.ZabbixUtils;
import com.baiyi.opscloud.zabbix.api.HostAPI;
import com.baiyi.opscloud.zabbix.builder.ZabbixDefaultParamBuilder;
import com.baiyi.opscloud.zabbix.entry.ZabbixHostgroup;
import com.baiyi.opscloud.zabbix.entry.ZabbixProxy;
import com.baiyi.opscloud.zabbix.entry.ZabbixTemplate;
import com.baiyi.opscloud.zabbix.handler.ZabbixHandler;
import com.baiyi.opscloud.zabbix.http.ZabbixBaseRequest;
import com.baiyi.opscloud.zabbix.builder.ZabbixRequestBuilder;
import com.baiyi.opscloud.zabbix.param.ZabbixDefaultParam;
import com.baiyi.opscloud.zabbix.param.ZabbixRequestParams;
import com.baiyi.opscloud.zabbix.mapper.ZabbixIdsMapper;
import com.baiyi.opscloud.zabbix.server.ZabbixHostServer;
import com.baiyi.opscloud.zabbix.server.ZabbixHostgroupServer;
import com.baiyi.opscloud.zabbix.server.ZabbixServer;
import com.baiyi.opscloud.zabbix.server.impl.ZabbixServerImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static com.baiyi.opscloud.zabbix.server.impl.ZabbixHostServerImpl.HOST_STATUS_DISABLE;
import static com.baiyi.opscloud.zabbix.server.impl.ZabbixHostServerImpl.HOST_STATUS_ENABLE;

/**
 * @Author baiyi
 * @Date 2020/4/1 7:19 下午
 * @Version 1.0
 */
@Slf4j
@Component("ZabbixHost")
public class ZabbixHost extends BaseServer implements IServer {

    @Resource
    private ZabbixHostServer zabbixHostServer;

    @Resource
    private ZabbixServer zabbixServer;

    @Resource
    private ZabbixHandler zabbixHandler;

    @Resource
    private ZabbixHostgroupServer zabbixHostgroupServer;

    @Resource
    private ZabbixUpdateHostEventHandler zabbixUpdateHostEventHandler;

    @Override
    public void sync() {
    }

    @Override
    public BusinessWrapper<Boolean> create(OcServer ocServer) {
        String mgmtIp = getManageIp(ocServer);
        com.baiyi.opscloud.zabbix.entry.ZabbixHost host = zabbixHostServer.getHost(mgmtIp);
        if (!host.isEmpty())
            return update(host, ocServer);
        else
            zabbixHostServer.evictHost(mgmtIp);
        ZabbixHostgroup zabbixHostgroup = zabbixHostgroupServer.createHostgroup(getServerGroupName(ocServer));
        if (zabbixHostgroup == null) return new BusinessWrapper<>(ErrorEnum.ZABBIX_HOSTGROUP_NOT_EXIST);
        Map<String, String> serverAttributeMap = getServerAttributeMap(ocServer);
        ZabbixBaseRequest request = ZabbixRequestBuilder.newBuilder()
                .method(HostAPI.CREATE)
                .paramEntry("host", getHostname(ocServer))
                .paramEntry("interfaces", ZabbixUtils.buildInterfacesParameter(getManageIp(ocServer)))
                .paramEntry("groups", ZabbixUtils.buildGroupsParameter(zabbixHostgroup))
                .paramEntry("templates", buildTemplatesParameter(serverAttributeMap))
                .paramEntry("tags", buildTagsParameter(ocServer))
                .paramEntrySkipEmpty("macros", ZabbixUtils.buildMacrosParameter(serverAttributeMap))
                .paramEntrySkipEmpty("proxy_hostid", getProxyhostid(serverAttributeMap))
                .build();
        JsonNode jsonNode = null;
        try {
            jsonNode = zabbixHandler.api(request);
            String hostid = new ZabbixIdsMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT).get("hostids")).get(0);
            if (!StringUtils.isEmpty(hostid)) {
                ocServer.setMonitorStatus(0);
                updateOcServer(ocServer);
                // 禁用主机监控
                disable(ocServer);
                return BusinessWrapper.SUCCESS;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zabbixServer.result(jsonNode);
    }

    @Override
    public BusinessWrapper<Boolean> remove(OcServer ocServer) {
        if (ocServer == null) return new BusinessWrapper<>(ErrorEnum.SERVER_NOT_EXIST);
        com.baiyi.opscloud.zabbix.entry.ZabbixHost host = zabbixHostServer.getHost(getManageIp(ocServer));
        if (host.isEmpty()) return BusinessWrapper.SUCCESS;
        ZabbixRequestParams request = ZabbixRequestParams.builder()
                .method(HostAPI.DELETE.getMethod())
                .params(new String[]{host.getHostid()})
                .build();

        JsonNode jsonNode = null;
        try {
            jsonNode = zabbixHandler.api(request);
            String hostid = new ZabbixIdsMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT).get("hostids")).get(0);
            if (!StringUtils.isEmpty(hostid))
                return BusinessWrapper.SUCCESS;
        } catch (Exception e) {
            log.error("remove fail", e);
        }
        return zabbixServer.result(jsonNode);
    }

    private String getProxyhostid(Map<String, String> serverAttributeMap) {
        if (!serverAttributeMap.containsKey(Global.SERVER_ATTRIBUTE_ZABBIX_PROXY))
            return null;
        String proxyHostname = serverAttributeMap.get(Global.SERVER_ATTRIBUTE_ZABBIX_PROXY);
        if (StringUtils.isEmpty(proxyHostname))
            return null;
        ZabbixProxy zabbixProxy = zabbixServer.getProxy(proxyHostname);
        if (zabbixProxy == null)
            return null;
        return zabbixProxy.getProxyid();
    }

    private List<Map<String, Object>> buildTemplatesParameter(Map<String, String> serverAttributeMap) {
        List<Map<String, Object>> templates = Lists.newArrayList();
        Map<String, String> templateMap = getTemplateMap(serverAttributeMap);
        templateMap.keySet().forEach(templateName -> {
            Map<String, Object> map = Maps.newHashMap();
            map.put("templateid", templateMap.get(templateName));
            templates.add(map);
        });
        return templates;
    }

    public List<ZabbixDefaultParam> buildTagsParameter(OcServer ocServer) {
        return Lists.newArrayList(ZabbixDefaultParamBuilder.newBuilder()
                .putEntry("tag", "env")
                .putEntry("value", acqEnvName(ocServer))
                .build());
    }

    /**
     * @param serverAttributeMap
     * @return templateName, templateid
     */
    public Map<String, String> getTemplateMap(Map<String, String> serverAttributeMap) {
        Map<String, String> templateMap = Maps.newHashMap();
        if (!serverAttributeMap.containsKey(Global.SERVER_ATTRIBUTE_ZABBIX_TEMPLATES))
            return templateMap;
        String templatesOpt = serverAttributeMap.get(Global.SERVER_ATTRIBUTE_ZABBIX_TEMPLATES);
        if (StringUtils.isEmpty(templatesOpt))
            return templateMap;
        Splitter.on(",").splitToList(templatesOpt).forEach(t -> {
            ZabbixTemplate zabbixTemplate = zabbixServer.getTemplate(t);
            if (zabbixTemplate != null)
                templateMap.put(t, zabbixTemplate.getTemplateid());
        });
        return templateMap;
    }

    @Override
    public BusinessWrapper<Boolean> disable(OcServer ocServer) {
        return updateHostStatus(ocServer, HOST_STATUS_DISABLE);
    }

    @Override
    public BusinessWrapper<Boolean> enable(OcServer ocServer) {
        return updateHostStatus(ocServer, HOST_STATUS_ENABLE);
    }

    /**
     * 更新状态（禁用/启用）
     *
     * @param ocServer
     * @param status
     * @return
     */
    private BusinessWrapper<Boolean> updateHostStatus(OcServer ocServer, int status) {
        com.baiyi.opscloud.zabbix.entry.ZabbixHost host = zabbixHostServer.getHost(getManageIp(ocServer));
        // 主机不存在
        if (host.isEmpty()) return BusinessWrapper.SUCCESS;
        BusinessWrapper<Boolean> wrapper = zabbixHostServer.updateHostStatus(host.getHostid(), status);
        if (wrapper.isSuccess()) {
            // 更新监控状态
            ocServer.setMonitorStatus(status);
            updateOcServer(ocServer);
        }
        return wrapper;
    }

    /**
     * 修改host名称
     *
     * @param host
     * @param hostname
     * @return
     */
    private BusinessWrapper<Boolean> updateHostName(com.baiyi.opscloud.zabbix.entry.ZabbixHost host, String hostname) {
        // 主机不存在
        if (StringUtils.isEmpty(host.getHostid()))
            return new BusinessWrapper<>(ErrorEnum.ZABBIX_HOST_NOT_EXIST);
        if (host.getHost().equals(hostname))
            return BusinessWrapper.SUCCESS;
        ZabbixBaseRequest request = ZabbixRequestBuilder.newBuilder()
                .method(HostAPI.UPDATE)
                .paramEntry("hostid", host.getHostid())
                .paramEntry("host", hostname)
                .build();
        JsonNode jsonNode = null;
        try {
            jsonNode = zabbixHandler.api(request);
            String hostid = new ZabbixIdsMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT).get("hostids")).get(0);
            if (!StringUtils.isEmpty(hostid))
                return BusinessWrapper.SUCCESS;
        } catch (Exception e) {
            log.error("updateHostName fail", e);
        }
        return zabbixServer.result(jsonNode);
    }

    private BusinessWrapper<Boolean> update(com.baiyi.opscloud.zabbix.entry.ZabbixHost host, OcServer ocServer) {
        if (StringUtils.isEmpty(host.getHostid())) {
            return create(ocServer);
        } else {
            // 更新主机名
            String hostname = getHostname(ocServer);
            if (!hostname.equals(host.getHost()))
                updateHostName(host, hostname);
        }
        ZabbixEventBO.HostUpdate hostUpdate = ZabbixEventBO.HostUpdate.builder()
                .host(host)
                .ocServer(ocServer)
                .build();
        zabbixUpdateHostEventHandler.eventPost(hostUpdate);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> update(OcServer ocServer) {
        return update(zabbixHostServer.getHost(getManageIp(ocServer)), ocServer);
    }


}
