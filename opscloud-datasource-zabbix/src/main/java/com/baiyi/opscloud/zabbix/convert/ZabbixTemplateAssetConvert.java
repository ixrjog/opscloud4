package com.baiyi.opscloud.zabbix.convert;

import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.datasource.provider.base.asset.IAssetRelation;
import com.baiyi.opscloud.datasource.provider.base.param.UniqueAssetParam;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.zabbix.entry.ZabbixTemplate;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/1 3:09 下午
 * @Since 1.0
 */


public class ZabbixTemplateAssetConvert implements IAssetRelation {

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, ZabbixTemplate entry) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entry.getTemplateId())
                .name(entry.getName())
                .assetKey(entry.getTemplateId())
                .assetKey2(entry.getHost())
                .assetType(DsAssetTypeEnum.ZABBIX_TEMPLATE.name())
                .kind("zabbixTemplate")
                .description(entry.getDescription())
                .build();
        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .build();
    }

    @Override
    public void buildAssetRelation(int dsInstanceId, UniqueAssetParam param) {

    }
}
