package com.baiyi.opscloud.zabbix.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constant.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.ZabbixConfig;
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
import com.baiyi.opscloud.zabbix.convert.ZabbixTriggerAssetConvert;
import com.baiyi.opscloud.zabbix.v5.drive.ZabbixV5TriggerDrive;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixHost;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixTrigger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

import static com.baiyi.opscloud.common.constant.SingleTaskConstants.PULL_ZABBIX_TRIGGER;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/2 3:43 下午
 * @Since 1.0
 */

@Component
public class ZabbixTriggerProvider extends AbstractAssetRelationProvider<ZabbixTrigger.Trigger, ZabbixHost.Host> {

    @Resource
    private ZabbixV5TriggerDrive zabbixV5TriggerDrive;

    @Resource
    private ZabbixTriggerProvider zabbixTriggerProvider;

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ZABBIX.name();
    }

    private ZabbixConfig.Zabbix buildConfig(DatasourceConfig dsConfig) {
        return dsConfigHelper.build(dsConfig, ZabbixConfig.class).getZabbix();
    }

    @Override
    protected List<ZabbixTrigger.Trigger> listEntities(DsInstanceContext dsInstanceContext, ZabbixHost.Host target) {
        ZabbixConfig.Zabbix zabbix = buildConfig(dsInstanceContext.getDsConfig());
        return zabbixV5TriggerDrive.listByHost(zabbix, target);
    }

    @Override
    protected List<ZabbixTrigger.Trigger> listEntities(DsInstanceContext dsInstanceContext) {
        return zabbixV5TriggerDrive.list(buildConfig(dsInstanceContext.getDsConfig()));
    }

    @Override
    protected ZabbixTrigger.Trigger getEntity(DsInstanceContext dsInstanceContext, UniqueAssetParam param) {
        return zabbixV5TriggerDrive.getById(buildConfig(dsInstanceContext.getDsConfig()), param.getAssetId());
    }

    @Override
    @SingleTask(name = PULL_ZABBIX_TRIGGER, lockTime = "5m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeEnum.ZABBIX_TRIGGER.getType();
    }

    @Override
    public String getTargetAssetKey() {
        return DsAssetTypeEnum.ZABBIX_HOST.getType();
    }

    @Override
    protected boolean equals(DatasourceInstanceAsset asset, DatasourceInstanceAsset preAsset) {
        if (!AssetUtil.equals(preAsset.getKind(), asset.getKind()))
            return false;
        if (!AssetUtil.equals(preAsset.getName(), asset.getName()))
            return false;
        if (preAsset.getIsActive() != asset.getIsActive())
            return false;
        if (preAsset.getCreatedTime() != asset.getCreatedTime())
            return false;
        return true;
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, ZabbixTrigger.Trigger entity) {
        return ZabbixTriggerAssetConvert.toAssetContainer(dsInstance, entity);
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(zabbixTriggerProvider);
    }
}

