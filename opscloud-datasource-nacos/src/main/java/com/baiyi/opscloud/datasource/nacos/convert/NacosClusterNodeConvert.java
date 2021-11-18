package com.baiyi.opscloud.datasource.nacos.convert;

import com.baiyi.opscloud.datasource.nacos.entity.NacosCluster;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;

/**
 * @Author baiyi
 * @Date 2021/11/12 2:37 下午
 * @Version 1.0
 */
public class NacosClusterNodeConvert {

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, NacosCluster.Node entity) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entity.getAddress())
                .name(entity.getAddress())
                .assetKey(entity.getIp())
                //.assetKey2()
                .isActive(true)
                .assetType(DsAssetTypeEnum.NACOS_CLUSTER_NODE.name())
                .kind("node")
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("state", entity.getState())
                .paramProperty("port", entity.getPort())
                .paramProperty("failAccessCnt", entity.getFailAccessCnt())
                .paramProperty("supportRemoteMetrics",entity.getAbilities().getConfigAbility().getSupportRemoteMetrics())
                .paramProperty("supportJraft",entity.getAbilities().getNamingAbility().getSupportJraft())
                .paramProperty("supportRemoteConnection",entity.getAbilities().getRemoteAbility().getSupportRemoteConnection())
                .build();
    }

}
