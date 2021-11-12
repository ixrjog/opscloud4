package com.baiyi.opscloud.datasource.nacos.convert;

import com.baiyi.opscloud.datasource.nacos.entry.NacosCluster;
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

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, NacosCluster.Node entry) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entry.getAddress())
                .name(entry.getAddress())
                .assetKey(entry.getIp())
                //.assetKey2()
                .isActive(true)
                .assetType(DsAssetTypeEnum.NACOS_CLUSTER_NODE.name())
                .kind("node")
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("state", entry.getState())
                .paramProperty("port", entry.getPort())
                .paramProperty("failAccessCnt", entry.getFailAccessCnt())
                .paramProperty("supportRemoteMetrics",entry.getAbilities().getConfigAbility().getSupportRemoteMetrics())
                .paramProperty("supportJraft",entry.getAbilities().getNamingAbility().getSupportJraft())
                .paramProperty("supportRemoteConnection",entry.getAbilities().getRemoteAbility().getSupportRemoteConnection())
                .build();
    }

}
