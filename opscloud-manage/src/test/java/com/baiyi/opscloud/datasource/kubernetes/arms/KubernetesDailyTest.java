package com.baiyi.opscloud.datasource.kubernetes.arms;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.base.BaseKubernetesTest;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesDeploymentDriver;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.service.application.ApplicationService;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2023/3/15 15:45
 * @Version 1.0
 */
public class KubernetesDailyTest extends BaseKubernetesTest {

    @Resource
    private ApplicationService applicationService;

    /**
     * 校验容器挂载点
     */
    @Test
    void armsTest() {
        KubernetesConfig kubernetesConfig = getConfigById(KubernetesClusterConfigs.EKS_TEST);
        List<Application> applications = applicationService.queryAll();
        for (Application application : applications) {
            String appName = application.getName();
            Deployment deployment = KubernetesDeploymentDriver.get(kubernetesConfig.getKubernetes(), "test", appName);
            if (deployment == null) continue;


            Optional<Container> optionalContainer = deployment.getSpec().getTemplate().getSpec().getContainers().stream().filter(c -> c.getName().startsWith(appName)).findFirst();
            if (optionalContainer.isPresent()) {
                List<EnvVar> srcEnvVars = optionalContainer.get().getEnv();
                List<EnvVar> newEnvVars = Lists.newArrayList();
                // 设置APP_NAME
                EnvVar appNameEnvVar = new EnvVar("APP_NAME", appName + "-daily", null);
                newEnvVars.add(appNameEnvVar);

                boolean flag = false;
                for (EnvVar srcEnvVar : srcEnvVars) {
                    if (srcEnvVar.getName().equals("APP_NAME")) {
                        continue;
                    }
                    if (srcEnvVar.getName().equals("JAVA_JVM_AGENT")) {
                        flag = true;
                    } else {
                        newEnvVars.add(srcEnvVar);
                    }
                }
                if (flag) {
                    EnvVar agentEnvVar = new EnvVar("JAVA_JVM_AGENT", "-javaagent:/pp-agent/arms-agent.jar", null);
                    newEnvVars.add(agentEnvVar);
                }
                optionalContainer.get().setEnv(newEnvVars);
                try {
                    optionalContainer.get().getStartupProbe().setFailureThreshold(120);
                } catch (Exception e) {
                    print("设置错误  optionalContainer.get().getStartupProbe().setFailureThreshold(120) ");
                }
                try {
                    deployment.getSpec().getTemplate().getSpec().setTerminationGracePeriodSeconds(40L);
                } catch (Exception e) {
                    print("设置错误  deployment.getSpec().getTemplate().getSpec().setTerminationGracePeriodSeconds(40L)");
                }

                KubernetesDeploymentDriver.create(kubernetesConfig.getKubernetes(), "test", deployment);
                print("---------------------------------------------------------------------------");
                print("应用名称: " + appName);
                print("---------------------------------------------------------------------------");
            }


        }
    }

    @Test
    void adotTest() {
        KubernetesConfig kubernetesConfig = getConfigById(KubernetesClusterConfigs.EKS_TEST);
        List<Application> applications = applicationService.queryAll();
        for (Application application : applications) {
            String appName = application.getName();
            Deployment deployment = KubernetesDeploymentDriver.get(kubernetesConfig.getKubernetes(), "test", appName);
            if (deployment == null) continue;

            for (int i = 0; i < deployment.getSpec().getTemplate().getSpec().getContainers().size(); i++) {
                if (deployment.getSpec().getTemplate().getSpec().getContainers().get(i).getName().equals("adot-collector")) {
                    deployment.getSpec().getTemplate().getSpec().getContainers().remove(i);
                    break;
                }
            }

            Optional<Container> optionalContainer = deployment.getSpec().getTemplate().getSpec().getContainers().stream().filter(c -> c.getName().startsWith(appName)).findFirst();
            if (optionalContainer.isPresent()) {

                List<EnvVar> srcEnvVars = optionalContainer.get().getEnv();

                List<EnvVar> newEnvVars = Lists.newArrayList();

                for (EnvVar srcEnvVar : srcEnvVars) {
                    if(srcEnvVar.getName().equals("JAVA_TOOL_OPTIONS")){
                        continue;
                    }
                    if(srcEnvVar.getName().equals("OTEL_TRACES_SAMPLER")){
                        continue;
                    }
                    if(srcEnvVar.getName().equals("OTEL_TRACES_SAMPLER_ARG")){
                        continue;
                    }
                    if(srcEnvVar.getName().equals("OTEL_OTLP_ENDPOINT")){
                        continue;
                    }
                    if(srcEnvVar.getName().equals("OTEL_RESOURCE")){
                        continue;
                    }
                    if(srcEnvVar.getName().equals("OTEL_RESOURCE_ATTRIBUTES")){
                        continue;
                    }
                    newEnvVars.add(srcEnvVar);
                }
                optionalContainer.get().getEnv().clear();
                optionalContainer.get().setEnv(newEnvVars);
                KubernetesDeploymentDriver.create(kubernetesConfig.getKubernetes(), "test", deployment);
                print("---------------------------------------------------------------------------");
                print("应用名称: " + appName);
                print("---------------------------------------------------------------------------");
            }


        }
    }

    @Test
    void adot2Test() {
        KubernetesConfig kubernetesConfig = getConfigById(KubernetesClusterConfigs.EKS_TEST);
            String appName = "airtime-product";
            Deployment deployment = KubernetesDeploymentDriver.get(kubernetesConfig.getKubernetes(), "test", appName);
            if (deployment == null) return;

            for (Container container : deployment.getSpec().getTemplate().getSpec().getContainers()) {
                if (container.getName().equals("adot-collector")) {
                    deployment.getSpec().getTemplate().getSpec().getContainers().remove(container);
                    break;
                }
            }
            Optional<Container> optionalContainer = deployment.getSpec().getTemplate().getSpec().getContainers().stream().filter(c -> c.getName().startsWith(appName)).findFirst();
            if (optionalContainer.isPresent()) {

                List<EnvVar> srcEnvVars = optionalContainer.get().getEnv();

                List<EnvVar> newEnvVars = Lists.newArrayList();

                for (EnvVar srcEnvVar : srcEnvVars) {
                    if(srcEnvVar.getName().equals("JAVA_TOOL_OPTIONS")){
                        continue;
                    }
                    if(srcEnvVar.getName().equals("OTEL_TRACES_SAMPLER")){
                        continue;
                    }
                    if(srcEnvVar.getName().equals("OTEL_TRACES_SAMPLER_ARG")){
                        continue;
                    }
                    if(srcEnvVar.getName().equals("OTEL_OTLP_ENDPOINT")){
                        continue;
                    }
                    if(srcEnvVar.getName().equals("OTEL_RESOURCE")){
                        continue;
                    }
                    if(srcEnvVar.getName().equals("OTEL_RESOURCE_ATTRIBUTES")){
                        continue;
                    }
                    newEnvVars.add(srcEnvVar);
                }

                optionalContainer.get().setEnv(newEnvVars);
                KubernetesDeploymentDriver.create(kubernetesConfig.getKubernetes(), "test", deployment);
                print("---------------------------------------------------------------------------");
                print("应用名称: " + appName);
                print("---------------------------------------------------------------------------");
            }



    }


}
