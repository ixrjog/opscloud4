package com.baiyi.opscloud.zabbix.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.datasource.ZabbixDsInstanceConfig;
import com.baiyi.opscloud.common.datasource.config.DsZabbixConfig;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.baiyi.opscloud.common.type.DsTypeEnum;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.datasource.factory.AssetProviderFactory;
import com.baiyi.opscloud.datasource.model.DsInstanceContext;
import com.baiyi.opscloud.datasource.provider.asset.AbstractAssetRelationProvider;
import com.baiyi.opscloud.datasource.provider.base.param.UniqueAssetParam;
import com.baiyi.opscloud.datasource.util.AssetUtil;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.zabbix.convert.ZabbixUserAssetConvert;
import com.baiyi.opscloud.zabbix.entry.ZabbixUser;
import com.baiyi.opscloud.zabbix.entry.ZabbixUserGroup;
import com.baiyi.opscloud.zabbix.handler.ZabbixUserGroupHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/28 3:03 下午
 * @Since 1.0
 */

@Component
public class ZabbixUserGroupProvider extends AbstractAssetRelationProvider<ZabbixUserGroup, ZabbixUser> {

    @Resource
    private ZabbixUserGroupHandler zabbixUserGroupHandler;

    @Resource
    private ZabbixUserGroupProvider zabbixUserGroupProvider;

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ZABBIX.name();
    }

    private DsZabbixConfig.Zabbix buildConfig(DatasourceConfig dsConfig) {
        return dsConfigFactory.build(dsConfig, ZabbixDsInstanceConfig.class).getZabbix();
    }

    @Override
    protected List<ZabbixUserGroup> listEntries(DsInstanceContext dsInstanceContext, ZabbixUser target) {
        DsZabbixConfig.Zabbix zabbix = buildConfig(dsInstanceContext.getDsConfig());
        return zabbixUserGroupHandler.listGroupsByUser(zabbix, target);
    }

    @Override
    protected List<ZabbixUserGroup> listEntries(DsInstanceContext dsInstanceContext) {
        return zabbixUserGroupHandler.listGroups(buildConfig(dsInstanceContext.getDsConfig()));
    }

    @Override
    protected ZabbixUserGroup getEntry(DsInstanceContext dsInstanceContext, UniqueAssetParam param) {
        return zabbixUserGroupHandler.getGroupById(buildConfig(dsInstanceContext.getDsConfig()), param.getAssetId());
    }

    @Override
    @SingleTask(name = "PullZabbixUserGroup", lockTime = "5m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeEnum.ZABBIX_USER_GROUP.getType();
    }

    @Override
    public String getTargetAssetKey() {
        return DsAssetTypeEnum.ZABBIX_USER.getType();
    }

    @Override
    protected boolean equals(DatasourceInstanceAsset asset, DatasourceInstanceAsset preAsset) {
        if (!AssetUtil.equals(preAsset.getName(), asset.getName()))
            return false;
        return true;
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, ZabbixUserGroup entry) {
        return ZabbixUserAssetConvert.toAssetContainer(dsInstance, entry);
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(zabbixUserGroupProvider);
    }
}
