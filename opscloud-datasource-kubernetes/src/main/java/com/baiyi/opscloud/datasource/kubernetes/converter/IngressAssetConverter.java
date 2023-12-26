package com.baiyi.opscloud.datasource.kubernetes.converter;

import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.core.util.TimeUtil;
import com.baiyi.opscloud.core.util.enums.TimeZoneEnum;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.google.common.base.Joiner;
import io.fabric8.kubernetes.api.model.networking.v1.Ingress;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2023/7/6 14:31
 * @Version 1.0
 */
public class IngressAssetConverter {

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, Ingress entity) {
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
                .assetType(DsAssetTypeConstants.KUBERNETES_INGRESS.name())
                .createdTime(createTime)
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("uid", entity.getMetadata().getUid())
                .paramProperty("rules", JSONUtil.writeValueAsString(entity.getSpec().getRules()))
                .paramProperty("ingressClassName", entity.getSpec().getIngressClassName())
                .build();
    }

}