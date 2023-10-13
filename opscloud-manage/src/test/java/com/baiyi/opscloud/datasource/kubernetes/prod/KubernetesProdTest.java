package com.baiyi.opscloud.datasource.kubernetes.prod;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.base.BaseKubernetesTest;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesDeploymentDriver;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.EnvVar;
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
            try {
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
                            } catch (Exception e) {
                                print(deployment.getMetadata().getName());
                            }
                        } else {
                            print(deployment.getMetadata().getName());
                        }
                    } catch (Exception e) {
                        log.error(name);
                    }
                }
            } catch (Exception e) {
                log.error(deployment.getMetadata().getName());
            }
        });
    }


    @Test
    void updateArmsAckOne() {
        KubernetesConfig kubernetesConfig = getConfigById(KubernetesClusterConfigs.EKS_PROD);
        List<Deployment> deploymentList = KubernetesDeploymentDriver.list(kubernetesConfig.getKubernetes(), NAMESPACE);
//        List<Deployment> deploymentList = Lists.newArrayList();
//        Deployment leoDemo1Deployment = KubernetesDeploymentDriver.get(kubernetesConfig.getKubernetes(), NAMESPACE, "leo-demo-1");
//        deploymentList.add(leoDemo1Deployment);
//        Deployment leoDemo2Deployment = KubernetesDeploymentDriver.get(kubernetesConfig.getKubernetes(), NAMESPACE, "leo-demo-2");
//        deploymentList.add(leoDemo2Deployment);
//        Deployment leoDemo3Deployment = KubernetesDeploymentDriver.get(kubernetesConfig.getKubernetes(), NAMESPACE, "leo-demo-3");
//        deploymentList.add(leoDemo3Deployment);

        Deployment leoDemoDeployment = KubernetesDeploymentDriver.get(kubernetesConfig.getKubernetes(), NAMESPACE, "leo-demo-canary");

        Container leoDemoContainer =
                leoDemoDeployment.getSpec().getTemplate().getSpec().getContainers().stream().filter(c -> c.getName().startsWith("leo-demo")).findFirst().get();

        List<EnvVar> leoDemoEnvVars = leoDemoContainer.getEnv();
        EnvVar leoDemoGroupEnv = leoDemoEnvVars.stream().filter(env -> env.getName().equals("GROUP")).findFirst().get();

        deploymentList.forEach(deployment -> {
            String name = deployment.getMetadata().getLabels().get("app");
            if (name.endsWith("-" + ENV_NAME)) {
                try {
                    String appName = name.substring(0, name.length() - ENV_NAME.length() - 1);
                    Optional<Container> optionalContainer =
                            deployment.getSpec().getTemplate().getSpec().getContainers().stream().filter(c -> c.getName().startsWith(appName)).findFirst();
                    if (optionalContainer.isPresent()) {
                        Container container = optionalContainer.get();
                        // env
                        List<EnvVar> envVars = container.getEnv();
                        // GROUP
                        if (envVars.stream().noneMatch(env -> env.getName().equals("GROUP"))) {
                            envVars.add(0, leoDemoGroupEnv);
                        }else {
                            EnvVar groupEnv = envVars.stream().filter(env -> env.getName().equals("GROUP")).findFirst().get();
                            envVars.remove(groupEnv);
                            envVars.add(0, leoDemoGroupEnv);
                        }
                        // SPRING_CUSTOM_OPTS
                        Optional<EnvVar> agentEnv = envVars.stream().filter(env -> env.getName().equals("SPRING_CUSTOM_OPTS")).findFirst();
                        agentEnv.ifPresent(envVar -> {
                            if (envVar.getValue().equals("-Dspring.cloud.consul.discovery.hostname=$(HOSTIP) -Dspring.cloud.consul.host=$(HOSTIP)")) {
                                envVar.setValue("-Dspring.cloud.consul.discovery.hostname=$(HOSTIP) -Dspring.cloud.consul.host=$(HOSTIP) -Dapollo.label=$(GROUP)");
                            } else {
                                print(deployment.getMetadata().getName() + "SPRING_CUSTOM_OPTS: " + envVar.getValue());
                            }
                        });
                        try {
                            KubernetesDeploymentDriver.update(kubernetesConfig.getKubernetes(), NAMESPACE, deployment);
                        } catch (Exception e) {
                            print(deployment.getMetadata().getName());
                        }
                    } else {
                        print(deployment.getMetadata().getName() + "不存在");
                    }
                } catch (Exception e) {
                    log.error(name);
                }
            }
        });

    }
}
