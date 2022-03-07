package com.baiyi.opscloud.datasource.kubernetes.converter;

import com.baiyi.opscloud.core.util.enums.TimeZoneEnum;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.core.util.TimeUtil;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.google.common.base.Joiner;
import io.fabric8.kubernetes.api.model.apps.Deployment;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2021/6/25 4:22 下午
 * @Version 1.0
 */
public class DeploymentAssetConverter {

    public static Date toGmtDate(String time) {
        return TimeUtil.toGmtDate(time, TimeZoneEnum.UTC);
    }

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, Deployment entity) {
        String namespace = entity.getMetadata().getNamespace();
        String name = entity.getMetadata().getName();
        /**
         * 为了兼容多集群中deployment名称相同导致无法拉取资产
         * 资产id使用联合键 namespace:deploymentName
         */
        String assetId = Joiner.on(":").join(namespace, name);

        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(assetId)
                .name(name)
                .assetKey(name)
                // entiry.getSpec().getTemplate().getSpec().getContainers().get(0).getImage() 容器模版镜像
                .assetKey2(namespace) // namespace
                .kind(entity.getKind())
                .assetType(DsAssetTypeConstants.KUBERNETES_DEPLOYMENT.name())
                .createdTime(toGmtDate(entity.getMetadata().getCreationTimestamp()))
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("replicas", entity.getSpec().getReplicas())
                .paramProperty("uid", entity.getMetadata().getUid())
                .build();
    }
}
