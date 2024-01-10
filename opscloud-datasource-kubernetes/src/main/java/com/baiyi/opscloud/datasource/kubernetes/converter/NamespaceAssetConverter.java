package com.baiyi.opscloud.datasource.kubernetes.converter;

import com.baiyi.opscloud.core.util.enums.TimeZoneEnum;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.core.util.TimeUtil;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import io.fabric8.kubernetes.api.model.Namespace;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2021/6/24 9:31 下午
 * @Version 1.0
 */
public class NamespaceAssetConverter {

    public static Date toUtcDate(String time) {
        return TimeUtil.toDate(time, TimeZoneEnum.UTC);
    }

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, Namespace entity) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                // 资产id
                .assetId(entity.getMetadata().getUid())
                .name(entity.getMetadata().getName())
                .assetKey(entity.getMetadata().getName())
                .kind(entity.getKind())
                .assetType(DsAssetTypeConstants.KUBERNETES_NAMESPACE.name())
                .createdTime(toUtcDate(entity.getMetadata().getCreationTimestamp()))
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("phase", entity.getStatus().getPhase())
                .build();
    }

}