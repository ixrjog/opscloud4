package com.baiyi.opscloud.zabbix.convert;

import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixHostGroup;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/1 2:20 下午
 * @Since 1.0
 */
public class ZabbixHostAssetConvert {

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, com.baiyi.opscloud.zabbix.v5.entity.ZabbixHost.Host entity) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entity.getHostid())
                .name(entity.getName())
                .assetKey(entity.getInterfaces().get(0).getIp())
                //.assetKey2()
                .kind(String.valueOf(entity.getFlags()))
                .isActive(0 == entity.getStatus())
                .assetType(DsAssetTypeEnum.ZABBIX_HOST.name())
                .description(entity.getDescription())
                .build();
        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .build();
    }

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, ZabbixHostGroup.HostGroup entity) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entity.getGroupid())
                .name(entity.getName())
                .assetKey(entity.getGroupid())
                .kind(String.valueOf(entity.getFlags()))
                .assetType(DsAssetTypeEnum.ZABBIX_HOST_GROUP.name())
                .build();
        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .build();
    }
}
