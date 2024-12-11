package com.baiyi.opscloud.datasource.kubernetes.util;

import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.PodSpec;
import io.fabric8.kubernetes.api.model.PodTemplateSpec;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentSpec;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/4/23 下午3:41
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class KubeUtil {

    public static int getReplicas(Deployment deployment) {
        return Optional.of(deployment)
                .map(Deployment::getSpec)
                .map(DeploymentSpec::getReplicas)
                .orElse(0);
    }

    public static void setReplicas(Deployment deployment, int replicas) {
        deployment.getSpec()
                .setReplicas(replicas);
    }

    /**
     * 从Deployment中找出应用容器
     *
     * @param deployment
     * @return
     */
    public static Optional<Container> findAppContainerOf(Deployment deployment) {
        if (deployment == null) {
            return Optional.empty();
        }
        final String namespace = Optional.of(deployment)
                .map(Deployment::getMetadata)
                .map(ObjectMeta::getNamespace)
                .orElse("");
        final String deploymentName = Optional.of(deployment)
                .map(Deployment::getMetadata)
                .map(ObjectMeta::getName)
                .orElse("");
        List<Container> containers = Optional.of(deployment)
                .map(Deployment::getSpec)
                .map(DeploymentSpec::getTemplate)
                .map(PodTemplateSpec::getSpec)
                .map(PodSpec::getContainers)
                .orElse(Collections.emptyList());

        if (CollectionUtils.isEmpty(containers)) {
            return Optional.empty();
        }
        Optional<Container> optionalContainer = containers.stream()
                .filter(e -> e.getName()
                        .equals(deploymentName))
                .findFirst();
        if (optionalContainer.isPresent()) {
            return optionalContainer;
        }
        return containers.stream()
                .filter(e -> e.getName()
                        .equals(getAppNameOf(deployment)))
                .findFirst();
    }

    private static String getAppNameOf(Deployment deployment) {
        String deploymentName = deployment.getMetadata()
                .getName();
        Optional<Map<String, String>> optionalLabels = Optional.of(deployment)
                .map(Deployment::getMetadata)
                .map(ObjectMeta::getLabels);

        if (optionalLabels.isEmpty()) {
            return deploymentName;
        }
        String app = "";
        if (optionalLabels.get()
                .containsKey("app")) {
            app = optionalLabels.get()
                    .get("app");
        }
        if (!StringUtils.hasText(app)) {
            return deploymentName;
        }
        final String env = optionalLabels.get()
                .containsKey("env") ? optionalLabels.get()
                .get("env") : deployment.getMetadata()
                .getNamespace();
        // 移除环境后缀
        return org.apache.commons.lang3.StringUtils.removeEnd(app, "-" + env);
    }

    private static String getAppNameOfV2(Deployment deployment) {
        String deploymentName = deployment.getMetadata()
                .getName();
        Optional<Map<String, String>> optionalLabels = Optional.of(deployment)
                .map(Deployment::getMetadata)
                .map(ObjectMeta::getLabels);

        if (optionalLabels.isEmpty()) {
            return deploymentName;
        }

        if (optionalLabels.get()
                .containsKey("app")) {
            final String app = optionalLabels.get()
                    .get("app");
            final String env = optionalLabels.get()
                    .containsKey("env") ? optionalLabels.get()
                    .get("env") : deployment.getMetadata()
                    .getNamespace();
            // 移除环境后缀
            return org.apache.commons.lang3.StringUtils.removeEnd(app, "-" + env);
        } else {
            return deploymentName;
        }

    }

}
