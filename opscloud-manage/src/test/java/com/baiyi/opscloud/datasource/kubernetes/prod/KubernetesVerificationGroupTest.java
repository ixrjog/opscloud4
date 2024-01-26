package com.baiyi.opscloud.datasource.kubernetes.prod;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.base.BaseKubernetesTest;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesDeploymentDriver;
import com.google.common.collect.Maps;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.PodTemplateSpec;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentSpec;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2024/1/26 10:35
 * @Version 1.0
 */
@Slf4j
public class KubernetesVerificationGroupTest extends BaseKubernetesTest {

    private final static String NAMESPACE = "daily";
    private final static String ENV_NAME = "prod";


    @Test
    void changeName() {
        KubernetesConfig kubernetesConfig = getConfigById(KubernetesClusterConfigs.ACK_DAILY );
        List<Deployment> deploymentList = KubernetesDeploymentDriver.list(kubernetesConfig.getKubernetes(), NAMESPACE);

        deploymentList.forEach(deployment -> {
                    Map<String, String> labels = Optional.of(deployment)
                            .map(Deployment::getSpec)
                            .map(DeploymentSpec::getTemplate)
                            .map(PodTemplateSpec::getMetadata)
                            .map(ObjectMeta::getLabels)
                            .orElse(Maps.newHashMap());

                    if (labels.containsKey("group")) {
                        log.info("{} , group={}", deployment.getMetadata().getName(), labels.get("group"));
                    } else {
                        log.error("{} , group is absent", deployment.getMetadata().getName());
                    }
                }

        );


    }

}

