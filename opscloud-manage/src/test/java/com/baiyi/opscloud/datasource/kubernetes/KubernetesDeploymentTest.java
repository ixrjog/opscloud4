package com.baiyi.opscloud.datasource.kubernetes;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.base.BaseKubernetesTest;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesDeploymentDriver;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.service.application.ApplicationService;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2022/7/4 16:51
 * @Version 1.0
 */
public class KubernetesDeploymentTest extends BaseKubernetesTest {

    @Resource
    private ApplicationService applicationService;

    @Test
    void aTest() {
        Deployment deployment = KubernetesDeploymentDriver.getDeployment(getConfig().getKubernetes(), "dev", "merchant-rss-dev");
        KubernetesDeploymentDriver.redeployDeployment(getConfig().getKubernetes(), "dev", "merchant-rss-dev");
        print(deployment);
    }

    @Test
    void bTest() {
        KubernetesConfig kubernetesConfig = getConfigById(KubernetesClusterConfigs.EKS_PROD);
        List<Application> applications = applicationService.queryAll();
        for (Application application : applications) {
            String appName = application.getName();
            Deployment deployment = KubernetesDeploymentDriver.getDeployment(kubernetesConfig.getKubernetes(), "prod", appName);
            if (deployment == null) continue;
            Optional<Container> optionalContainer = deployment.getSpec().getTemplate().getSpec().getContainers().stream().filter(e -> "consul-agent".equals(e.getName())).findFirst();
            if (optionalContainer.isPresent()) {
                print("------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                print("应用名称: " + appName);
                print(optionalContainer.get().getArgs());
                print("------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            } else {
                print("容器不存在: " + appName);
            }
        }
    }

    /**
     * 校验容器挂载点
     */
    @Test
    void cTest() {
        KubernetesConfig kubernetesConfig = getConfigById(KubernetesClusterConfigs.EKS_TEST);
        List<Application> applications = applicationService.queryAll();
        for (Application application : applications) {
            String appName = application.getName();
            Deployment deployment = KubernetesDeploymentDriver.getDeployment(kubernetesConfig.getKubernetes(), "ci", appName);
            if (deployment == null) continue;
            print("------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            print("应用名称: " + appName);
            print(deployment.getSpec().getTemplate().getSpec().getVolumes());
            print("------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        }
    }

    /**
     * 修改配置
     */
    @Test
    void dTest() {
        KubernetesConfig kubernetesConfig = getConfigById(KubernetesClusterConfigs.EKS_PRE);
        // account
        updateDeploymentWithApplication(kubernetesConfig,applicationService.getById(23));
    }

    /**
     * 修改配置
     */
    @Test
    void eTest() {
        KubernetesConfig kubernetesConfig = getConfigById(KubernetesClusterConfigs.EKS_PRE);
        List<Application> applications = applicationService.queryAll();
        for (Application application : applications) {
            updateDeploymentWithApplication(kubernetesConfig, application);
        }
    }

    private void updateDeploymentWithApplication(KubernetesConfig kubernetesConfig, Application application) {
        String appName = application.getName();
        print("------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        print("应用名称: " + appName);
        if(appName.equals("okcard-risk-control")) return;
        if(appName.equals("postloan-risk-control")) return;
        Deployment deployment = KubernetesDeploymentDriver.getDeployment(kubernetesConfig.getKubernetes(), "gray", appName);
        if (deployment == null) return;

        // 删除 init容器
        if (!CollectionUtils.isEmpty(deployment.getSpec().getTemplate().getSpec().getInitContainers())) {
            deployment.getSpec().getTemplate().getSpec().getInitContainers().clear();
        }

        Optional<Container> optionalContainer =
                deployment.getSpec().getTemplate().getSpec().getContainers().stream().filter(e -> e.getName().equals(appName)).findFirst();
        if (optionalContainer.isPresent()) {

            // 删除磁盘挂载点
            Container container = optionalContainer.get();
            container.getVolumeMounts().clear();
            deployment.getSpec().getTemplate().getSpec().getVolumes().clear();

            // 修改容器资源限制
            container.getResources().getLimits().put("memory",new Quantity("5Gi"));
            container.getResources().getRequests().put("memory",new Quantity("2Gi"));

            // 修改JVM
            Optional<EnvVar> optionalEnvVar = container.getEnv().stream().filter(e -> e.getName().equals("JAVA_OPTS")).findFirst();
            if (optionalEnvVar.isPresent()) {
                optionalEnvVar.get().setValue("-Xms1024M -Xmx3072M -Xmn1536M -XX:MetaspaceSize=128M -XX:MaxMetaspaceSize=256M -Xss256K -XX:+HeapDumpOnOutOfMemoryError -XX:+PrintGCDateStamps -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=80");
            }
        }

        // 更新 Deployment
        KubernetesDeploymentDriver.createOrReplaceDeployment(kubernetesConfig.getKubernetes(), deployment);
    }

}


