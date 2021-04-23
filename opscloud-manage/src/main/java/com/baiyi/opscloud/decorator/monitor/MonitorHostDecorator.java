package com.baiyi.opscloud.decorator.monitor;

import com.baiyi.opscloud.common.base.BusinessType;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.decorator.server.ServerGroupDecorator;
import com.baiyi.opscloud.domain.generator.opscloud.OcEnv;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroup;
import com.baiyi.opscloud.domain.param.tag.TagParam;
import com.baiyi.opscloud.domain.vo.env.EnvVO;
import com.baiyi.opscloud.domain.vo.monitor.MonitorVO;
import com.baiyi.opscloud.domain.vo.server.ServerGroupVO;
import com.baiyi.opscloud.facade.TagFacade;
import com.baiyi.opscloud.server.facade.ServerAttributeFacade;
import com.baiyi.opscloud.service.env.OcEnvService;
import com.baiyi.opscloud.service.server.OcServerGroupService;
import com.baiyi.opscloud.service.server.OcServerService;
import com.baiyi.opscloud.zabbix.entry.ZabbixHost;
import com.baiyi.opscloud.zabbix.entry.ZabbixMacro;
import com.baiyi.opscloud.zabbix.entry.ZabbixTemplate;
import com.baiyi.opscloud.zabbix.server.ZabbixHostServer;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/11/24 10:54 上午
 * @Version 1.0
 */
@Component("MonitorHostDecorator")
public class MonitorHostDecorator {

    @Resource
    private OcEnvService ocEnvService;

    @Resource
    private OcServerGroupService ocServerGroupService;

    @Resource
    private TagFacade tagFacade;

    @Resource
    private ServerGroupDecorator serverGroupDecorator;

    @Resource
    private ServerAttributeFacade serverAttributeFacade;

    @Resource
    private OcServerService ocServerService;

    @Resource
    private ZabbixHostServer zabbixHostServer;

    public MonitorVO.Host decorator(MonitorVO.Host host) {
        // 装饰 环境信息
        OcEnv ocEnv = ocEnvService.queryOcEnvByType(host.getEnvType());
        if (ocEnv != null) {
            EnvVO.Env env = BeanCopierUtils.copyProperties(ocEnv, EnvVO.Env.class);
            host.setEnv(env);
        }
        // 装饰 服务器组信息
        OcServerGroup ocServerGroup = ocServerGroupService.queryOcServerGroupById(host.getServerGroupId());
        if (ocServerGroup != null) {
            ServerGroupVO.ServerGroup serverGroup = BeanCopierUtils.copyProperties(ocServerGroup, ServerGroupVO.ServerGroup.class);
            host.setServerGroup(serverGroupDecorator.decorator(serverGroup));
        }
        // 装饰 标签
        TagParam.BusinessQuery businessQuery = new TagParam.BusinessQuery();
        businessQuery.setBusinessType(BusinessType.SERVER.getType());
        businessQuery.setBusinessId(host.getId());
        host.setTags(tagFacade.queryBusinessTag(businessQuery));

        ZabbixHost zabbixHost = zabbixHostServer.getHost(host.getPrivateIp());
        if (!zabbixHost.isEmpty()) {
            OcServer ocServer = ocServerService.queryOcServerById(host.getId());
            Map<String, String> attrMap = serverAttributeFacade.getServerAttributeMap(ocServer);
            host.setAgentAvailable(Integer.valueOf(zabbixHost.getAvailable()));
            // 装饰模版
            invokeTemplates(host, zabbixHost, attrMap);
            // 装饰宏
            invokeMacros(host, zabbixHost, attrMap);
        }
        return host;
    }

    private void invokeTemplates(MonitorVO.Host host, ZabbixHost zabbixHost, Map<String, String> attrMap) {
        if (host.getMonitorStatus().equals(-1)) return;
        if (!attrMap.containsKey("zabbix_templates")) return;
        List<MonitorVO.Template> templates = Lists.newArrayList();
        Map<String, ZabbixTemplate> templateMap = acqHostTemplateMap(zabbixHost);
        Splitter.on(",").split(attrMap.get("zabbix_templates")).forEach(e -> {
            MonitorVO.Template tpl = new MonitorVO.Template();
            tpl.setIsActive(templateMap.containsKey(e));
            tpl.setName(e);
            templates.add(tpl);
        });
        host.setTemplates(templates);
    }

    private void invokeMacros(MonitorVO.Host host, ZabbixHost zabbixHost, Map<String, String> attrMap) {
        if (!attrMap.containsKey("zabbix_host_macros")) return;
        Map<String, ZabbixMacro> hostMacroMap = getZabbixHostMacroMap(zabbixHost);
        List<MonitorVO.Macro> macros =
                getLocalMacros(attrMap).stream().map(e -> buildMacro(hostMacroMap, e)).collect(Collectors.toList());
        host.setMacros(macros);
    }

    private MonitorVO.Macro buildMacro(Map<String, ZabbixMacro> hostMacroMap, ZabbixMacro zabbixMacro) {
        MonitorVO.Macro macro = new MonitorVO.Macro();
        macro.setIsActive(hostMacroMap.containsKey(zabbixMacro.getMacro())
                && zabbixMacro.getValue().equals(hostMacroMap.get(zabbixMacro.getMacro()).getValue()));
        macro.setMacro(zabbixMacro.getMacro());
        macro.setValue(zabbixMacro.getValue());
        return macro;
    }

    private List<ZabbixMacro> getLocalMacros(Map<String, String> attrMap) {
        String str = attrMap.get("zabbix_host_macros");
        if (StringUtils.isEmpty(str)) return Collections.emptyList();
        return new GsonBuilder().create().fromJson(str, new TypeToken<ArrayList<ZabbixMacro>>() {
        }.getType());
    }

    private Map<String, ZabbixMacro> getZabbixHostMacroMap(ZabbixHost zabbixHost) {
        List<ZabbixMacro> macros = zabbixHostServer.getHostMacros(zabbixHost.getHostid());
        return macros.stream().collect(Collectors.toMap(ZabbixMacro::getMacro, a -> a, (k1, k2) -> k1));
    }

    private Map<String, ZabbixTemplate> acqHostTemplateMap(ZabbixHost zabbixHost) {
        List<ZabbixTemplate> templates = zabbixHostServer.getHostTemplates(zabbixHost.getHostid());
        if (CollectionUtils.isEmpty(templates)) return Maps.newHashMap();
        return templates.stream().collect(Collectors.toMap(ZabbixTemplate::getName, a -> a, (k1, k2) -> k1));
    }

}
