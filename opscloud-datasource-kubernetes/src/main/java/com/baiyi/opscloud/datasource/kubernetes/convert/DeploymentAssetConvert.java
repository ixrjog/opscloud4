package com.baiyi.opscloud.datasource.kubernetes.convert;

import com.baiyi.opscloud.datasource.util.enums.TimeZoneEnum;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.datasource.util.TimeUtil;
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
public class DeploymentAssetConvert {

    public static Date toGmtDate(String time) {
        return TimeUtil.toGmtDate(time, TimeZoneEnum.UTC);
    }

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, Deployment entry) {

        String namespace = entry.getMetadata().getNamespace();
        String name = entry.getMetadata().getName();
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
                // entry.getSpec().getTemplate().getSpec().getContainers().get(0).getImage() 容器模版镜像
                .assetKey2(namespace) // namespace
                .kind(entry.getKind())
                .assetType(DsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name())
                .createdTime(toGmtDate(entry.getMetadata().getCreationTimestamp()))
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("replicas", entry.getSpec().getReplicas())
                .paramProperty("uid", entry.getMetadata().getUid())
                .build();
    }
}
