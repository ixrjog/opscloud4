package com.baiyi.opscloud.datasource.kubernetes.convert;

import com.baiyi.opscloud.common.type.DsAssetTypeEnum;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.datasource.util.TimeUtil;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import io.fabric8.kubernetes.api.model.apps.Deployment;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2021/6/25 4:22 下午
 * @Version 1.0
 */
public class DeploymentAssetConvert {

    public static Date toGmtDate(String time) {
        return TimeUtil.toGmtDate(time, TimeUtil.Format.UTC);
    }

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, Deployment entry) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entry.getMetadata().getUid()) // 资产id
                .name(entry.getMetadata().getName())
                .assetKey(entry.getMetadata().getName())
                // entry.getSpec().getTemplate().getSpec().getContainers().get(0).getImage() 容器模版镜像
                .assetKey2(entry.getMetadata().getNamespace()) // namespace
                .kind(entry.getKind())
                .assetType(DsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name())
                .createdTime(toGmtDate(entry.getMetadata().getCreationTimestamp()))
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("replicas", entry.getSpec().getReplicas())
                .build();
    }
}
