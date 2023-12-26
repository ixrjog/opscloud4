package com.baiyi.opscloud.datasource.kubernetes.converter;

import com.baiyi.opscloud.core.util.TimeUtil;
import com.baiyi.opscloud.core.util.enums.TimeZoneEnum;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.kubernetes.api.model.ResourceRequirements;
import io.fabric8.kubernetes.api.model.apps.Deployment;

import java.util.*;

/**
 * @Author baiyi
 * @Date 2021/6/25 4:22 下午
 * @Version 1.0
 */
public class DeploymentAssetConverter {

    public static Date toUtcDate(String time) {
        return TimeUtil.toDate(time, TimeZoneEnum.UTC);
    }

    private static final String CPU = "cpu";

    private static final String MEMORY = "memory";

    private static final String JAVA_OPTS = "JAVA_OPTS";

    public static final String SIDECAR_ISTIO_IO_INJECT = "sidecar.istio.io/inject";

    private static final String ENV_JAVA_OPTS = "env.java.opts";

    private static final String ENV_JAVA_OPTS_TAG = "env.java.opts.tag";

    // private static final String[] JVM_ARGS_PREFIX = {"-Xms", "-Xmx", "-Xmn", "-XX:MetaspaceSize", "-XX:MaxMetaspaceSize"};
    private static final String[] JVM_ARGS_PREFIX = {"-Xms", "-Xmx", "-Xmn"};

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, Deployment entity) {
        final String namespace = entity.getMetadata().getNamespace();
        final String name = entity.getMetadata().getName();
        /*
         * 为了兼容多集群中deployment名称相同导致无法拉取资产
         * 资产id使用联合键 namespace:deployment.name
         */
        final String assetId = Joiner.on(":").join(namespace, name);

        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(assetId)
                .name(name)
                .assetKey(name)
                // namespace
                .assetKey2(namespace)
                .kind(entity.getKind())
                .assetType(DsAssetTypeConstants.KUBERNETES_DEPLOYMENT.name())
                .createdTime(toUtcDate(entity.getMetadata().getCreationTimestamp()))
                .build();

        AssetContainerBuilder assetContainerBuilder = AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("replicas", entity.getSpec().getReplicas())
                .paramProperty("uid", entity.getMetadata().getUid());

        Optional<Container> optionalContainer = entity
                .getSpec()
                .getTemplate()
                .getSpec()
                .getContainers()
                .stream()
                /*
                 * 无状态名称 name: account-1
                 * 容器名称 c: account
                 */
                .filter(c -> name.startsWith(c.getName()))
                .findFirst();

        if (optionalContainer.isPresent()) {
            Container container = optionalContainer.get();
            setPropertyWithLimits(assetContainerBuilder, container);
            setPropertyWithJavaOpts(assetContainerBuilder, container);
        }

        // Istio Envoy
        Map<String, String> labels = entity.getSpec().getTemplate().getMetadata().getLabels();
        if (labels.containsKey(SIDECAR_ISTIO_IO_INJECT)) {
            assetContainerBuilder.paramProperty(SIDECAR_ISTIO_IO_INJECT, "true".equals(labels.get(SIDECAR_ISTIO_IO_INJECT)));
        }
        return assetContainerBuilder.build();
    }

    private static void setPropertyWithLimits(AssetContainerBuilder assetContainerBuilder, Container container) {
        /*
         *         resources:
         *           limits:
         *             cpu: "4"
         *             memory: 8Gi
         *           requests:
         *             cpu: "2"
         *             memory: 6Gi
         */
        Optional<Map<String, Quantity>> optionalLimits = Optional.of(container)
                .map(Container::getResources)
                .map(ResourceRequirements::getLimits);

        if (optionalLimits.isPresent()) {
            Map<String, Quantity> limits = optionalLimits.get();
            if (limits.containsKey(CPU)) {
                assetContainerBuilder.paramProperty("resources.limits.cpu", limits.get(CPU).toString());
            }
            if (limits.containsKey(MEMORY)) {
                assetContainerBuilder.paramProperty("resources.limits.memory", limits.get(MEMORY).toString());
            }
        }
    }

    private static void setPropertyWithJavaOpts(AssetContainerBuilder assetContainerBuilder, Container container) {
        // Optional<EnvVar> optionalEnvVar =
        container.getEnv()
                .stream()
                .filter(e -> JAVA_OPTS.equals(e.getName()))
                .findFirst()
                .ifPresent(javaOptsEnvVar -> {
                    List<String> javaOptsList = Splitter.onPattern(" |\\n").omitEmptyStrings().splitToList(javaOptsEnvVar.getValue());
                    assetContainerBuilder.paramProperty(ENV_JAVA_OPTS, Joiner.on("\n").skipNulls().join(javaOptsList));
                    List<String> tags = javaOptsList.stream().filter(e ->
                            Arrays.stream(JVM_ARGS_PREFIX).anyMatch(e::startsWith)
                    ).toList();
                    assetContainerBuilder.paramProperty(ENV_JAVA_OPTS_TAG, Joiner.on(" ").skipNulls().join(tags));
                });
    }

}