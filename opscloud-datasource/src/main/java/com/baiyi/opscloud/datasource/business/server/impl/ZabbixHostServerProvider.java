package com.baiyi.opscloud.datasource.business.server.impl;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.ZabbixConfig;
import com.baiyi.opscloud.datasource.business.server.impl.base.AbstractZabbixHostServerProvider;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.model.property.ServerProperty;
import com.baiyi.opscloud.domain.types.BusinessTypeEnum;
import com.baiyi.opscloud.facade.server.SimpleServerNameFacade;
import com.baiyi.opscloud.zabbix.v5.drive.base.AbstractZabbixV5HostDrive;
import com.baiyi.opscloud.zabbix.v5.drive.base.SimpleZabbixAuth;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixHost;
import com.baiyi.opscloud.zabbix.v5.feign.ZabbixHostFeign;
import com.baiyi.opscloud.zabbix.v5.request.ZabbixRequest;
import com.baiyi.opscloud.zabbix.v5.request.builder.ZabbixRequestBuilder;
import feign.Feign;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/8/19 11:40 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class ZabbixHostServerProvider extends AbstractZabbixHostServerProvider {

    @Resource
    private SimpleZabbixAuth simpleZabbixAuth;

    private ZabbixHostFeign buildFeign(ZabbixConfig.Zabbix config) {
        return Feign.builder()
                .retryer(new Retryer.Default(3000, 3000, 3))
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(ZabbixHostFeign.class, config.getUrl());
    }

    protected ZabbixHost.CreateHostResponse createHandle(ZabbixConfig.Zabbix config, ZabbixRequest.DefaultRequest request) {
        ZabbixHostFeign zabbixAPI = buildFeign(config);
        request.setMethod(AbstractZabbixV5HostDrive.HostAPIMethod.CREATE);
        request.setAuth(simpleZabbixAuth.getAuth(config));
        return zabbixAPI.create(request);
    }

    @Override
    protected void doCreate(Server server) {
        ServerProperty.Server property = getBusinessProperty(server);
        if (!isEnabled(property)) return;
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                .method(AbstractZabbixV5HostDrive.HostAPIMethod.CREATE)
                .putParam("host", SimpleServerNameFacade.toServerName(server))
                .putParam("interfaces", buildHostInterfaceParams(server, property))
                .putParam("groups", buildHostGroupParams(configContext.get(), server))
                .putParam("templates", buildTemplatesParams(configContext.get(), property))
                .putParam("tags", buildTagsParams(server))
                .putParamSkipEmpty("proxy_hostid", getProxyHostid(property))
                .build();
        ZabbixHost.CreateHostResponse response = createHandle(configContext.get(), request);
        if (CollectionUtils.isEmpty(response.getResult().getHostids())) {
            log.error("ZabbixHost创建失败!");
        }
    }

    @Override
    protected void doUpdate(Server server) {
        ServerProperty.Server property = getBusinessProperty(server);
        if (!isEnabled(property)) return;
        String manageIp = getManageIp(server, property);
        try {
            com.baiyi.opscloud.zabbix.v5.entity.ZabbixHost.Host host = zabbixV5HostDrive.getByIp(configContext.get(), manageIp);
            if (host == null) {
                doCreate(server);
            } else {
                updateHost(server, property, host);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(Server server) {
        ServerProperty.Server property = getBusinessProperty(server);
        String manageIp = getManageIp(server, property);
        try {
            com.baiyi.opscloud.zabbix.v5.entity.ZabbixHost.Host host = zabbixV5HostDrive.getByIp(configContext.get(), manageIp);
            if (host == null) {
                return;
            }
            zabbixV5HostDrive.deleteById(configContext.get(), host.getHostid());
            zabbixV5HostDrive.evictHostById(configContext.get(), host.getHostid());
        } catch (Exception ignored) {
        } finally {
            zabbixV5HostDrive.evictHostByIp(configContext.get(), manageIp);
        }
    }

    @Override
    protected int getBusinessResourceType() {
        return BusinessTypeEnum.SERVERGROUP.getType();
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ZABBIX.getName();
    }

}
