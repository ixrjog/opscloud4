package com.baiyi.opscloud.datasource.kubernetes.xray;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.base.BaseKubernetesTest;
import com.baiyi.opscloud.datasource.kubernetes.driver.NewKubernetesDeploymentDriver;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.service.business.BusinessAssetRelationService;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import jakarta.annotation.Resource;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2023/4/7 10:12
 * @Version 1.0
 */
public class KubernetesProdXRayTest extends BaseKubernetesTest {

    private final static String NAMESPACE = "prod";

    @Resource
    private ApplicationService applicationService;

    @Resource
    private BusinessAssetRelationService relationService;

    /*
     * nibss, pay-route, ng-channel
     */

    @Test
    void bTest() {
        List<String> apps = Lists.newArrayList(
                "mail"
        );
        for (String app : apps) {
            Application application = applicationService.getByName(app);

          //  relationService.

            //  oneTest(app);
        }
    }

    /**
     * 单个Deployment(Canary) 启用ARMS
     */

    private void oneTest(String appName, String deploymentName, String envName) {
        KubernetesConfig kubernetesConfig = getConfigById(BaseKubernetesTest.KubernetesClusterConfigs.EKS_PROD);
        /*
         * ARMS中应用的名称
         */
        final String armsAppName = appName + "-" + envName;

        Deployment deployment = NewKubernetesDeploymentDriver.get(kubernetesConfig.getKubernetes(), NAMESPACE, deploymentName);
        if (deployment == null) return;
        /*
         * 移除X-Ray容器
         */
        for (int i = 0; i < deployment.getSpec().getTemplate().getSpec().getContainers().size(); i++) {
            if (deployment.getSpec().getTemplate().getSpec().getContainers().get(i).getName().equals("adot-collector")) {
                deployment.getSpec().getTemplate().getSpec().getContainers().remove(i);
                break;
            }
        }
        /*
         * 查询应用容器
         */
        Optional<Container> optionalContainer = deployment.getSpec().getTemplate().getSpec().getContainers().stream().filter(c -> c.getName().startsWith(appName)).findFirst();
        if (optionalContainer.isEmpty()) {
            print("未找到容器: 退出");
            return;
        }
        List<EnvVar> srcEnvVars = optionalContainer.get().getEnv();
        List<EnvVar> newEnvVars = Lists.newArrayList();
        /*
         * 设置环境变量 $APP_NAME
         */
        EnvVar appNameEnvVar = new EnvVar("APP_NAME", armsAppName, null);
        newEnvVars.add(appNameEnvVar);
        for (EnvVar srcEnvVar : srcEnvVars) {
            /*
             * 下线X-Ray
             */
            if (srcEnvVar.getName().equals("JAVA_TOOL_OPTIONS")) {
                continue;
            }
            if (srcEnvVar.getName().equals("OTEL_TRACES_SAMPLER")) {
                continue;
            }
            if (srcEnvVar.getName().equals("OTEL_TRACES_SAMPLER_ARG")) {
                continue;
            }
            if (srcEnvVar.getName().equals("OTEL_OTLP_ENDPOINT")) {
                continue;
            }
            if (srcEnvVar.getName().equals("OTEL_RESOURCE")) {
                continue;
            }
            if (srcEnvVar.getName().equals("OTEL_RESOURCE_ATTRIBUTES")) {
                continue;
            }
            if (srcEnvVar.getName().equals("APP_NAME")) {
                continue;
            }

            newEnvVars.add(srcEnvVar);
        }
        optionalContainer.get().getEnv().clear();
        /*
         * 重新设置环境变量
         */
        optionalContainer.get().setEnv(newEnvVars);
        /*
         * 更新 Deployment
         */
        NewKubernetesDeploymentDriver.create(kubernetesConfig.getKubernetes(), NAMESPACE, deployment);
        print("---------------------------------------------------------------------------");
        print("应用名称: " + appName);
        print("---------------------------------------------------------------------------");
    }

}
