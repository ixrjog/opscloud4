package com.baiyi.opscloud.datasource.serverGroup.impl;

import com.baiyi.opscloud.common.datasource.ZabbixDsInstanceConfig;
import com.baiyi.opscloud.common.datasource.config.DsZabbixConfig;
import com.baiyi.opscloud.common.type.DsTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.ServerGroup;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.model.property.ServerProperty;
import com.baiyi.opscloud.domain.types.BusinessTypeEnum;
import com.baiyi.opscloud.domain.vo.business.BaseBusiness;
import com.baiyi.opscloud.service.business.BusinessPropertyHelper;
import com.baiyi.opscloud.zabbix.facade.ZabbixFacade;
import com.baiyi.opscloud.zabbix.handler.ZabbixActionHandler;
import com.baiyi.opscloud.zabbix.util.ZabbixUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2021/8/24 1:43 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class ZabbixServerGroupProvider extends AbstractServerGroupProvider<DsZabbixConfig.Zabbix> {

    @Resource
    private ZabbixFacade zabbixFacade;

    @Resource
    private ZabbixActionHandler zabbixActionHandler;

    @Resource
    private BusinessPropertyHelper businessPropertyHelper;

    private ServerProperty.Server getBusinessProperty(ServerGroup serverGroup) {
        return businessPropertyHelper.getServerGroupProperty(serverGroup.getId());
    }

    @Override
    protected DsZabbixConfig.Zabbix buildConfig(DatasourceConfig dsConfig) {
        return dsConfigFactory.build(dsConfig, ZabbixDsInstanceConfig.class).getZabbix();
    }

    @Override
    protected void doCreate(DsZabbixConfig.Zabbix zabbix, ServerGroup serverGroup) {
        ServerProperty.Server property = getBusinessProperty(serverGroup);
        boolean enable = Optional.ofNullable(property)
                .map(ServerProperty.Server::getZabbix)
                .map(ServerProperty.Zabbix::getEnable)
                .orElse(false);
        if (!enable) return;
        zabbixFacade.getOrCreateHostGroup(zabbix, serverGroup.getName());
        String usergroupName = ZabbixUtil.toUsergrpName(serverGroup.getName());
        zabbixFacade.getOrCreateUserGroup(zabbix, usergroupName);
        // 创建动作
        String actionName = zabbixActionHandler.buildActionName(usergroupName);
        if (zabbixActionHandler.getActionByName(zabbix, actionName) == null)
            zabbixActionHandler.create(zabbix, actionName, usergroupName);
    }

    @Override
    protected void doUpdate(DsZabbixConfig.Zabbix zabbix, ServerGroup serverGroup) {
        doCreate(zabbix, serverGroup);
    }

    @Override
    protected void doDelete(DsZabbixConfig.Zabbix zabbix, ServerGroup serverGroup) {

    }

    @Override
    protected void doGrant(DsZabbixConfig.Zabbix zabbix, User user, BaseBusiness.IBusiness businessResource) {

    }

    @Override
    protected void doRevoke(DsZabbixConfig.Zabbix zabbix, User user, BaseBusiness.IBusiness businessResource) {

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
