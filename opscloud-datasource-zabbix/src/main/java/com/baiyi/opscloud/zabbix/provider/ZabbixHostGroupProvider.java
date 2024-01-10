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
import com.baiyi.opscloud.zabbix.v5.driver.ZabbixV5HostGroupDriver;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixHost;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixHostGroup;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_ZABBIX_HOST_GROUP;

/**
 * @Author 修远
 * @Date 2021/7/1 2:23 下午
 * @Since 1.0
 */

@Component
public class ZabbixHostGroupProvider extends AbstractAssetRelationProvider<ZabbixHostGroup.HostGroup, ZabbixHost.Host> {

    @Resource
    private ZabbixV5HostGroupDriver zabbixV5HostGroupDriver;

    @Resource
    private ZabbixHostGroupProvider zabbixHostGroupProvider;

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ZABBIX.name();
    }

    private ZabbixConfig.Zabbix buildConfig(DatasourceConfig dsConfig) {
        return dsConfigManager.build(dsConfig, ZabbixConfig.class).getZabbix();
    }

    @Override
    protected List<ZabbixHostGroup.HostGroup> listEntities(DsInstanceContext dsInstanceContext, ZabbixHost.Host target) {
        ZabbixConfig.Zabbix zabbix = buildConfig(dsInstanceContext.getDsConfig());
        return zabbixV5HostGroupDriver.listByHost(zabbix, target);
    }

    @Override
    protected List<ZabbixHostGroup.HostGroup> listEntities(DsInstanceContext dsInstanceContext) {
        return zabbixV5HostGroupDriver.list(buildConfig(dsInstanceContext.getDsConfig()));
    }

    @Override
    @SingleTask(name = PULL_ZABBIX_HOST_GROUP, lockTime = "5m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.ZABBIX_HOST_GROUP.name();
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
                .build();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(zabbixHostGroupProvider);
    }

}