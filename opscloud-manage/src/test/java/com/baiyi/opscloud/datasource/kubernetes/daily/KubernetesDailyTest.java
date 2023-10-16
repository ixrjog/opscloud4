package com.baiyi.opscloud.datasource.kubernetes.daily;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.base.BaseKubernetesTest;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesDeploymentDriver;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.ResourceRequirements;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Author 修远
 * @Date 2023/4/28 11:10 AM
 * @Since 1.0
 */

@Slf4j
public class KubernetesDailyTest extends BaseKubernetesTest {

    private final static String NAMESPACE = "daily";

    private final static String AWS_NAMESPACE = "test";

    @Test
    void updateArmsAckOne() {
        KubernetesConfig kubernetesConfig = getConfigById(KubernetesClusterConfigs.EKS_TEST);
        List<Deployment> deploymentList = KubernetesDeploymentDriver.list(kubernetesConfig.getKubernetes(), AWS_NAMESPACE);
        Deployment accountDeployment = KubernetesDeploymentDriver.get(kubernetesConfig.getKubernetes(), AWS_NAMESPACE, "account");
        ResourceRequirements resourceRequirements = accountDeployment.getSpec().getTemplate().getSpec().getContainers().get(1).getResources();

        deploymentList.forEach(deployment -> {
            String appName = deployment.getMetadata().getName();
            Optional<Container> optionalContainer =
                    deployment.getSpec().getTemplate().getSpec().getContainers().stream().filter(c -> c.getName().startsWith(appName)).findFirst();
            if (optionalContainer.isPresent()) {
                Container container = optionalContainer.get();
                // resource
                container.setResources(resourceRequirements);
                // terminationGracePeriodSeconds
                deployment.getSpec().getTemplate().getSpec().setTerminationGracePeriodSeconds(45L);
                // labels
                Map<String, String> labels = deployment.getSpec().getTemplate().getMetadata().getLabels();
                if (!labels.containsKey("armsPilotAutoEnable")) {
                    labels.put("armsPilotAutoEnable", "on");
                }
                if (!labels.containsKey("armsPilotCreateAppName")) {
                    labels.put("armsPilotCreateAppName", deployment.getMetadata().getName() + "-daily");
                }
                if (!labels.containsKey("group")) {
                    labels.put("group", deployment.getMetadata().getName());
                }
                deployment.getSpec().getTemplate().getMetadata().setLabels(labels);

                // env
                List<EnvVar> envVars = container.getEnv();
                // JAVA_OPTS
                Optional<EnvVar> jvmEnv = envVars.stream().filter(env -> env.getName().equals("JAVA_OPTS")).findFirst();
                jvmEnv.ifPresent(envVar -> envVar.setValue("-Xms512m -Xmx1024m -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5000"));
                // APP_NAME
                if (envVars.stream().noneMatch(env -> env.getName().equals("APP_NAME"))) {
                    EnvVar appNameEnvVar = new EnvVar("APP_NAME", deployment.getMetadata().getLabels().get("app"), null);
                    envVars.add(0, appNameEnvVar);
                }
                Optional<EnvVar> agentEnv = envVars.stream().filter(env -> env.getName().equals("JAVA_JVM_AGENT")).findFirst();
                agentEnv.ifPresent(envVar -> envVar.setValue("-javaagent:/pp-agent/arms-agent.jar"));
                try {
                    KubernetesDeploymentDriver.update(kubernetesConfig.getKubernetes(), AWS_NAMESPACE, deployment);
                } catch (Exception e) {
                    print(deployment.getMetadata().getName());
                }

            } else {
                print(deployment.getMetadata().getName());
            }
        });

    }

    @Test
    void updateJacoco() {
        KubernetesConfig kubernetesConfig = getConfigById(KubernetesClusterConfigs.EKS_TEST);
        List<Deployment> deploymentList = KubernetesDeploymentDriver.list(kubernetesConfig.getKubernetes(), AWS_NAMESPACE);

        deploymentList.forEach(deployment -> {
            String appName = deployment.getMetadata().getName();
            Optional<Container> optionalContainer =
                    deployment.getSpec().getTemplate().getSpec().getContainers().stream().filter(c -> c.getName().startsWith(appName)).findFirst();
            if (optionalContainer.isPresent()) {
                Container container = optionalContainer.get();
                // env
                List<EnvVar> envVars = container.getEnv();
                Optional<EnvVar> agentEnv = envVars.stream().filter(env -> env.getName().equals("JAVA_JVM_AGENT")).findFirst();
                agentEnv.ifPresent(envVar -> {
                    if (envVar.getValue().equals("-javaagent:/pp-agent/arms-agent.jar"))
                            envVar.setValue("-javaagent:/pp-agent/arms-agent.jar -javaagent:/jacoco/jacocoagent.jar=appname=$(APP_NAME),cloud=aws,buildid=$(OC_BUILD_ID)");
                        }
                );
                try {
                    KubernetesDeploymentDriver.update(kubernetesConfig.getKubernetes(), AWS_NAMESPACE, deployment);
                } catch (Exception e) {
                    print(deployment.getMetadata().getName());
                }

            } else {
                print(deployment.getMetadata().getName());
            }
        });

    }

}
