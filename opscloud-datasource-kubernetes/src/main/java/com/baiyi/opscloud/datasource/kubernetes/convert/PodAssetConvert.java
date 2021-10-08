package com.baiyi.opscloud.datasource.kubernetes.convert;

import com.baiyi.opscloud.datasource.util.enums.TimeZoneEnum;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.baiyi.opscloud.datasource.util.TimeUtil;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.Pod;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/6/25 1:16 下午
 * @Version 1.0
 */
public class PodAssetConvert {

    public static Date toGmtDate(String time) {
        return TimeUtil.toGmtDate(time, TimeZoneEnum.UTC);
    }

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, Pod entry) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entry.getMetadata().getUid()) // 资产id
                .name(entry.getMetadata().getName())
                .assetKey(entry.getStatus().getPodIP())    // podIp
                .assetKey2(entry.getMetadata().getNamespace()) // namespace
                .kind(entry.getKind())
                .assetType(DsAssetTypeEnum.KUBERNETES_POD.name())
                .createdTime(toGmtDate(entry.getMetadata().getCreationTimestamp()))
                .build();

        Date startTime = toGmtDate(entry.getStatus().getStartTime());
        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramChildren(toChildren(entry))
                .paramProperty("phase", entry.getStatus().getPhase())
                .paramProperty("startTime", com.baiyi.opscloud.common.util.TimeUtil.dateToStr(startTime))
                .paramProperty("nodeName", entry.getSpec().getNodeName())
                .paramProperty("restartCount",entry.getStatus().getContainerStatuses().get(0).getRestartCount())
                .paramProperty("hostIp", entry.getStatus().getHostIP())
                .paramProperty("image", entry.getStatus().getContainerStatuses().get(0).getImage())
                .paramProperty("imageId", entry.getStatus().getContainerStatuses().get(0).getImageID())
                .paramProperty("containerId",entry.getStatus().getContainerStatuses().get(0).getContainerID())
                .build();
    }

    private static List<AssetContainer> toChildren(Pod pod) {
        List<Container> containers = pod.getSpec().getContainers();
        if (CollectionUtils.isEmpty(containers)) return null;
        return containers.stream().map(c -> {
            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .name(c.getName())
                    .assetKey(pod.getMetadata().getName()) // podName
                    .build();
            return AssetContainerBuilder.newBuilder()
                    .paramAsset(asset).build();
        }).collect(Collectors.toList());
    }

}
