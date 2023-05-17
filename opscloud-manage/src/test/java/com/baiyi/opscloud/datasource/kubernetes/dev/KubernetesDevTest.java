package com.baiyi.opscloud.datasource.kubernetes.dev;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.base.BaseKubernetesTest;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesDeploymentDriver;
import com.google.common.base.Joiner;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @Author 修远
 * @Date 2023/5/12 11:09 AM
 * @Since 1.0
 */

@Slf4j
public class KubernetesDevTest extends BaseKubernetesTest {

    private final static String NAMESPACE = "dev";

    private final static String AWS_NAMESPACE = "ci";

    @Test
    void updatePods() {
        KubernetesConfig kubernetesConfig = getConfigById(KubernetesClusterConfigs.EKS_TEST);
        List<Deployment> deploymentList = KubernetesDeploymentDriver.list(kubernetesConfig.getKubernetes(), AWS_NAMESPACE);

        deploymentList.forEach(deployment -> {
            String appName = deployment.getMetadata().getName();
            Optional<Container> optionalContainer =
                    deployment.getSpec().getTemplate().getSpec().getContainers().stream().filter(c -> c.getName().startsWith(appName)).findFirst();
            if (optionalContainer.isPresent()) {
                Container container = optionalContainer.get();
                // terminationGracePeriodSeconds
                deployment.getSpec().getTemplate().getSpec().setTerminationGracePeriodSeconds(45L);
                // env
                List<EnvVar> envVars = container.getEnv();
                // APP_NAME
                if (envVars.stream().noneMatch(env -> env.getName().equals("GROUP"))) {
                    EnvVar appNameEnvVar = new EnvVar("GROUP", deployment.getMetadata().getName(), null);
                    envVars.add(0, appNameEnvVar);
                }
                if (envVars.stream().noneMatch(env -> env.getName().equals("APP_NAME"))) {
                    EnvVar appNameEnvVar = new EnvVar("APP_NAME", deployment.getMetadata().getName() + "-dev", null);
                    envVars.add(0, appNameEnvVar);
                } else {
                    envVars.remove(envVars.stream().filter(env -> env.getName().equals("APP_NAME")).findFirst().get());
                    EnvVar appNameEnvVar = new EnvVar("APP_NAME", deployment.getMetadata().getName() + "-dev", null);
                    envVars.add(0, appNameEnvVar);
                }
                Optional<EnvVar> customOptsEnv = envVars.stream().filter(env -> env.getName().equals("SPRING_CUSTOM_OPTS")).findFirst();
                customOptsEnv.ifPresent(envVar -> {
                    List<String> opts = Arrays.asList(envVar.getValue().split(" "));
                    String apolloLabel = "-Dapollo.label=$(GROUP)";
                    if (!opts.contains(apolloLabel)) {
                        List<String> newOpts = Lists.newArrayList();
                        newOpts.addAll(opts);
                        newOpts.add(apolloLabel);
                        envVar.setValue(Joiner.on(" ").join(newOpts));
                    }
                });
                try {
                    KubernetesDeploymentDriver.update(kubernetesConfig.getKubernetes(), AWS_NAMESPACE, deployment);
                } catch (Exception e) {
                    log.error(deployment.getMetadata().getName());
                }
            } else {
                print(deployment.getMetadata().getName());
            }
        });

    }

}
