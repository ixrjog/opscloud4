package com.baiyi.opscloud.datasource.serverGroup.impl;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.ZabbixConfig;
import com.baiyi.opscloud.datasource.account.impl.ZabbixAccountProvider;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.ServerGroup;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.model.property.ServerProperty;
import com.baiyi.opscloud.domain.types.BusinessTypeEnum;
import com.baiyi.opscloud.domain.base.BaseBusiness;
import com.baiyi.opscloud.service.business.BusinessPropertyHelper;
import com.baiyi.opscloud.zabbix.helper.ZabbixGroupHelper;
import com.baiyi.opscloud.zabbix.ZabbixUtil;
import com.baiyi.opscloud.zabbix.v5.drive.ZabbixV5ActionDrive;
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
public class ZabbixServerGroupProvider extends AbstractServerGroupProvider {

    @Resource
    private ZabbixGroupHelper zabbixFacade;

    @Resource
    private ZabbixV5ActionDrive zabbixV5ActionDatasource;

    @Resource
    private BusinessPropertyHelper businessPropertyHelper;

    @Resource
    private ZabbixAccountProvider zabbixAccountProvider;

    protected static ThreadLocal<ZabbixConfig.Zabbix> configContext = new ThreadLocal<>();

    @Override
    protected void initialConfig(DatasourceConfig dsConfig) {
        configContext.set(dsConfigHelper.build(dsConfig, ZabbixConfig.class).getZabbix());
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
        if (!enable) return;
        zabbixFacade.getOrCreateHostGroup(configContext.get(), serverGroup.getName());
        String usergroupName = ZabbixUtil.toUsergrpName(serverGroup.getName());
        zabbixFacade.getOrCreateUserGroup(configContext.get(), usergroupName);
        // 创建动作
        String actionName = zabbixV5ActionDatasource.buildActionName(usergroupName);
        if (zabbixV5ActionDatasource.getActionByName(configContext.get(), actionName) == null)
            zabbixV5ActionDatasource.create(configContext.get(), actionName, usergroupName);
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
        zabbixAccountProvider.grant(dsInstanceContext.get().getDsInstance(), user, businessResource);
    }

    /**
     * 用户组撤销授权
     *
     * @param user
     * @param businessResource
     */
    @Override
    protected void doRevoke(User user, BaseBusiness.IBusiness businessResource) {
        zabbixAccountProvider.revoke(dsInstanceContext.get().getDsInstance(), user, businessResource);
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
