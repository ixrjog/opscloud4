package com.baiyi.opscloud.datasource.kubernetes.ms;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.base.BaseKubernetesTest;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesDeploymentDriver;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.service.application.ApplicationService;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.PodSpec;
import io.fabric8.kubernetes.api.model.PodTemplateSpec;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentSpec;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2023/5/25 09:38
 * @Version 1.0
 */
public class KubernetesMeterSphereTest extends BaseKubernetesTest {

    @Resource
    private ApplicationService applicationService;

    @Test
    void updateEnvTest() {
        List<Application> apps = applicationService.queryAll();
        KubernetesConfig kubernetesConfig = getConfigById(KubernetesClusterConfigs.EKS_TEST);
        for (Application app : apps) {
            updateOneAppEnv(kubernetesConfig, app.getName());
        }
    }

    private void updateOneAppEnv(KubernetesConfig kubernetesConfig, String appName) {

        final String namespace = "test";
//        final String deploymentName = appName + "-daily";
        final String deploymentName = appName;

        Deployment deployment = KubernetesDeploymentDriver.get(kubernetesConfig.getKubernetes(), namespace, deploymentName);
        if (deployment == null) {
            print("---------------------------------------------------------------------------");
            print("应用名称: " + appName + " 未找到Deployment");
            print("---------------------------------------------------------------------------");
            return;
        }

        Optional<List<Container>> optionalContainers = Optional.ofNullable(deployment)
                .map(Deployment::getSpec)
                .map(DeploymentSpec::getTemplate)
                .map(PodTemplateSpec::getSpec)
                .map(PodSpec::getContainers);
        if (optionalContainers.isEmpty()) {
            return;
        }

        //  查询应用容器
        Optional<Container> optionalContainer = optionalContainers.get().stream().filter(c -> c.getName().startsWith(appName)).findFirst();
        if (optionalContainer.isEmpty()) {
            print("---------------------------------------------------------------------------");
            print("应用名称: " + appName + " 未找到容器");
            print("---------------------------------------------------------------------------");
            return;
        }

        List<EnvVar> srcEnvVars = optionalContainer.get().getEnv();

        for (EnvVar envVar : srcEnvVars) {
            if (envVar.getName().equals("JAVA_JVM_AGENT")) {
                if (envVar.getValue().equals("-javaagent:/pp-agent/arms-agent.jar")) {
                    envVar.setValue("-javaagent:/pp-agent/arms-agent.jar -javaagent:/jacoco/jacocoagent.jar=appname=$(APP_NAME),cloud=aws,buildid=$(OC_BUILD_ID)");
                    /*
                     * 更新 Deployment
                     */
                    KubernetesDeploymentDriver.update(kubernetesConfig.getKubernetes(), namespace, deployment);
                    print("---------------------------------------------------------------------------");
                    print("应用名称: " + appName + " 配置完成");
                    print("---------------------------------------------------------------------------");
                }
                return;
            }
        }

    }


}
