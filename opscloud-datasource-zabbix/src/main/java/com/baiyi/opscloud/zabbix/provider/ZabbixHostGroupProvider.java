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
import com.baiyi.opscloud.zabbix.entity.ZabbixHost;
import com.baiyi.opscloud.zabbix.entity.ZabbixHostGroup;
import com.baiyi.opscloud.zabbix.datasource.ZabbixHostGroupDatasource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

import static com.baiyi.opscloud.common.constant.SingleTaskConstants.PULL_ZABBIX_HOST_GROUP;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/1 2:23 下午
 * @Since 1.0
 */

@Component
public class ZabbixHostGroupProvider extends AbstractAssetRelationProvider<ZabbixHostGroup, ZabbixHost> {

    @Resource
    private ZabbixHostGroupDatasource zabbixHostGroupDatasource;

    @Resource
    private ZabbixHostGroupProvider zabbixHostGroupProvider;

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ZABBIX.name();
    }

    private ZabbixConfig.Zabbix buildConfig(DatasourceConfig dsConfig) {
        return dsConfigHelper.build(dsConfig, ZabbixConfig.class).getZabbix();
    }

    @Override
    protected List<ZabbixHostGroup> listEntities(DsInstanceContext dsInstanceContext, ZabbixHost target) {
        ZabbixConfig.Zabbix zabbix = buildConfig(dsInstanceContext.getDsConfig());
        return zabbixHostGroupDatasource.listByHost(zabbix, target);
    }

    @Override
    protected List<ZabbixHostGroup> listEntities(DsInstanceContext dsInstanceContext) {
        return zabbixHostGroupDatasource.list(buildConfig(dsInstanceContext.getDsConfig()));
    }

    @Override
    protected ZabbixHostGroup getEntity(DsInstanceContext dsInstanceContext, UniqueAssetParam param) {
        return zabbixHostGroupDatasource.getById(buildConfig(dsInstanceContext.getDsConfig()), param.getAssetId());
    }

    @Override
    @SingleTask(name = PULL_ZABBIX_HOST_GROUP, lockTime = "5m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeEnum.ZABBIX_HOST_GROUP.getType();
    }

    @Override
    public String getTargetAssetKey() {
        return DsAssetTypeEnum.ZABBIX_HOST.getType();
    }

    @Override
    protected boolean equals(DatasourceInstanceAsset asset, DatasourceInstanceAsset preAsset) {
        if (!AssetUtil.equals(preAsset.getName(), asset.getName()))
            return false;
        if (!AssetUtil.equals(preAsset.getKind(), asset.getKind()))
            return false;
        return true;
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, ZabbixHostGroup entity) {
        return ZabbixHostAssetConvert.toAssetContainer(dsInstance, entity);
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(zabbixHostGroupProvider);
    }
}
