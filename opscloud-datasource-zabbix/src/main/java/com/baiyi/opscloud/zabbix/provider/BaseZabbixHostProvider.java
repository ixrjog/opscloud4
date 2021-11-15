package com.baiyi.opscloud.zabbix.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.datasource.ZabbixConfig;
import com.baiyi.opscloud.common.constant.enums.DsTypeEnum;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.AbstractAssetRelationProvider;
import com.baiyi.opscloud.core.provider.base.param.UniqueAssetParam;
import com.baiyi.opscloud.core.util.AssetUtil;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.baiyi.opscloud.zabbix.convert.ZabbixHostAssetConvert;
import com.baiyi.opscloud.zabbix.entry.ZabbixHost;
import com.baiyi.opscloud.zabbix.handler.ZabbixHostHandler;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/8/2 6:20 下午
 * @Version 1.0
 */
public abstract class BaseZabbixHostProvider<T> extends AbstractAssetRelationProvider<ZabbixHost,T> {

    @Resource
    protected ZabbixHostHandler zabbixHostHandler;

    @Resource
    private ZabbixHostProvider zabbixHostTargetGroupProvider;

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ZABBIX.name();
    }

    protected ZabbixConfig.Zabbix buildConfig(DatasourceConfig dsConfig) {
        return dsConfigHelper.build(dsConfig, ZabbixConfig.class).getZabbix();
    }

    @Override
    protected List<ZabbixHost> listEntries(DsInstanceContext dsInstanceContext) {
        return zabbixHostHandler.list(buildConfig(dsInstanceContext.getDsConfig()));
    }

    @Override
    protected ZabbixHost getEntry(DsInstanceContext dsInstanceContext, UniqueAssetParam param) {
        return zabbixHostHandler.getById(buildConfig(dsInstanceContext.getDsConfig()), param.getAssetId());
    }

    @Override
    @SingleTask(name = "PullZabbixHost", lockTime = "5m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeEnum.ZABBIX_HOST.getType();
    }

    @Override
    protected boolean equals(DatasourceInstanceAsset asset, DatasourceInstanceAsset preAsset) {
        if (!AssetUtil.equals(preAsset.getName(), asset.getName()))
            return false;
        if (!AssetUtil.equals(preAsset.getAssetKey(), asset.getAssetKey()))
            return false;
        if (!AssetUtil.equals(preAsset.getKind(), asset.getKind()))
            return false;
        if (!preAsset.getIsActive().equals(asset.getIsActive()))
            return false;
        if (!AssetUtil.equals(preAsset.getDescription(), asset.getDescription()))
            return false;
        return true;
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, ZabbixHost entry) {
        return ZabbixHostAssetConvert.toAssetContainer(dsInstance, entry);
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(zabbixHostTargetGroupProvider);
    }
}

