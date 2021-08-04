package com.baiyi.opscloud.zabbix.convert;

import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.zabbix.entry.ZabbixHost;
import com.baiyi.opscloud.zabbix.entry.ZabbixHostGroup;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/1 2:20 下午
 * @Since 1.0
 */
public class ZabbixHostAssetConvert {

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, ZabbixHost entry) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entry.getHostId())
                .name(entry.getName())
                .assetKey(entry.getInterfaces().get(0).getIp())
                //.assetKey2()
                .kind(String.valueOf(entry.getFlags()))
                .isActive(0 == entry.getStatus())
                .assetType(DsAssetTypeEnum.ZABBIX_HOST.name())
                .description(entry.getDescription())
                .build();
        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .build();
    }

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, ZabbixHostGroup entry) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entry.getGroupId())
                .name(entry.getName())
                .assetKey(entry.getGroupId())
                .kind(String.valueOf(entry.getFlags()))
                .assetType(DsAssetTypeEnum.ZABBIX_HOST_GROUP.name())
                .build();
        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .build();
    }
}
