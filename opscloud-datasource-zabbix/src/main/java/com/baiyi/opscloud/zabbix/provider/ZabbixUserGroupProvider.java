package com.baiyi.opscloud.zabbix.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.ZabbixConfig;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.AbstractAssetRelationProvider;
import com.baiyi.opscloud.core.util.AssetUtil;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.baiyi.opscloud.zabbix.v5.drive.ZabbixV5UserGroupDrive;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixUser;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixUserGroup;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_ZABBIX_USER_GROUP;

/**
 * @Author 修远
 * @Date 2021/6/28 3:03 下午
 * @Since 1.0
 */

@Component
public class ZabbixUserGroupProvider extends AbstractAssetRelationProvider<ZabbixUserGroup.UserGroup, ZabbixUser.User> {

    @Resource
    private ZabbixV5UserGroupDrive zabbixV5UserGroupDrive;

    @Resource
    private ZabbixUserGroupProvider zabbixUserGroupProvider;

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ZABBIX.name();
    }

    private ZabbixConfig.Zabbix buildConfig(DatasourceConfig dsConfig) {
        return dsConfigHelper.build(dsConfig, ZabbixConfig.class).getZabbix();
    }

    @Override
    protected List<ZabbixUserGroup.UserGroup> listEntities(DsInstanceContext dsInstanceContext, ZabbixUser.User target) {
        ZabbixConfig.Zabbix zabbix = buildConfig(dsInstanceContext.getDsConfig());
        return zabbixV5UserGroupDrive.listByUser(zabbix, target);
    }

    @Override
    protected List<ZabbixUserGroup.UserGroup> listEntities(DsInstanceContext dsInstanceContext) {
        return zabbixV5UserGroupDrive.list(buildConfig(dsInstanceContext.getDsConfig()));
    }

    @Override
    @SingleTask(name = PULL_ZABBIX_USER_GROUP, lockTime = "5m")
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
    public void afterPropertiesSet() {
        AssetProviderFactory.register(zabbixUserGroupProvider);
    }
}
