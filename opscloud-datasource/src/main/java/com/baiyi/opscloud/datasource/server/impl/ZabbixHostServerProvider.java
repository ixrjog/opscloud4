package com.baiyi.opscloud.datasource.server.impl;

import com.baiyi.opscloud.common.datasource.ZabbixDsInstanceConfig;
import com.baiyi.opscloud.common.datasource.config.DsZabbixConfig;
import com.baiyi.opscloud.common.type.DsTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.types.BusinessTypeEnum;
import com.baiyi.opscloud.domain.vo.business.BaseBusiness;
import com.baiyi.opscloud.service.server.ServerGroupService;
import com.baiyi.opscloud.zabbix.facade.ZabbixFacade;
import com.baiyi.opscloud.zabbix.handler.ZabbixHostHandler;
import com.baiyi.opscloud.zabbix.handler.ZabbixUserHandler;
import com.baiyi.opscloud.zabbix.http.SimpleZabbixRequest;
import com.baiyi.opscloud.zabbix.http.SimpleZabbixRequestBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/8/19 11:40 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class ZabbixHostServerProvider extends AbstractServerProvider<DsZabbixConfig.Zabbix> {

    @Resource
    private ZabbixUserHandler zabbixUserHandler;

    @Resource
    private ServerGroupService serverGroupService;

    @Resource
    private ZabbixFacade zabbixFacade;

    @Resource
    private ZabbixHostHandler zabbixHostHandler;



    @Override
    protected void doCreate(DsZabbixConfig.Zabbix zabbix, Server server) {


        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(ZabbixHostHandler.HostAPIMethod.CREATE)
             //   .paramEntry("host", getHostname(ocServer))
              //  .paramEntry("interfaces", ZabbixUtils.buildInterfacesParameter(getManageIp(ocServer)))
              //  .paramEntry("groups", ZabbixUtils.buildGroupsParameter(zabbixHostgroup))
              //  .paramEntry("templates", buildTemplatesParameter(serverAttributeMap))
              //  .paramEntry("tags", buildTagsParameter(ocServer))
              //  .paramEntrySkipEmpty("macros", ZabbixUtils.buildMacrosParameter(serverAttributeMap))
              //  .paramEntrySkipEmpty("proxy_hostid", getProxyhostid(serverAttributeMap))
                .build();
        // JsonNode data = call(zabbix, request);
//        if (data.get(RESULT).get(HOST_IDS).isEmpty()) {
//            throw new RuntimeException("ZabbixHost创建失败");
//        }
        // return ZabbixMapper.mapperList(data.get(RESULT).get(HOST_IDS), String.class).get(0);


    }

    @Override
    protected void doUpdate(DsZabbixConfig.Zabbix zabbix, Server server) {

    }

    @Override
    protected void doDelete(DsZabbixConfig.Zabbix zabbix, Server server) {
    }

    @Override
    protected void doGrant(DsZabbixConfig.Zabbix zabbix, Server server, BaseBusiness.IBusiness businessResource) {

    }

    @Override
    protected void doRevoke(DsZabbixConfig.Zabbix zabbix, Server server, BaseBusiness.IBusiness businessResource) {

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

    @Override
    public void grant(DatasourceInstance dsInstance, User user, BaseBusiness.IBusiness businessResource) {

    }

    @Override
    public void revoke(DatasourceInstance dsInstance, User user, BaseBusiness.IBusiness businessResource) {

    }
}
