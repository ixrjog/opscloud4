package com.baiyi.caesar.datasource.kubernetes.convert;

import com.baiyi.caesar.common.type.DsAssetTypeEnum;
import com.baiyi.caesar.datasource.builder.AssetContainer;
import com.baiyi.caesar.datasource.builder.AssetContainerBuilder;
import com.baiyi.caesar.datasource.util.TimeUtil;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstance;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstanceAsset;
import io.fabric8.kubernetes.api.model.Namespace;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2021/6/24 9:31 下午
 * @Version 1.0
 */
public class NamespaceAssetConvert {

    public static Date toGmtDate(String time) {
        return TimeUtil.toGmtDate(time, TimeUtil.Format.UTC);
    }

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, Namespace entry) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entry.getMetadata().getUid()) // 资产id
                .name(entry.getMetadata().getName())
                .assetKey(entry.getMetadata().getName())
                .kind(entry.getKind())
                .assetType(DsAssetTypeEnum.KUBERNETES_NAMESPACE.name())
                //.description(entry.getDescription())
                .createdTime(toGmtDate(entry.getMetadata().getCreationTimestamp()))
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("phase", entry.getStatus().getPhase())
                .build();
    }
}
