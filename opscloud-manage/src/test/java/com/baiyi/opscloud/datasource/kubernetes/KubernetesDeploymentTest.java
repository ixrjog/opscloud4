package com.baiyi.opscloud.datasource.kubernetes;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.base.BaseKubernetesTest;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesDeploymentDriver;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.service.application.ApplicationService;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;
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
        Deployment deployment = KubernetesDeploymentDriver.get(getConfigById(KubernetesClusterConfigs.EKS_TEST).getKubernetes(), "ci", "account");
        // KubernetesDeploymentDriver.redeployDeployment(getConfig().getKubernetes(), "dev", "merchant-rss-dev");
        Map<String, Quantity> limits = deployment.getSpec().getTemplate().getSpec().getContainers().get(1).getResources().getLimits();
        print(limits.get("cpu").getAmount());
        print(limits.get("cpu").getFormat());
        print(limits.get("memory").getAmount());
        print(limits.get("memory").getFormat());
        print(limits.get("memory").toString());
        print(deployment);
    }

    @Test
    void fTest() {
        Deployment deployment = KubernetesDeploymentDriver.get(getConfigById(KubernetesClusterConfigs.EKS_PROD).getKubernetes(), "prod", "leo-demo-1");
        print(deployment);
    }

    @Test
    void bTest() {
        KubernetesConfig kubernetesConfig = getConfigById(KubernetesClusterConfigs.EKS_PROD);
        List<Application> applications = applicationService.queryAll();
        for (Application application : applications) {
            String appName = application.getName();
            Deployment deployment = KubernetesDeploymentDriver.get(kubernetesConfig.getKubernetes(), "prod", appName);
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
            Deployment deployment = KubernetesDeploymentDriver.get(kubernetesConfig.getKubernetes(), "ci", appName);
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
        updateDeploymentWithApplication(kubernetesConfig, applicationService.getById(23));
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

    // CI
    private void updateDeploymentWithApplication(KubernetesConfig kubernetesConfig, Application application) {
        String appName = application.getName();
        print("------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        print("应用名称: " + appName);
        Deployment deployment = KubernetesDeploymentDriver.get(kubernetesConfig.getKubernetes(), "pre", appName);
        if (deployment == null) return;
        // 选中应用容器
        Optional<Container> optionalContainer = deployment
                .getSpec()
                .getTemplate()
                .getSpec()
                .getContainers().stream().filter(e -> e.getName().equals(appName)).findFirst();
        if (optionalContainer.isPresent()) {
            // 修改preStop参数
            try {
                Container container = optionalContainer.get();
                container.getLifecycle().getPreStop().getExec().getCommand().clear();
                List<String> command = Lists.newArrayList("curl", "http://127.0.0.1:8080/eksshutdown", "-X", "GET");
                container.getLifecycle().getPreStop().getExec().setCommand(command);
                // 更新 Deployment
                KubernetesDeploymentDriver.update(kubernetesConfig.getKubernetes(), deployment);
            } catch (Exception e) {
                print(e.getMessage());
            }
        }

    }

}


