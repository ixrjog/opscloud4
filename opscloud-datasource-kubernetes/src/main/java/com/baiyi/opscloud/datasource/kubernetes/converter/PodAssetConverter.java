package com.baiyi.opscloud.datasource.kubernetes.converter;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.common.util.NewTimeUtil;
import com.baiyi.opscloud.common.util.time.AgoUtil;
import com.baiyi.opscloud.core.util.TimeUtil;
import com.baiyi.opscloud.core.util.enums.TimeZoneEnum;
import com.baiyi.opscloud.datasource.kubernetes.model.ContainerBO;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import io.fabric8.kubernetes.api.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/6/25 1:16 下午
 * @Version 1.0
 */
@Slf4j
public class PodAssetConverter {

    public static Date toUtcDate(String time) {
        return TimeUtil.toDate(time, TimeZoneEnum.UTC);
    }

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, KubernetesConfig.Kubernetes kubernetes, Pod entity) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                // 资产id
                .assetId(entity.getMetadata().getUid())
                .name(entity.getMetadata().getName())
                // podIp
                .assetKey(entity.getStatus().getPodIP())
                // namespace
                .assetKey2(entity.getMetadata().getNamespace())
                .kind(entity.getKind()).assetType(DsAssetTypeConstants.KUBERNETES_POD.name())
                .createdTime(toUtcDate(entity.getMetadata().getCreationTimestamp()))
                .build();

        Date startTime = toUtcDate(entity.getStatus().getStartTime());
        Map<String, Boolean> podStatusMap = entity.getStatus().getConditions()
                .stream()
                .collect(Collectors.toMap(PodCondition::getType, a -> Boolean.valueOf(a.getStatus()), (k1, k2) -> k1));
        Optional<String> statusOptional = podStatusMap.keySet()
                .stream()
                .filter(k -> !podStatusMap.get(k))
                .findFirst();

        ContainerStatus containerStatus = buildContainerStatus(kubernetes, entity);

        Optional<ContainerStateTerminated> optionalContainerStateTerminated = Optional.of(containerStatus)
                .map(ContainerStatus::getState)
                .map(ContainerState::getTerminated);
        AssetContainer assetContainer = AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramChildren(toChildren(kubernetes, entity))
                .paramProperty("phase", entity.getStatus().getPhase())
                .paramProperty("startTime", NewTimeUtil.parse(startTime))
                .paramProperty("nodeName", entity.getSpec().getNodeName())
                .paramProperty("restartCount", containerStatus.getRestartCount())
                .paramProperty("hostIp", entity.getStatus().getHostIP())
                .paramProperty("image", containerStatus.getImage())
                .paramProperty("imageId", containerStatus.getImageID())
                .paramProperty("containerId", containerStatus.getContainerID())
                .paramProperty("podScheduled", podStatusMap.get("PodScheduled"))
                .paramProperty("containersReady", podStatusMap.get("ContainersReady"))
                .paramProperty("initialized", podStatusMap.get("Initialized"))
                .paramProperty("ready", podStatusMap.get("Ready"))
                .paramProperty("status", statusOptional.isEmpty())
                .paramProperty("reason", entity.getStatus().getReason())
                .paramProperty("terminating", optionalContainerStateTerminated.isPresent())
                .build();
        assetContainer.setAgo(AgoUtil.format(startTime));
        return assetContainer;
    }

    private static ContainerStatus buildContainerStatus(KubernetesConfig.Kubernetes kubernetes, Pod pod) {
        try {
            if (!CollectionUtils.isEmpty(pod.getStatus().getContainerStatuses())) {
                List<ContainerStatus> containerStatusList = pod.getStatus()
                        .getContainerStatuses()
                        .stream()
                        .filter(s -> tryIgnoreName(kubernetes, s.getName()))
                        .toList();
                if (!CollectionUtils.isEmpty(containerStatusList)) {
                    return containerStatusList.getFirst();
                }
            }
        } catch (Exception e) {
            log.error("获取容器状态错误: {}", e.getMessage());
        }
        return ContainerBO.ContainerStatus.builder().build().toContainerStatus();
    }

    private static List<AssetContainer> toChildren(KubernetesConfig.Kubernetes kubernetes, Pod pod) {
        List<Container> containers = pod.getSpec().getContainers();
        if (CollectionUtils.isEmpty(containers)) {
            return null;
        }
        return containers.stream().filter(c -> tryIgnoreName(kubernetes, c.getName())).toList()
                .stream().map(c -> {
                    DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                            .name(c.getName())
                            // podName
                            .assetKey(pod.getMetadata().getName())
                            .build();
                    return AssetContainerBuilder.newBuilder().paramAsset(asset).build();
                }).collect(Collectors.toList());
    }

    private static boolean tryIgnoreName(KubernetesConfig.Kubernetes kubernetes, String containerName) {
        if (kubernetes.getContainer() == null || CollectionUtils.isEmpty(kubernetes.getContainer().getIgnore())) {
            return true;
        }
        for (String name : kubernetes.getContainer().getIgnore()) {
            if (containerName.startsWith(name)) {
                return false;
            }
        }
        return true;
    }

}