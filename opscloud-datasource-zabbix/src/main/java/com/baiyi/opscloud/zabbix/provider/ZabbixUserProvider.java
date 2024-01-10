package com.baiyi.opscloud.zabbix.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.ZabbixConfig;
import com.baiyi.opscloud.core.comparer.AssetComparer;
import com.baiyi.opscloud.core.comparer.AssetComparerBuilder;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.AbstractAssetRelationProvider;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.zabbix.v5.driver.ZabbixV5UserDriver;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixUser;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixUserGroup;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_ZABBIX_USER;

/**
 * @Author 修远
 * @Date 2021/6/25 3:32 下午
 * @Since 1.0
 */

@Component
public class ZabbixUserProvider extends AbstractAssetRelationProvider<ZabbixUser.User, ZabbixUserGroup.UserGroup> {

    @Resource
    private ZabbixV5UserDriver zabbixV5UserDriver;

    @Resource
    private ZabbixUserProvider zabbixUserProvider;

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ZABBIX.name();
    }

    private ZabbixConfig.Zabbix buildConfig(DatasourceConfig dsConfig) {
        return dsConfigManager.build(dsConfig, ZabbixConfig.class).getZabbix();
    }

    @Override
    protected List<ZabbixUser.User> listEntities(DsInstanceContext dsInstanceContext, ZabbixUserGroup.UserGroup target) {
        ZabbixConfig.Zabbix zabbix = buildConfig(dsInstanceContext.getDsConfig());
        return zabbixV5UserDriver.listByGroup(zabbix, target);
    }

    @Override
    protected List<ZabbixUser.User> listEntities(DsInstanceContext dsInstanceContext) {
        return zabbixV5UserDriver.list(buildConfig(dsInstanceContext.getDsConfig()));
    }

    @Override
    @SingleTask(name = PULL_ZABBIX_USER, lockTime = "5m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.ZABBIX_USER.name();
    }

    @Override
    public String getTargetAssetKey() {
        return DsAssetTypeConstants.ZABBIX_USER_GROUP.name();
    }

    @Override
    protected AssetComparer getAssetComparer() {
        return AssetComparerBuilder.newBuilder()
                .compareOfName()
                .compareOfKey2()
                .compareOfKind()
                .build();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(zabbixUserProvider);
    }

}