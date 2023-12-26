package com.baiyi.opscloud.datasource.kubernetes.converter;

import com.baiyi.opscloud.core.util.TimeUtil;
import com.baiyi.opscloud.core.util.enums.TimeZoneEnum;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.google.common.base.Joiner;
import io.fabric8.istio.api.networking.v1alpha3.DestinationRule;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2023/10/8 14:02
 * @Version 1.0
 */
public class DestinationRuleConverter {

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, DestinationRule entity) {
        final String namespace = entity.getMetadata().getNamespace();
        final String name = entity.getMetadata().getName();
        /*
         * 为了兼容多集群中 Ingress 名称相同导致无法拉取资产
         * 资产id使用联合键 namespace: ingress.name
         */
        final String assetId = Joiner.on(":").join(namespace, name);
        final Date createTime = TimeUtil.toDate(entity.getMetadata().getCreationTimestamp(), TimeZoneEnum.UTC);

        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(assetId)
                .name(name)
                .assetKey(name)
                // namespace
                .assetKey2(namespace)
                .kind(entity.getKind())
                .assetType(DsAssetTypeConstants.ISTIO_DESTINATION_RULE.name())
                .createdTime(createTime)
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("uid", entity.getMetadata().getUid())
                .build();
    }

}