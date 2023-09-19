package com.baiyi.opscloud.datasource.kubernetes.prod;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.base.BaseKubernetesTest;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesDeploymentDriver;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

/**
 * @Author 修远
 * @Date 2023/9/15 10:39 AM
 * @Since 1.0
 */

@Slf4j
public class KubernetesProdTest extends BaseKubernetesTest {

    private final static String NAMESPACE = "prod";
    private final static String ENV_NAME = "prod";

    @Test
    void changeName() {
        KubernetesConfig kubernetesConfig = getConfigById(KubernetesClusterConfigs.ACK_FRANKFURT_PROD);
        List<Deployment> deploymentList = KubernetesDeploymentDriver.list(kubernetesConfig.getKubernetes(), NAMESPACE);
        deploymentList.forEach(deployment -> {
            try{
                String name = deployment.getMetadata().getLabels().get("app");
                if (name.endsWith("-" + ENV_NAME)) {
                    try {
                        String appName = name.substring(0, name.length() - ENV_NAME.length() - 1);
                        Optional<Container> optionalContainer =
                                deployment.getSpec().getTemplate().getSpec().getContainers().stream().filter(c -> c.getName().startsWith(appName)).findFirst();
                        if (optionalContainer.isPresent()) {
                            Container container = optionalContainer.get();
                            // resource
                            container.setName(appName);
                            try {
                                KubernetesDeploymentDriver.update(kubernetesConfig.getKubernetes(), NAMESPACE, deployment);
                            }catch (Exception e) {
                                print(deployment.getMetadata().getName());
                            }
                        } else {
                            print(deployment.getMetadata().getName());
                        }
                    } catch (Exception e) {
                        log.error(name);
                    }
                }
            }catch (Exception e) {
                log.error(deployment.getMetadata().getName());
            }
        });
    }
}
