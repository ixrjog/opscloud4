package com.baiyi.opscloud.datasource.server.impl.base;

import com.baiyi.opscloud.common.datasource.ZabbixDsInstanceConfig;
import com.baiyi.opscloud.common.datasource.config.DsZabbixConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.Env;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.model.property.ServerProperty;
import com.baiyi.opscloud.domain.vo.business.BaseBusiness;
import com.baiyi.opscloud.facade.server.SimpleServerNameFacade;
import com.baiyi.opscloud.zabbix.entry.ZabbixHost;
import com.baiyi.opscloud.zabbix.entry.ZabbixHostGroup;
import com.baiyi.opscloud.zabbix.entry.ZabbixHostTag;
import com.baiyi.opscloud.zabbix.entry.ZabbixTemplate;
import com.baiyi.opscloud.zabbix.entry.base.ZabbixTag;
import com.baiyi.opscloud.zabbix.facade.ZabbixFacade;
import com.baiyi.opscloud.zabbix.handler.ZabbixHostHandler;
import com.baiyi.opscloud.zabbix.handler.ZabbixHostTagHandler;
import com.baiyi.opscloud.zabbix.handler.ZabbixTemplateHandler;
import com.baiyi.opscloud.zabbix.handler.base.ZabbixServer;
import com.baiyi.opscloud.zabbix.http.IZabbixRequest;
import com.baiyi.opscloud.zabbix.param.ZabbixHostParam;
import com.fasterxml.jackson.databind.JsonNode;
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
public abstract class BaseZabbixHostServerProvider extends AbstractServerProvider<DsZabbixConfig.Zabbix> {

    @Resource
    private ZabbixFacade zabbixFacade;

    @Resource
    protected ZabbixHostHandler zabbixHostHandler;

    @Resource
    protected ZabbixHostTagHandler zabbixHostTagHandler;

    @Resource
    private ZabbixTemplateHandler zabbixTemplateHandler;

    @Resource
    private ZabbixServer zabbixServer;

    protected static ThreadLocal<DsZabbixConfig.Zabbix> configContext = new ThreadLocal<>();

    @Override
    protected void initialConfig(DatasourceConfig dsConfig) {
        configContext.set(dsConfigFactory.build(dsConfig, ZabbixDsInstanceConfig.class).getZabbix());
    }

    @Override
    protected void doGrant(User user, BaseBusiness.IBusiness businessResource) {
    }

    @Override
    protected void doRevoke(User user, BaseBusiness.IBusiness businessResource) {
    }

    protected JsonNode call(DsZabbixConfig.Zabbix zabbix, IZabbixRequest request) {
        return zabbixServer.call(zabbix, request);
    }

    protected void updateHost(Server server, ServerProperty.Server property, ZabbixHost zabbixHost) {
        String hostName = SimpleServerNameFacade.toServerName(server);
        // 更新主机名
        if (!hostName.equals(zabbixHost.getName())) {
            zabbixHostHandler.updateHostName(configContext.get(), zabbixHost, hostName);
            zabbixHostHandler.evictHostByIp(configContext.get(), getManageIp(server, property));
        }
        // 更新Templates
        updateHostTemplate(property, zabbixHost);
        // 更新Tags
        updateHostTag(server, zabbixHost);
        // TODO 更新Macros
    }

    /**
     * 更新主机标签
     *
     * @param server
     * @param zabbixHost
     */
    private void updateHostTag(Server server, ZabbixHost zabbixHost) {
        ZabbixHostTag zabbixHostTag = zabbixHostTagHandler.getHostTag(configContext.get(), zabbixHost);
        Env env = getEnv(server);
        if (zabbixHostTag != null && !CollectionUtils.isEmpty(zabbixHostTag.getTags())) {
            for (ZabbixTag tag : zabbixHostTag.getTags()) {
                if (tag.getTag().equals("env")) {
                    if (tag.getValue().equals(env.getEnvName())) {
                        return;
                    } else {
                        break;
                    }
                }
            }
        }
        ZabbixHostParam.Tag tag = ZabbixHostParam.Tag.builder()
                .tag("env")
                .value(env.getEnvName())
                .build();
        zabbixHostTagHandler.updateHostTags(configContext.get(), zabbixHost, Lists.newArrayList(tag));
        zabbixHostTagHandler.evictHostTag(configContext.get(), zabbixHost);  //清理缓存
    }

    /**
     * 更新主机模版（追加）
     *
     * @param property
     * @param zabbixHost
     */
    private void updateHostTemplate(ServerProperty.Server property, ZabbixHost zabbixHost) {
        List<ZabbixTemplate> zabbixTemplates = zabbixTemplateHandler.getByHost(configContext.get(), zabbixHost);
        if (hostTemplateEquals(zabbixTemplates, property)) return; // 判断主机模版与配置是否相同，相同则跳过更新
        Set<String> templateNamSet = Sets.newHashSet();
        zabbixTemplates.forEach(t -> templateNamSet.add(t.getName()));
        property.getZabbix().getTemplates().forEach(n -> {
            if (!templateNamSet.contains(n)) {
                ZabbixTemplate zabbixTemplate = zabbixTemplateHandler.getByName(configContext.get(), n);
                if (zabbixTemplate != null) {
                    zabbixTemplates.add(zabbixTemplate);
                    templateNamSet.add(n);
                }
            }
        });
        zabbixHostHandler.updateHostTemplates(configContext.get(), zabbixHost, zabbixTemplates);
        // 主机模版与配置保持一致，清理多余模版
        if (property.getZabbix().getTemplateUniformity() != null && property.getZabbix().getTemplateUniformity()) {
            clearTemplates(zabbixTemplates, property); // 清理模版
            zabbixHostHandler.clearHostTemplates(configContext.get(), zabbixHost, zabbixTemplates);
        }
        zabbixTemplateHandler.evictHostTemplate(configContext.get(), zabbixHost); //清理缓存
    }

    private void clearTemplates(List<ZabbixTemplate> zabbixTemplates, ServerProperty.Server property) {
        property.getZabbix().getTemplates().forEach(n -> {
            for (ZabbixTemplate zabbixTemplate : zabbixTemplates) {
                if (zabbixTemplate.getName().equals(n)) {
                    zabbixTemplates.remove(zabbixTemplate);
                    return;
                }
            }
        });
    }

    private boolean hostTemplateEquals(List<ZabbixTemplate> zabbixTemplates, ServerProperty.Server property) {
        if (CollectionUtils.isEmpty(zabbixTemplates)) {
            return CollectionUtils.isEmpty(property.getZabbix().getTemplates());
        } else {
            if (property.getZabbix() == null || CollectionUtils.isEmpty(property.getZabbix().getTemplates())) {
                return false;
            } else {
                Set<String> templateNamSet = Sets.newHashSet();
                zabbixTemplates.forEach(t -> templateNamSet.add(t.getName()));
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

    protected ZabbixHostParam.Tag buildTagsParams(Server server) {
        return ZabbixHostParam.Tag.builder()
                .tag("env")
                .value(getEnv(server).getEnvName())
                .build();
    }

    protected List<ZabbixHostParam.Template> buildTemplatesParams(DsZabbixConfig.Zabbix zabbix, ServerProperty.Server property) {
        return zabbixTemplateHandler.listByNames(zabbix, property.getZabbix().getTemplates()).stream().map(e ->
                ZabbixHostParam.Template.builder()
                        .templateid(e.getTemplateid())
                        .build()
        ).collect(Collectors.toList());
    }

    protected ZabbixHostParam.Group buildHostGroupParams(DsZabbixConfig.Zabbix zabbix, Server server) {
        ZabbixHostGroup zabbixHostGroup = zabbixFacade.getOrCreateHostGroup(zabbix, getServerGroup(server).getName());
        return ZabbixHostParam.Group.builder()
                .groupid(zabbixHostGroup.getGroupid())
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
