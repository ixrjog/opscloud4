package com.baiyi.opscloud.zabbix.convert;

import com.baiyi.opscloud.common.type.DsAssetTypeEnum;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.zabbix.entry.ZabbixTrigger;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/2 3:44 下午
 * @Since 1.0
 */
public class ZabbixTriggerAssetConvert {

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, ZabbixTrigger entry) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entry.getTriggerId())
                .name(entry.getDescription())
                .assetKey(entry.getTriggerId())
                .kind(String.valueOf(entry.getPriority()))
                .createdTime(entry.getLastChange())
                .assetType(DsAssetTypeEnum.ZABBIX_TRIGGER.name())
                .build();
        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .build();
    }
}
