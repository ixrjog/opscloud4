package com.baiyi.opscloud.datasource.kubernetes;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.base.BaseKubernetesTest;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesDeploymentDriver;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.service.application.ApplicationService;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import org.junit.jupiter.api.Test;

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
        KubernetesConfig kubernetesConfig = getConfigById(KubernetesClusterConfigs.EKS_PROD);
        List<Application> applications = applicationService.queryAll();
        for (Application application : applications) {
            String appName = application.getName();
            Deployment deployment = KubernetesDeploymentDriver.getDeployment(kubernetesConfig.getKubernetes(), "prod", appName);
            if (deployment == null) continue;
            print("------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            print("应用名称: " + appName);
            print(deployment.getSpec().getTemplate().getSpec().getVolumes());
            print("------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        }
    }


}
