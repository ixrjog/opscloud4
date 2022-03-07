package com.baiyi.opscloud.datasource.kubernetes.converter;

import com.baiyi.opscloud.core.util.TimeUtil;
import com.baiyi.opscloud.core.util.enums.TimeZoneEnum;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import io.fabric8.kubernetes.api.model.Node;
import io.fabric8.kubernetes.api.model.NodeAddress;

import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/12/17 1:49 PM
 * @Version 1.0
 */
public class NodeAssetConverter {

    public static Date toGmtDate(String time) {
        return TimeUtil.toGmtDate(time, TimeZoneEnum.UTC);
    }

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, Node entity) {
        Map<String, NodeAddress> addressMap
                = entity.getStatus().getAddresses().stream().collect(Collectors.toMap(NodeAddress::getType, a -> a, (k1, k2) -> k1));

        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entity.getMetadata().getUid()) // 资产id
                .name(entity.getMetadata().getName())
                .assetKey(addressMap.containsKey("InternalIP") ? addressMap.get("InternalIP").getAddress() : entity.getMetadata().getName())
                .assetKey2(addressMap.containsKey("Hostname") ? addressMap.get("Hostname").getAddress() : null)
                .kind(entity.getKind())
                .assetType(DsAssetTypeConstants.KUBERNETES_NODE.name())
                .createdTime(toGmtDate(entity.getMetadata().getCreationTimestamp()))
                .build();
        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("cpu", entity.getStatus().getCapacity().get("cpu"))
                .paramProperty("memory", entity.getStatus().getCapacity().get("memory"))
                .paramProperty("pods", entity.getStatus().getCapacity().get("pods"))
                .build();
    }

}
