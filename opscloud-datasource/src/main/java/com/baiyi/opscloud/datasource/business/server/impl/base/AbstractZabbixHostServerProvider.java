package com.baiyi.opscloud.datasource.business.server.impl.base;

import com.baiyi.opscloud.common.datasource.ZabbixConfig;
import com.baiyi.opscloud.domain.base.BaseBusiness;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.Env;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.model.property.ServerProperty;
import com.baiyi.opscloud.facade.server.SimpleServerNameFacade;
import com.baiyi.opscloud.zabbix.helper.ZabbixGroupHelper;
import com.baiyi.opscloud.zabbix.v5.drive.ZabbixV5HostDrive;
import com.baiyi.opscloud.zabbix.v5.drive.ZabbixV5HostTagDrive;
import com.baiyi.opscloud.zabbix.v5.drive.ZabbixV5ProxyDrive;
import com.baiyi.opscloud.zabbix.v5.drive.ZabbixV5TemplateDrive;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixHost;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixHostGroup;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixProxy;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixTemplate;
import com.baiyi.opscloud.zabbix.v5.param.ZabbixHostParam;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/8/23 9:48 上午
 * @Version 1.0
 */
public abstract class AbstractZabbixHostServerProvider extends AbstractServerProvider<ZabbixConfig.Zabbix> {

    @Resource
    private ZabbixGroupHelper zabbixFacade;

    @Resource
    protected ZabbixV5HostDrive zabbixV5HostDrive;

    @Resource
    protected ZabbixV5HostTagDrive zabbixV5HostTagDrive;

    @Resource
    protected ZabbixV5ProxyDrive zabbixV5ProxyDrive;

    @Resource
    private ZabbixV5TemplateDrive zabbixV5TemplateDrive;

    protected static ThreadLocal<ZabbixConfig.Zabbix> configContext = new ThreadLocal<>();

    @Override
    protected void initialConfig(DatasourceConfig dsConfig) {
        configContext.set(dsConfigHelper.build(dsConfig, ZabbixConfig.class).getZabbix());
    }

    @Override
    protected void doGrant(User user, BaseBusiness.IBusiness businessResource) {
    }

    @Override
    protected void doRevoke(User user, BaseBusiness.IBusiness businessResource) {
    }

    protected void updateHost(Server server, ServerProperty.Server property, ZabbixHost.Host host) {
        String hostName = SimpleServerNameFacade.toServerName(server);
        // 更新主机名
        if (!hostName.equals(host.getName())) {
            zabbixV5HostDrive.updateHostName(configContext.get(), host, hostName);
            zabbixV5HostDrive.evictHostByIp(configContext.get(), getManageIp(server, property));
        }
        // 更新Templates
        updateHostTemplate(property, host);
        // 更新Tags
        updateHostTag(server, host);
        // zabbix-proxy
        ZabbixProxy.Proxy proxy = getProxy(property);
        if (proxy != null) {
            zabbixV5ProxyDrive.updateHostProxy(configContext.get(), host, proxy);
        }
    }

    /**
     * 更新主机标签
     *
     * @param server
     * @param host
     */
    private void updateHostTag(Server server, ZabbixHost.Host host) {
        ZabbixHost.Host hostTag = zabbixV5HostTagDrive.getHostTag(configContext.get(), host);
        Env env = getEnv(server);
        if (CollectionUtils.isEmpty(hostTag.getTags())) {
            updateHostTag(host, env);
        } else {
            Optional<ZabbixHost.HostTag> optionalHostTag = hostTag.getTags().stream().filter(e -> e.getTag().equals("env")).findFirst();
            if (optionalHostTag.isPresent() && env.getEnvName().equals(optionalHostTag.get().getValue())) {
                return;
            }
            updateHostTag(host, env);
        }
    }

    private void updateHostTag(ZabbixHost.Host host, Env env) {
        ZabbixHostParam.Tag tag = ZabbixHostParam.Tag.builder()
                .tag("env")
                .value(env.getEnvName())
                .build();
        zabbixV5HostTagDrive.updateHostTags(configContext.get(), host, Lists.newArrayList(tag));
        zabbixV5HostTagDrive.evictHostTag(configContext.get(), host);  //清理缓存
    }

    /**
     * 更新主机模版（追加）
     *
     * @param property
     * @param host
     */
    private void updateHostTemplate(ServerProperty.Server property, ZabbixHost.Host host) {
        List<ZabbixTemplate.Template> zabbixTemplates = zabbixV5TemplateDrive.getByHost(configContext.get(), host);
        if (hostTemplateEquals(zabbixTemplates, property)) return; // 判断主机模版与配置是否相同，相同则跳过更新
        Set<String> templateNamSet = Sets.newHashSet();
        zabbixTemplates.forEach(t -> templateNamSet.add(t.getName()));
        property.getZabbix().getTemplates().forEach(n -> {
            if (!templateNamSet.contains(n)) {
                ZabbixTemplate.Template zabbixTemplate = zabbixV5TemplateDrive.getByName(configContext.get(), n);
                if (zabbixTemplate != null) {
                    zabbixTemplates.add(zabbixTemplate);
                    templateNamSet.add(n);
                }
            }
        });
        zabbixV5HostDrive.updateHostTemplates(configContext.get(), host, zabbixTemplates);
        // 主机模版与配置保持一致，清理多余模版
        if (property.getZabbix().getTemplateUniformity() != null && property.getZabbix().getTemplateUniformity()) {
            clearTemplates(zabbixTemplates, property); // 清理模版
            zabbixV5HostDrive.clearHostTemplates(configContext.get(), host, zabbixTemplates);
        }
        zabbixV5TemplateDrive.evictHostTemplate(configContext.get(), host); //清理缓存
    }

    private void clearTemplates(List<ZabbixTemplate.Template> templates, ServerProperty.Server property) {
        property.getZabbix().getTemplates().forEach(n -> {
            for (ZabbixTemplate.Template template : templates) {
                if (template.getName().equals(n)) {
                    templates.remove(template);
                    return;
                }
            }
        });
    }

    private boolean hostTemplateEquals(List<ZabbixTemplate.Template> templates, ServerProperty.Server property) {
        if (CollectionUtils.isEmpty(templates)) {
            return CollectionUtils.isEmpty(property.getZabbix().getTemplates());
        } else {
            if (property.getZabbix() == null || CollectionUtils.isEmpty(property.getZabbix().getTemplates())) {
                return false;
            } else {
                Set<String> templateNamSet = Sets.newHashSet();
                templates.forEach(t -> templateNamSet.add(t.getName()));
                for (String template : property.getZabbix().getTemplates()) {
                    if (templateNamSet.contains(template)) {
                        templateNamSet.remove(template);
                    } else {
                        return false;
                    }
                }
                return CollectionUtils.isEmpty(templateNamSet);
            }
        }
    }

    protected boolean isEnabled(ServerProperty.Server property) {
        return Optional.ofNullable(property)
                .map(ServerProperty.Server::getZabbix)
                .map(ServerProperty.Zabbix::getEnabled)
                .orElse(false);
    }

    protected String getProxyHostid(ServerProperty.Server property) {
        ZabbixProxy.Proxy proxy = getProxy(property);
        if (proxy == null)
            return null;
        return proxy.getProxyid();
    }

    private ZabbixProxy.Proxy getProxy(ServerProperty.Server property) {
        String proxyName = Optional.ofNullable(property)
                .map(ServerProperty.Server::getZabbix)
                .map(ServerProperty.Zabbix::getProxyName).orElse(null);
        if (StringUtils.isEmpty(proxyName))
            return null;
        return zabbixV5ProxyDrive.getProxy(configContext.get(), proxyName);
    }

    protected ZabbixHostParam.Tag buildTagsParams(Server server) {
        return ZabbixHostParam.Tag.builder()
                .tag("env")
                .value(getEnv(server).getEnvName())
                .build();
    }

    protected List<ZabbixHostParam.Template> buildTemplatesParams(ZabbixConfig.Zabbix zabbix, ServerProperty.Server property) {
        return zabbixV5TemplateDrive.listByNames(zabbix, property.getZabbix().getTemplates()).stream().map(e ->
                ZabbixHostParam.Template.builder()
                        .templateid(e.getTemplateid())
                        .build()
        ).collect(Collectors.toList());
    }

    protected ZabbixHostParam.Group buildHostGroupParams(ZabbixConfig.Zabbix zabbix, Server server) {
        ZabbixHostGroup.HostGroup hostGroup = zabbixFacade.getOrCreateHostGroup(zabbix, getServerGroup(server).getName());
        return ZabbixHostParam.Group.builder()
                .groupid(hostGroup.getGroupid())
                .build();
    }

    protected ZabbixHostParam.Interface buildHostInterfaceParams(Server server, ServerProperty.Server property) {
        return ZabbixHostParam.Interface.builder()
                .ip(getManageIp(server, property))
                .build();
    }

    protected String getManageIp(Server server, ServerProperty.Server property) {
        String manageIp = Optional.ofNullable(property)
                .map(ServerProperty.Server::getMetadata)
                .map(ServerProperty.Metadata::getManageIp)
                .orElse(server.getPrivateIp());
        return StringUtils.isEmpty(manageIp) ? server.getPrivateIp() : manageIp;
    }

}
