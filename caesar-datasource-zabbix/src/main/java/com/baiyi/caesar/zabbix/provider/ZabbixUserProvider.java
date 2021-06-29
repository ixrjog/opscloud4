package com.baiyi.caesar.zabbix.provider;

import com.baiyi.caesar.common.datasource.ZabbixDsInstanceConfig;
import com.baiyi.caesar.common.datasource.config.DsZabbixConfig;
import com.baiyi.caesar.common.type.DsAssetTypeEnum;
import com.baiyi.caesar.common.type.DsTypeEnum;
import com.baiyi.caesar.datasource.builder.AssetContainer;
import com.baiyi.caesar.datasource.factory.AssetProviderFactory;
import com.baiyi.caesar.datasource.model.DsInstanceContext;
import com.baiyi.caesar.datasource.provider.asset.AbstractAssetRelationProvider;
import com.baiyi.caesar.datasource.util.AssetUtil;
import com.baiyi.caesar.domain.generator.caesar.DatasourceConfig;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstance;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstanceAsset;
import com.baiyi.caesar.zabbix.convert.ZabbixAssetConvert;
import com.baiyi.caesar.zabbix.entry.ZabbixUser;
import com.baiyi.caesar.zabbix.entry.ZabbixUserGroup;
import com.baiyi.caesar.zabbix.handler.ZabbixUserHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/25 3:32 下午
 * @Since 1.0
 */

@Component
public class ZabbixUserProvider extends AbstractAssetRelationProvider<ZabbixUser, ZabbixUserGroup> {

    @Resource
    private ZabbixUserHandler zabbixUserHandler;

    @Resource
    private ZabbixUserProvider zabbixUserProvider;

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ZABBIX.name();
    }

    private DsZabbixConfig.Zabbix buildConfig(DatasourceConfig dsConfig) {
        return dsFactory.build(dsConfig, ZabbixDsInstanceConfig.class).getZabbix();
    }

    @Override
    protected List<ZabbixUser> listEntries(DsInstanceContext dsInstanceContext, ZabbixUserGroup target) {
        DsZabbixConfig.Zabbix zabbix = buildConfig(dsInstanceContext.getDsConfig());
        return zabbixUserHandler.ListUsersByGroup(zabbix, target);
    }

    @Override
    protected List<ZabbixUser> listEntries(DsInstanceContext dsInstanceContext) {
        return zabbixUserHandler.ListUsers(buildConfig(dsInstanceContext.getDsConfig()));
    }

    @Override
//    @SingleTask(name = "PullZabbixUser", lockTime = 300)
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeEnum.ZABBIX_USER.getType();
    }

    @Override
    public String getTargetAssetKey() {
        return DsAssetTypeEnum.ZABBIX_USER_GROUP.getType();
    }

    @Override
    protected boolean equals(DatasourceInstanceAsset asset, DatasourceInstanceAsset preAsset) {
        if (!AssetUtil.equals(preAsset.getName(), asset.getName()))
            return false;
        if (!AssetUtil.equals(preAsset.getAssetKey2(), asset.getAssetKey2()))
            return false;
        if (!AssetUtil.equals(preAsset.getKind(), asset.getKind()))
            return false;
        return true;
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, ZabbixUser entry) {
        return ZabbixAssetConvert.toAssetContainer(dsInstance, entry);
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(zabbixUserProvider);
    }
}