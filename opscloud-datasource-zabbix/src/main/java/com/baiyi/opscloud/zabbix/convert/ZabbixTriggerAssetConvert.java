package com.baiyi.opscloud.zabbix.convert;

import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.zabbix.entry.ZabbixTrigger;

import java.util.Date;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/2 3:44 下午
 * @Since 1.0
 */
public class ZabbixTriggerAssetConvert {

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, ZabbixTrigger entry) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entry.getTriggerid())
                .name(entry.getDescription())
                .assetKey(entry.getTriggerid())
                .kind(String.valueOf(entry.getPriority()))
                .createdTime(new Date(entry.getLastchange() * 1000))
                .assetType(DsAssetTypeEnum.ZABBIX_TRIGGER.name())
                .build();
        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .build();
    }
}
