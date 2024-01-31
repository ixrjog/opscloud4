package com.baiyi.opscloud.datasource.kubernetes.prod;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.base.BaseKubernetesTest;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesDeploymentDriver;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentSpec;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;

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
        KubernetesConfig kubernetesConfig = getConfigById(KubernetesClusterConfigs.ACK_FRANKFURT_DAILY);
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

    /**
     * 校验consul-agent preStop配置
     */
    @Test
    void test2() {
        KubernetesConfig kubernetesConfig = getConfigById(KubernetesClusterConfigs.ACK_FRANKFURT_DAILY);
        List<Deployment> deploymentList = KubernetesDeploymentDriver.list(kubernetesConfig.getKubernetes(), NAMESPACE);
        deploymentList.forEach(deployment -> {
                    print(deployment.getMetadata().getName());
                    List<Container> containers = Optional.of(deployment)
                            .map(Deployment::getSpec)
                            .map(DeploymentSpec::getTemplate)
                            .map(PodTemplateSpec::getSpec)
                            .map(PodSpec::getContainers)
                            .orElse(Lists.newArrayList());
                    if (!CollectionUtils.isEmpty(containers)) {
                        for (Container container : containers) {
                            if(container.getName().equals("consul-agent")){
                                Optional<LifecycleHandler> optionalLifecycleHandler = Optional.of(container)
                                        .map(Container::getLifecycle)
                                        .map(Lifecycle::getPreStop);
                                if (optionalLifecycleHandler.isPresent()) {
                                    print(optionalLifecycleHandler.get());
                                    print("---------------------------------------");
                                }
                            }
                        }
                    }
                }

        );
    }

}

