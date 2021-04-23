package com.baiyi.opscloud.server.event.listener;

import com.baiyi.opscloud.common.base.Global;
import com.baiyi.opscloud.server.bo.ZabbixEventBO;
import com.baiyi.opscloud.server.facade.ServerAttributeFacade;
import com.baiyi.opscloud.server.impl.ZabbixHost;
import com.baiyi.opscloud.zabbix.entry.ZabbixTemplate;
import com.baiyi.opscloud.zabbix.param.ZabbixDefaultParam;
import com.baiyi.opscloud.zabbix.server.ZabbixHostServer;
import com.google.common.collect.Maps;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/20 4:00 下午
 * @Since 1.0
 */

@Slf4j
@Component
public class ZabbixUpdateHostEventListener {

    @Resource
    private ZabbixHost zabbixHost;

    @Resource
    private ZabbixHostServer zabbixHostServer;

    @Resource
    private ServerAttributeFacade serverAttributeFacade;

    @Subscribe
    public void updateHostTemplates(ZabbixEventBO.HostUpdate host) {
        log.info("更新Zabbix主机模板");
        Map<String, String> serverAttributeMap = serverAttributeFacade.getServerAttributeMap(host.getOcServer());
        com.baiyi.opscloud.zabbix.entry.ZabbixHost zabbixHostEntry = host.getHost();
        List<ZabbixTemplate> templates = zabbixHostServer.getHostTemplates(zabbixHostEntry.getHostid());
        if (!serverAttributeMap.containsKey(Global.SERVER_ATTRIBUTE_ZABBIX_TEMPLATES))
            return;
        /**
         * 配置的模版 key:模版名称 value:模版id
         */
        Map<String, String> templateMap = zabbixHost.getTemplateMap(serverAttributeMap);
        // 合并的模版(主机当前的模版+服务器配置的模版集合)
        Map<String, String> mergeTemplateMap = getMergeTemplate(templates, templateMap);
        // 更新模版（追加）
        zabbixHostServer.updateHostTemplates(zabbixHostEntry.getHostid(), mergeTemplateMap);
        if (!serverAttributeMap.containsKey(Global.SERVER_ATTRIBUTE_ZABBIX_BIDIRECTIONAL_SYNC))
            return;
        String isBidirectionalSync = serverAttributeMap.get(Global.SERVER_ATTRIBUTE_ZABBIX_BIDIRECTIONAL_SYNC);
        // 不执行清理模版
        if (!isBidirectionalSync.equalsIgnoreCase("true"))
            return;
        // 清理模版
        zabbixHostServer.clearHostTemplates(zabbixHostEntry.getHostid(), getClearTemplate(templates, mergeTemplateMap));
    }

    private Map<String, String> getMergeTemplate(List<ZabbixTemplate> templates, Map<String, String> templateMap) {
        Map<String, String> map = Maps.newHashMap(templateMap);
        templates.forEach(t -> map.put(t.getName(), t.getTemplateid()));
        return map;
    }

    private Map<String, String> getClearTemplate(List<ZabbixTemplate> templates, Map<String, String> mergeTemplateMap) {
        Map<String, String> map = Maps.newHashMap(mergeTemplateMap);
        templates.forEach(t -> map.put(t.getName(), t.getTemplateid()));
        return map;
    }


    @Subscribe
    public void updateHostTags(ZabbixEventBO.HostUpdate host) {
        log.info("更新Zabbix主机标签");
        List<ZabbixDefaultParam> tags = zabbixHost.buildTagsParameter(host.getOcServer());
        zabbixHostServer.updateHostTags(host.getHost().getHostid(), tags);
    }

    @Subscribe
    public void updateHostMacros(ZabbixEventBO.HostUpdate host) {
        log.info("更新Zabbix主机宏");
        Map<String, String> attrMap = serverAttributeFacade.getServerAttributeMap(host.getOcServer());
        String macros = attrMap.get("zabbix_host_macros");
        if (StringUtils.isEmpty(macros)) return;
        zabbixHostServer.updateHostMacros(host.getHost().getHostid(), macros);
    }
}
