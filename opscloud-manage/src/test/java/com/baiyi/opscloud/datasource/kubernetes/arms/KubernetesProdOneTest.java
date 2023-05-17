package com.baiyi.opscloud.datasource.kubernetes.arms;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.base.BaseKubernetesTest;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesDeploymentDriver;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2023/4/19 15:24
 * @Version 1.0
 */
public class KubernetesProdOneTest extends BaseKubernetesTest {

    private final static String NAMESPACE = "prod";

    @Test
    void bTest() {
        oneTest("flutterwave", "flutterwave-canary");
    }

    /**
     * 单个Deployment 启用ARMS
     */
    private void oneTest(String appName, String deploymentName) {
        KubernetesConfig kubernetesConfig = getConfigById(KubernetesClusterConfigs.EKS_PROD);

        /*
         * ARMS中应用的名称
         */
        final String armsAppName = appName + "-prod";

        Deployment deployment = KubernetesDeploymentDriver.get(kubernetesConfig.getKubernetes(), NAMESPACE, deploymentName);
        if (deployment == null) return;

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
             * 启用ARMS
             */
            if (srcEnvVar.getName().equals("JAVA_JVM_AGENT")) {
                srcEnvVar.setValue("-javaagent:/jmx_prometheus_javaagent-0.16.1.jar=9999:/prometheus-jmx-config.yaml -javaagent:/arms-agent/arms-bootstrap-1.7.0-SNAPSHOT.jar -Darms.licenseKey=ib04e3ad3a@2a60bfc4abfe2d0 -Darms.appName=$(APP_NAME)");
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
        KubernetesDeploymentDriver.update(kubernetesConfig.getKubernetes(), NAMESPACE, deployment);
        print("---------------------------------------------------------------------------");
        print("应用名称: " + appName);
        print("---------------------------------------------------------------------------");

    }

}