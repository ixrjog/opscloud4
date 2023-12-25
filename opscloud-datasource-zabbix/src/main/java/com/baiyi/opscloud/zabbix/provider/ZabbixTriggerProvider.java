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
import com.baiyi.opscloud.zabbix.v5.driver.ZabbixV5TriggerDriver;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixHost;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixTrigger;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_ZABBIX_TRIGGER;

/**
 * @Author 修远
 * @Date 2021/7/2 3:43 下午
 * @Since 1.0
 */

@Component
public class ZabbixTriggerProvider extends AbstractAssetRelationProvider<ZabbixTrigger.Trigger, ZabbixHost.Host> {

    @Resource
    private ZabbixV5TriggerDriver zabbixV5TriggerDriver;

    @Resource
    private ZabbixTriggerProvider zabbixTriggerProvider;

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ZABBIX.name();
    }

    private ZabbixConfig.Zabbix buildConfig(DatasourceConfig dsConfig) {
        return dsConfigManager.build(dsConfig, ZabbixConfig.class).getZabbix();
    }

    @Override
    protected List<ZabbixTrigger.Trigger> listEntities(DsInstanceContext dsInstanceContext, ZabbixHost.Host target) {
        ZabbixConfig.Zabbix zabbix = buildConfig(dsInstanceContext.getDsConfig());
        return zabbixV5TriggerDriver.listByHost(zabbix, target);
    }

    @Override
    protected List<ZabbixTrigger.Trigger> listEntities(DsInstanceContext dsInstanceContext) {
        return zabbixV5TriggerDriver.list(buildConfig(dsInstanceContext.getDsConfig()));
    }

    @Override
    @SingleTask(name = PULL_ZABBIX_TRIGGER, lockTime = "5m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.ZABBIX_TRIGGER.name();
    }

    @Override
    public String getTargetAssetKey() {
        return DsAssetTypeConstants.ZABBIX_HOST.name();
    }

    @Override
    protected AssetComparer getAssetComparer() {
        return AssetComparerBuilder.newBuilder()
                .compareOfName()
                .compareOfKind()
                .compareOfActive()
                .compareOfCreatedTime()
                .build();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(zabbixTriggerProvider);
    }

}