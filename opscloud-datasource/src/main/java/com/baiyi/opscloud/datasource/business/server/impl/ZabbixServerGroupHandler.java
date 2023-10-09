package com.baiyi.opscloud.datasource.business.server.impl;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.ZabbixConfig;
import com.baiyi.opscloud.datasource.business.account.impl.ZabbixAccountHandler;
import com.baiyi.opscloud.datasource.business.server.impl.base.AbstractServerGroupHandler;
import com.baiyi.opscloud.domain.base.BaseBusiness;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.ServerGroup;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.model.property.ServerProperty;
import com.baiyi.opscloud.service.business.BizPropertyHelper;
import com.baiyi.opscloud.zabbix.ZabbixUtil;
import com.baiyi.opscloud.zabbix.helper.ZabbixGroupHelper;
import com.baiyi.opscloud.zabbix.v5.driver.ZabbixV5ActionDriver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2021/8/24 1:43 下午
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ZabbixServerGroupHandler extends AbstractServerGroupHandler {

    private final ZabbixGroupHelper zabbixGroupHelper;

    private final ZabbixV5ActionDriver zabbixV5ActionDriver;

    private final BizPropertyHelper businessPropertyHelper;

    private final ZabbixAccountHandler zabbixAccountHandler;

    protected static ThreadLocal<ZabbixConfig.Zabbix> configContext = new ThreadLocal<>();

    @Override
    protected void initialConfig(DatasourceConfig dsConfig) {
        configContext.set(dsConfigManager.build(dsConfig, ZabbixConfig.class).getZabbix());
    }

    private ServerProperty.Server getBusinessProperty(ServerGroup serverGroup) {
        return businessPropertyHelper.getServerGroupProperty(serverGroup.getId());
    }

    @Override
    protected void doCreate(ServerGroup serverGroup) {
        ServerProperty.Server property = getBusinessProperty(serverGroup);
        boolean enable = Optional.ofNullable(property)
                .map(ServerProperty.Server::getZabbix)
                .map(ServerProperty.Zabbix::getEnabled)
                .orElse(false);
        if (!enable) {
            return;
        }
        zabbixGroupHelper.getOrCreateHostGroup(configContext.get(), serverGroup.getName());
        String usergroupName = ZabbixUtil.toUsergrpName(serverGroup.getName());
        zabbixGroupHelper.getOrCreateUserGroup(configContext.get(), usergroupName);
        // 创建动作
        String actionName = zabbixV5ActionDriver.buildActionName(usergroupName);
        if (zabbixV5ActionDriver.getActionByName(configContext.get(), actionName) == null) {
            zabbixV5ActionDriver.create(configContext.get(), actionName, usergroupName);
        }
    }

    @Override
    protected void doUpdate(ServerGroup serverGroup) {
        doCreate(serverGroup);
    }

    @Override
    protected void doDelete(ServerGroup serverGroup) {
    }

    /**
     * 用户组授权
     *
     * @param user
     * @param businessResource
     */
    @Override
    protected void doGrant(User user, BaseBusiness.IBusiness businessResource) {
        zabbixAccountHandler.grant(dsInstanceContext.get().getDsInstance(), user, businessResource);
    }

    /**
     * 用户组撤销授权
     *
     * @param user
     * @param businessResource
     */
    @Override
    protected void doRevoke(User user, BaseBusiness.IBusiness businessResource) {
        zabbixAccountHandler.revoke(dsInstanceContext.get().getDsInstance(), user, businessResource);
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
