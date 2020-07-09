package com.baiyi.opscloud.server.impl;

import com.baiyi.opscloud.common.base.Global;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.server.IServer;
import com.baiyi.opscloud.server.utils.ZabbixUtils;
import com.baiyi.opscloud.zabbix.entry.ZabbixHostgroup;
import com.baiyi.opscloud.zabbix.entry.ZabbixProxy;
import com.baiyi.opscloud.zabbix.entry.ZabbixTemplate;
import com.baiyi.opscloud.zabbix.handler.ZabbixHandler;
import com.baiyi.opscloud.zabbix.http.ZabbixRequest;
import com.baiyi.opscloud.zabbix.http.ZabbixRequestBuilder;
import com.baiyi.opscloud.zabbix.http.ZabbixRequestParamsIds;
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

    @Override
    public void sync() {
    }

    @Override
    public Boolean create(OcServer ocServer) {
        ZabbixHostgroup zabbixHostgroup = zabbixHostgroupServer.createHostgroup(getServerGroupName(ocServer));
        if (zabbixHostgroup == null) return Boolean.FALSE;
        Map<String, String> serverAttributeMap = getServerAttributeMap(ocServer);
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("host.create")
                .paramEntry("host", getHostname(ocServer))
                .paramEntry("interfaces", ZabbixUtils.buildInterfacesParameter(getManageIp(ocServer)))
                .paramEntry("groups", ZabbixUtils.buildGroupsParameter(zabbixHostgroup))
                .paramEntry("templates", buildTemplatesParameter(serverAttributeMap))
                .paramEntrySkipEmpty("macros", ZabbixUtils.buildMacrosParameter(serverAttributeMap))
                .paramEntrySkipEmpty("proxy_hostid", getProxyhostid(serverAttributeMap))
                .build();
        try {
            JsonNode jsonNode = zabbixHandler.api(request);
            String hostid = new ZabbixIdsMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT).get("hostids")).get(0);
            if (!StringUtils.isEmpty(hostid))
                return Boolean.TRUE;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Boolean.FALSE;
    }

    @Override
    public Boolean remove(OcServer ocServer) {
        if (ocServer == null) return Boolean.FALSE;
        com.baiyi.opscloud.zabbix.entry.ZabbixHost host = zabbixHostServer.getHost(getManageIp(ocServer));
        if (host == null) return Boolean.TRUE;
        String[] hostids = new String[]{host.getHostid()};
        ZabbixRequestParamsIds request = new ZabbixRequestParamsIds();
        request.setMethod("host.delete");
        request.setId(1);
        request.setParams(hostids);
        try {
            JsonNode jsonNode = zabbixHandler.api(request);
            String hostid = new ZabbixIdsMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT).get("hostids")).get(0);
            if (!StringUtils.isEmpty(hostid))
                return Boolean.TRUE;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Boolean.FALSE;
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
        for (String templateName : templateMap.keySet()) {
            Map<String, Object> map = Maps.newHashMap();
            map.put("templateid", templateMap.get(templateName));
            templates.add(map);
        }
        return templates;
    }

    /**
     * @param serverAttributeMap
     * @return templateName, templateid
     */
    private Map<String, String> getTemplateMap(Map<String, String> serverAttributeMap) {
        Map<String, String> map = Maps.newHashMap();
        if (!serverAttributeMap.containsKey(Global.SERVER_ATTRIBUTE_ZABBIX_TEMPLATES))
            return map;
        String templatesOpt = serverAttributeMap.get(Global.SERVER_ATTRIBUTE_ZABBIX_TEMPLATES);
        if (StringUtils.isEmpty(templatesOpt))
            return map;
        List<String> templateNameList = Splitter.on(",").splitToList(templatesOpt);
        for (String templateName : templateNameList) {
            ZabbixTemplate zabbixTemplate = zabbixServer.getTemplate(templateName);
            if (zabbixTemplate != null)
                map.put(templateName, zabbixTemplate.getTemplateid());
        }
        return map;
    }


    @Override
    public Boolean disable(OcServer ocServer) {
        return updateHostStatus(ocServer, HOST_STATUS_DISABLE);
    }

    @Override
    public Boolean enable(OcServer ocServer) {
        return updateHostStatus(ocServer, HOST_STATUS_ENABLE);
    }

    /**
     * 更新状态（禁用/启用）
     *
     * @param ocServer
     * @param status
     * @return
     */
    private Boolean updateHostStatus(OcServer ocServer, int status) {
        com.baiyi.opscloud.zabbix.entry.ZabbixHost host = zabbixHostServer.getHost(getManageIp(ocServer));
        // 主机不存在
        if (host == null) return Boolean.TRUE;
        return zabbixHostServer.updateHostStatus(host.getHostid(), status);
    }

    /**
     * 修改host名称
     *
     * @param host
     * @param hostname
     * @return
     */
    private Boolean updateHostName(com.baiyi.opscloud.zabbix.entry.ZabbixHost host, String hostname) {
        // 主机不存在
        if (host == null) return Boolean.FALSE;
        if (host.getHost().equals(hostname))
            return Boolean.TRUE;
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("host.update")
                .paramEntry("hostid", host.getHostid())
                .paramEntry("host", hostname)
                .build();
        try {
            JsonNode jsonNode = zabbixHandler.api(request);
            String hostid = new ZabbixIdsMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT).get("hostids")).get(0);
            if (!StringUtils.isEmpty(hostid))
                return Boolean.TRUE;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Boolean.FALSE;
    }

    /**
     * 更新host 模版
     *
     * @param host
     * @param serverAttributeMap
     */
    public void updateHostTemplates(com.baiyi.opscloud.zabbix.entry.ZabbixHost host, Map<String, String> serverAttributeMap) {
        // 当前host链接的模版
        List<ZabbixTemplate> templates = zabbixHostServer.getHostTemplates(host.getHostid());
        if (!serverAttributeMap.containsKey(Global.SERVER_ATTRIBUTE_ZABBIX_TEMPLATES))
            return;
        // 配置的模版
        Map<String, String> templateMap = getTemplateMap(serverAttributeMap);
        // 合并的模版(主机当前的模版+服务器配置的模版集合)
        Map<String, String> mergeTemplateMap = getMergeTemplate(templates, templateMap);
        // 更新模版（追加）
        zabbixHostServer.updateHostTemplates(host.getHostid(), mergeTemplateMap);

        if (!serverAttributeMap.containsKey(Global.SERVER_ATTRIBUTE_ZABBIX_BIDIRECTIONAL_SYNC))
            return;
        String isBidirectionalSync = serverAttributeMap.get(Global.SERVER_ATTRIBUTE_ZABBIX_BIDIRECTIONAL_SYNC);
        // 不执行清理模版
        if (!isBidirectionalSync.equalsIgnoreCase("true"))
            return;
        // 清理模版
        zabbixHostServer.clearHostTemplates(host.getHostid(), getClearTemplate(templates, mergeTemplateMap));
    }

    private Map<String, String> getClearTemplate(List<ZabbixTemplate> templates, Map<String, String> mergeTemplateMap) {
        Map<String, String> map = Maps.newHashMap(mergeTemplateMap);
        for (ZabbixTemplate template : templates)
            map.put(template.getName(), template.getTemplateid());
        return map;
    }

    /**
     * 合并模版
     *
     * @param templates
     * @param templateMap
     * @return
     */
    private Map<String, String> getMergeTemplate(List<ZabbixTemplate> templates, Map<String, String> templateMap) {
        Map<String, String> map = Maps.newHashMap(templateMap);
        for (ZabbixTemplate template : templates)
            map.put(template.getName(), template.getTemplateid());
        return map;
    }

    @Override
    public Boolean update(OcServer ocServer) {
        com.baiyi.opscloud.zabbix.entry.ZabbixHost host = zabbixHostServer.getHost(getManageIp(ocServer));
        if (host == null) {
            return create(ocServer);
        } else {
            // 更新主机名
            String hostname = getHostname(ocServer);
            if (!hostname.equals(host.getHost()))
                updateHostName(host, hostname);
        }
        // 更新模版
        updateHostTemplates(host, getServerAttributeMap(ocServer));
        return Boolean.TRUE;
    }


}
