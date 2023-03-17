package com.baiyi.opscloud.datasource.kubernetes.converter;

import com.baiyi.opscloud.core.util.TimeUtil;
import com.baiyi.opscloud.core.util.enums.TimeZoneEnum;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.google.common.base.Joiner;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.kubernetes.api.model.ResourceRequirements;
import io.fabric8.kubernetes.api.model.apps.Deployment;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2021/6/25 4:22 下午
 * @Version 1.0
 */
public class DeploymentAssetConverter {

    public static Date toGmtDate(String time) {
        return TimeUtil.toDate(time, TimeZoneEnum.UTC);
    }

    private static final String CPU = "cpu";

    private static final String MEMORY = "memory";

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
                // namespace
                .assetKey2(namespace)
                .kind(entity.getKind())
                .assetType(DsAssetTypeConstants.KUBERNETES_DEPLOYMENT.name())
                .createdTime(toGmtDate(entity.getMetadata().getCreationTimestamp()))
                .build();

        /**
         *         resources:
         *           limits:
         *             cpu: "4"
         *             memory: 8Gi
         *           requests:
         *             cpu: "2"
         *             memory: 6Gi
         */
        Optional<Map<String, Quantity>> optionalLimits = entity
                .getSpec()
                .getTemplate()
                .getSpec()
                .getContainers()
                .stream()
                /**
                 * 无状态名称 name: account-1
                 * 容器名称 c: account
                 */
                .filter(c -> name.startsWith(c.getName()))
                .findFirst()
                .map(Container::getResources)
                .map(ResourceRequirements::getLimits);

        String cpu = "n/a";
        String memory = "n/a";

        if (optionalLimits.isPresent()) {
            Map<String, Quantity> limits = optionalLimits.get();
            if (limits.containsKey(CPU)) {
                cpu = limits.get(CPU).toString();
            }
            if (limits.containsKey(MEMORY)) {
                memory = limits.get(MEMORY).toString();
            }
        }
        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("replicas", entity.getSpec().getReplicas())
                .paramProperty("uid", entity.getMetadata().getUid())
                .paramProperty("resources.limits.cpu", cpu)
                .paramProperty("resources.limits.memory", memory)
                .build();
    }

}
