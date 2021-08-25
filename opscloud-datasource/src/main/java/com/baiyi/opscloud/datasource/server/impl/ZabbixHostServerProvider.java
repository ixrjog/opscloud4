package com.baiyi.opscloud.datasource.server.impl;

import com.baiyi.opscloud.common.datasource.ZabbixDsInstanceConfig;
import com.baiyi.opscloud.common.datasource.config.DsZabbixConfig;
import com.baiyi.opscloud.common.type.DsTypeEnum;
import com.baiyi.opscloud.datasource.server.impl.base.BaseZabbixHostServerProvider;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.model.property.ServerProperty;
import com.baiyi.opscloud.domain.types.BusinessTypeEnum;
import com.baiyi.opscloud.facade.server.SimpleServerNameFacade;
import com.baiyi.opscloud.zabbix.entry.ZabbixHost;
import com.baiyi.opscloud.zabbix.handler.ZabbixHostHandler;
import com.baiyi.opscloud.zabbix.http.SimpleZabbixRequest;
import com.baiyi.opscloud.zabbix.http.SimpleZabbixRequestBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.baiyi.opscloud.zabbix.handler.base.ZabbixServer.ApiConstant.HOST_IDS;
import static com.baiyi.opscloud.zabbix.handler.base.ZabbixServer.ApiConstant.RESULT;

/**
 * @Author baiyi
 * @Date 2021/8/19 11:40 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class ZabbixHostServerProvider extends BaseZabbixHostServerProvider {

    @Override
    protected void doCreate(DsZabbixConfig.Zabbix zabbix, Server server) {
        ServerProperty.Server property = getBusinessProperty(server);
        boolean enable = Optional.ofNullable(property)
                .map(ServerProperty.Server::getZabbix)
                .map(ServerProperty.Zabbix::getEnable)
                .orElse(false);
        if (!enable) return;
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(ZabbixHostHandler.HostAPIMethod.CREATE)
                .paramEntry("host", SimpleServerNameFacade.toServerName(server))
                .paramEntry("interfaces", buildHostInterfaceParams(server, property))
                .paramEntry("groups", buildHostGroupParams(zabbix, server))
                .paramEntry("templates", buildTemplatesParams(zabbix, property))
                .paramEntry("tags", buildTagsParams(server))
                //  .paramEntrySkipEmpty("macros", ZabbixUtils.buildMacrosParameter(serverAttributeMap))
                //  .paramEntrySkipEmpty("proxy_hostid", getProxyhostid(serverAttributeMap))
                .build();
        JsonNode data = call(zabbix, request);
        if (data.get(RESULT).get(HOST_IDS).isEmpty()) {
            log.error("ZabbixHost创建失败!");
        }
    }

    @Override
    protected void doUpdate(DsZabbixConfig.Zabbix zabbix, Server server) {
        ServerProperty.Server property = getBusinessProperty(server);
        String manageIp = getManageIp(server, property);
        ZabbixHost zabbixHost = null;
        try {
            zabbixHost = zabbixHostHandler.getByIp(zabbix, manageIp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (zabbixHost == null) {
            doCreate(zabbix, server);
        } else {
            // 开始更新
            updateHost(zabbix, server, property, zabbixHost);
        }
    }

    @Override
    protected void doDelete(DsZabbixConfig.Zabbix zabbix, Server server) {
        ServerProperty.Server property = getBusinessProperty(server);
        String manageIp = getManageIp(server, property);
        try {
            ZabbixHost zabbixHost = zabbixHostHandler.getByIp(zabbix, manageIp);
            if (zabbixHost == null) {
                return;
            }
            zabbixHostHandler.deleteById(zabbix, zabbixHost.getHostid());
            zabbixHostHandler.evictHostById(zabbixHost.getHostid());
        } catch (Exception ignored) {
        }
        zabbixHostHandler.evictHostByIp(manageIp);
    }

    @Override
    protected int getBusinessResourceType() {
        return BusinessTypeEnum.SERVERGROUP.getType();
    }

    @Override
    protected DsZabbixConfig.Zabbix buildConfig(DatasourceConfig dsConfig) {
        return dsConfigFactory.build(dsConfig, ZabbixDsInstanceConfig.class).getZabbix();
    }


    @Override
    public String getInstanceType() {
        return DsTypeEnum.ZABBIX.getName();
    }

}
