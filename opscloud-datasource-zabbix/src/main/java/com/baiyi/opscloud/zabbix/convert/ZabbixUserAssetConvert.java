package com.baiyi.opscloud.zabbix.convert;

import com.baiyi.opscloud.common.type.DsAssetTypeEnum;
import com.baiyi.opscloud.datasource.builder.AssetContainer;
import com.baiyi.opscloud.datasource.builder.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.zabbix.entry.ZabbixUser;
import com.baiyi.opscloud.zabbix.entry.ZabbixUserGroup;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/25 3:43 下午
 * @Since 1.0
 */
public class ZabbixUserAssetConvert {

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, ZabbixUser entry) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entry.getUserId())
                .name(entry.getName())
                .assetKey(entry.getAlias())
                .assetKey2(entry.getSurname())
                .kind(String.valueOf(entry.getType()))
                .assetType(DsAssetTypeEnum.ZABBIX_USER.name())
                .build();
        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .build();
    }

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, ZabbixUserGroup entry) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entry.getUserGroupId())
                .name(entry.getName())
                .assetKey(entry.getUserGroupId())
                .assetType(DsAssetTypeEnum.ZABBIX_USER_GROUP.name())
                .isActive("0".equals(entry.getStatus()))
                .kind("zabbixUserGroup")
                .build();
        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .build();
    }
}
