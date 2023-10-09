package com.baiyi.opscloud.datasource.business.server.impl.base;

import com.baiyi.opscloud.common.datasource.ZabbixConfig;
import com.baiyi.opscloud.datasource.business.server.util.HostParamUtil;
import com.baiyi.opscloud.datasource.business.server.util.ZabbixTemplateUtil;
import com.baiyi.opscloud.domain.base.BaseBusiness;
import com.baiyi.opscloud.domain.base.SimpleBusiness;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.model.property.ServerProperty;
import com.baiyi.opscloud.domain.util.ObjectUtil;
import com.baiyi.opscloud.service.tag.BusinessTagService;
import com.baiyi.opscloud.service.tag.TagService;
import com.baiyi.opscloud.zabbix.helper.ZabbixGroupHelper;
import com.baiyi.opscloud.zabbix.v5.driver.ZabbixV5HostDriver;
import com.baiyi.opscloud.zabbix.v5.driver.ZabbixV5HostTagDriver;
import com.baiyi.opscloud.zabbix.v5.driver.ZabbixV5ProxyDriver;
import com.baiyi.opscloud.zabbix.v5.driver.ZabbixV5TemplateDriver;
import com.baiyi.opscloud.zabbix.v5.driver.base.SimpleZabbixV5HostDriver;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixHost;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixHostGroup;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixProxy;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixTemplate;
import com.baiyi.opscloud.zabbix.v5.param.ZabbixHostParam;
import com.baiyi.opscloud.zabbix.v5.request.ZabbixRequest;
import com.baiyi.opscloud.zabbix.v5.request.builder.ZabbixRequestBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/8/23 9:48 上午
 * @Version 1.0
 */
@Slf4j
public abstract class AbstractZabbixHostServerHandler extends BaseServerHandler<ZabbixConfig.Zabbix> {

    @Resource
    private ZabbixGroupHelper zabbixGroupHelper;

    @Resource
    protected ZabbixV5HostDriver zabbixV5HostDriver;

    @Resource
    protected ZabbixV5HostTagDriver zabbixV5HostTagDriver;

    @Resource
    protected ZabbixV5ProxyDriver zabbixV5ProxyDriver;

    @Resource
    private ZabbixV5TemplateDriver zabbixV5TemplateDriver;

    @Resource
    private SimpleZabbixV5HostDriver simpleZabbixV5HostDriver;

    @Resource
    private BusinessTagService businessTagService;

    @Resource
    private TagService tagService;

    protected static ThreadLocal<ZabbixConfig.Zabbix> configContext = new ThreadLocal<>();

    @Override
    protected void initialConfig(DatasourceConfig dsConfig) {
        configContext.set(dsConfigManager.build(dsConfig, ZabbixConfig.class).getZabbix());
    }

    @Override
    protected void doGrant(User user, BaseBusiness.IBusiness businessResource) {
    }

    @Override
    protected void doRevoke(User user, BaseBusiness.IBusiness businessResource) {
    }

    protected void doCreate(Server server, ServerProperty.Server property) {
        if (!property.zabbixEnabled()) {
            return;
        }
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                .method(SimpleZabbixV5HostDriver.HostAPIMethod.CREATE)
                .putParam("host", server.getDisplayName())
                .putParam("interfaces", HostParamUtil.buildInterfaceParam(server, property))
                .putParam("groups", buildHostGroupParam(configContext.get(), server))
                .putParam("templates", buildTemplatesParam(configContext.get(), property))
                .putParam("tags", buildTagsParam(server))
                .putParamSkipEmpty("proxy_hostid", getProxyHostid(property))
                .putParamSkipEmpty("macros", property.getZabbix().toMacros())
                .build();
        ZabbixHost.CreateHostResponse response = simpleZabbixV5HostDriver.createHandle(configContext.get(), request);
        if (CollectionUtils.isEmpty(response.getResult().getHostids())) {
            log.error("ZabbixHost创建失败!");
        }
    }

    protected void updateHost(Server server, ServerProperty.Server property, ZabbixHost.Host host, String manageIp) {
        String hostName = server.getDisplayName();
        ZabbixRequestBuilder requestBuilder = ZabbixRequestBuilder.builder();
        // 更新主机名
        if (!hostName.equals(host.getName())) {
            requestBuilder.putParam("host", hostName);
            zabbixV5HostDriver.evictHostByIp(configContext.get(), HostParamUtil.getManageIp(server, property));
        }
        putProxyUpdateParam(property, host, requestBuilder);
        putTemplateUpdateParam(property, host, requestBuilder);
        putTagUpdateParam(server, host, requestBuilder);
        putMacroUpdateParam(host, property, requestBuilder);
        updateHost(host, requestBuilder, manageIp);
    }

    private void updateHost(ZabbixHost.Host host, ZabbixRequestBuilder requestBuilder, String manageIp) {
        ZabbixRequest.DefaultRequest request = requestBuilder.build();
        Map<String, Object> params = request.getParams();
        // 空参数则不更新主机
        if (params.keySet().stream().anyMatch(k -> ObjectUtil.isNotEmpty(params.get(k)))) {
            zabbixV5HostDriver.updateHost(configContext.get(), host, request);
            zabbixV5HostDriver.evictHostByIp(configContext.get(), manageIp);
        }
    }

    public void putMacroUpdateParam(ZabbixHost.Host host, ServerProperty.Server property, ZabbixRequestBuilder requestBuilder) {
        List<ServerProperty.Macro> macros = property.getZabbix().toMacros();
        if (CollectionUtils.isEmpty(macros)) {
            return;
        }
        requestBuilder.putParam("macros", macros);
    }

    private void putTagUpdateParam(Server server, ZabbixHost.Host host, ZabbixRequestBuilder requestBuilder) {
        // 判断Tags是否需要更新
        List<ZabbixHostParam.Tag> preTags = buildTagsParam(server);
        if (CollectionUtils.isEmpty(preTags)) {
            return;
        }
//        ZabbixHost.Host preHost = zabbixV5HostTagDriver.getHostTag(configContext.get(), host);
//        if (!CollectionUtils.isEmpty(preHost.getTags())) {
//            Map<String, ZabbixHost.HostTag> hostTagMap = preHost.getTags().stream().collect(Collectors.toMap(ZabbixHost.HostTag::getTag, a -> a, (k1, k2) -> k1));
//
//            for (ZabbixHostParam.Tag preTag : preTags) {
//                if (hostTagMap.containsKey(preTag.getTag())) {
//                    ZabbixHost.HostTag hostTag = hostTagMap.get(preTag.getTag());
//                    if (!preTag.getValue().equals(hostTag.getValue())) {
//                        break;
//                    }
//                } else {
//                    break;
//                }
//            }
//        }
        requestBuilder.putParam("tags", buildTagsParam(server));
        // 清理缓存
        zabbixV5HostTagDriver.evictHostTag(configContext.get(), host);
    }

    /**
     * 更新模板参数（追加）
     *
     * @param property
     * @param host
     */
    private void putTemplateUpdateParam(ServerProperty.Server property, ZabbixHost.Host host, ZabbixRequestBuilder requestBuilder) {
        List<ZabbixTemplate.Template> zabbixTemplates = zabbixV5TemplateDriver.getByHost(configContext.get(), host);
        // 判断主机模板与配置是否相同，相同则跳过更新
        if (ZabbixTemplateUtil.hostTemplateEquals(zabbixTemplates, property)) {
            return;
        }
        Set<String> templateNamSet = Sets.newHashSet();
        zabbixTemplates.forEach(t -> templateNamSet.add(t.getName()));
        property.getZabbix().getTemplates().forEach(n -> {
            if (!templateNamSet.contains(n)) {
                ZabbixTemplate.Template zabbixTemplate = zabbixV5TemplateDriver.getByName(configContext.get(), n);
                if (zabbixTemplate != null) {
                    zabbixTemplates.add(zabbixTemplate);
                    templateNamSet.add(n);
                }
            }
        });
        // 更新模板参数
        requestBuilder.putParamSkipEmpty("templates", HostParamUtil.toTemplateParam(zabbixTemplates));
        // 主机模板与配置保持一致，清理多余模板
        if (property.getZabbix().getTemplateUniformity() != null && property.getZabbix().getTemplateUniformity()) {
            // 清理模板
            clearTemplates(zabbixTemplates, property);
            requestBuilder.putParamSkipEmpty("templates_clear", HostParamUtil.toTemplateParam(zabbixTemplates));
        }
        //清理缓存
        zabbixV5TemplateDriver.evictHostTemplate(configContext.get(), host);
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

    private void putProxyUpdateParam(ServerProperty.Server property, ZabbixHost.Host host, ZabbixRequestBuilder requestBuilder) {
        String proxyHostid = getProxyHostid(property);
        if (StringUtils.isEmpty(host.getProxyHostid()) || host.getProxyHostid().equals("0")) {
            if (StringUtils.isEmpty(proxyHostid)) {
                return;
            }
        } else {
            if (host.getProxyHostid().equals(proxyHostid)) {
                return;
            } else {
                // 删除代理
                proxyHostid = "0";
            }
        }
        requestBuilder.putParam("proxy_hostid", proxyHostid);
    }

    protected String getProxyHostid(ServerProperty.Server property) {
        ZabbixProxy.Proxy proxy = getProxy(property);
        return proxy == null ? null : proxy.getProxyid();
    }

    private ZabbixProxy.Proxy getProxy(ServerProperty.Server property) {
        String proxyName = Optional.ofNullable(property)
                .map(ServerProperty.Server::getZabbix)
                .map(ServerProperty.Zabbix::getProxyName).orElse(null);
        if (StringUtils.isEmpty(proxyName)) {
            return null;
        }
        return zabbixV5ProxyDriver.getProxy(configContext.get(), proxyName);
    }

    protected List<ZabbixHostParam.Tag> buildTagsParam(Server server) {
        List<ZabbixHostParam.Tag> tags = Lists.newArrayList();
        ZabbixHostParam.Tag envTag = ZabbixHostParam.Tag.builder()
                .tag("env")
                .value(getEnv(server).getEnvName())
                .build();
        tags.add(envTag);

        // TODO
        SimpleBusiness query = SimpleBusiness.builder()
                .businessType(BusinessTypeEnum.SERVER.getType())
                .businessId(server.getId())
                .build();
        List<BusinessTag> bizTags = businessTagService.queryByBusiness(query);

        if (!CollectionUtils.isEmpty(bizTags)) {
            for (BusinessTag bizTag : bizTags) {
                Tag tag = tagService.getById(bizTag.getTagId());
                if (tag.getTagKey().startsWith("@")) {
                    ZabbixHostParam.Tag regionTag = ZabbixHostParam.Tag.builder()
                            .tag("region")
                            .value(tag.getTagKey())
                            .build();
                    tags.add(regionTag);
                }
                break;
            }
        }
        return tags;
    }

    protected List<ZabbixHostParam.Template> buildTemplatesParam(ZabbixConfig.Zabbix zabbix, ServerProperty.Server property) {
        return zabbixV5TemplateDriver.listByNames(zabbix, property.getZabbix().getTemplates()).stream().map(e ->
                ZabbixHostParam.Template.builder()
                        .templateid(e.getTemplateid())
                        .build()
        ).collect(Collectors.toList());
    }

    protected ZabbixHostParam.Group buildHostGroupParam(ZabbixConfig.Zabbix zabbix, Server server) {
        ZabbixHostGroup.HostGroup hostGroup = zabbixGroupHelper.getOrCreateHostGroup(zabbix, getServerGroup(server).getName());
        return ZabbixHostParam.Group.builder()
                .groupid(hostGroup.getGroupid())
                .build();
    }

}
