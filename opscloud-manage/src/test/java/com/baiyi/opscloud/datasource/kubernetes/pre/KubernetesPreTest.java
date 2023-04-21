package com.baiyi.opscloud.datasource.kubernetes.pre;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.base.BaseKubernetesTest;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesDeploymentDriver;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import org.assertj.core.util.Lists;

import java.util.List;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2023/3/30 16:12
 * @Version 1.0
 */
public class KubernetesPreTest extends BaseKubernetesTest {

    private final static String NAMESPACE = "pre";

    void update() {
        KubernetesConfig kubernetesConfig = getConfigById(KubernetesClusterConfigs.EKS_PROD);
        List<Deployment> deploymentList = KubernetesDeploymentDriver.list(kubernetesConfig.getKubernetes(), NAMESPACE);
        for (int i = 0; i < deploymentList.size(); i++) {
            // index namespace name
            String appName = deploymentList.get(i).getMetadata().getName();

            print(String.format("%s %s %s", i, deploymentList.get(i).getMetadata().getNamespace(), appName));

            Deployment deployment = deploymentList.get(i);
            Optional<Container> optionalContainer =
                    deployment.getSpec().getTemplate().getSpec().getContainers().stream().filter(c -> c.getName().equals("consul-agent")).findFirst();
            if (optionalContainer.isPresent()) {
                Container container = optionalContainer.get();
                container.getArgs().clear();
                List<String> args = Lists.newArrayList();
                args.add("agent");
                args.add("-bind=$(POD_IP)");
                // args.add("-node=posp-admin-$(POD_IP)");

                args.add("-node=" + appName + "-$(POD_IP)");

                args.add("-retry-join=172.30.151.77");
                args.add("-retry-join=172.30.153.69");
                args.add("-retry-join=172.30.155.237");
                args.add("-client=0.0.0.0");
                args.add("-ui");
                container.setArgs(args);
                KubernetesDeploymentDriver.update(kubernetesConfig.getKubernetes(), NAMESPACE, deployment);
            } else {
                print("consul-agent不存在");
            }

        }
    }


    /**
     * 单个Deployment(Canary) 启用ARMS
     */

    private void oneTest(String appName) {
        KubernetesConfig kubernetesConfig = getConfigById(KubernetesClusterConfigs.EKS_PROD);

        final String deploymentName = appName + "-1";
        /**
         * ARMS中应用的名称
         */
        final String armsAppName = appName + "-prod";

        Deployment deployment = KubernetesDeploymentDriver.get(kubernetesConfig.getKubernetes(), NAMESPACE, deploymentName);
        if (deployment == null) return;
        /**
         * 移除X-Ray容器
         */
        for (int i = 0; i < deployment.getSpec().getTemplate().getSpec().getContainers().size(); i++) {
            if (deployment.getSpec().getTemplate().getSpec().getContainers().get(i).getName().equals("adot-collector")) {
                deployment.getSpec().getTemplate().getSpec().getContainers().remove(i);
                break;
            }
        }

        /**
         * 查询应用容器
         */
        Optional<Container> optionalContainer = deployment.getSpec().getTemplate().getSpec().getContainers().stream().filter(c -> c.getName().startsWith(appName)).findFirst();
        if (!optionalContainer.isPresent()) {
            print("未找到容器: 退出");
            return;
        }

        List<EnvVar> srcEnvVars = optionalContainer.get().getEnv();
        List<EnvVar> newEnvVars = Lists.newArrayList();

        /**
         * 设置环境变量 $APP_NAME
         */
        EnvVar appNameEnvVar = new EnvVar("APP_NAME", armsAppName, null);
        newEnvVars.add(appNameEnvVar);

        for (EnvVar srcEnvVar : srcEnvVars) {
            /**
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
            /**
             * 启用ARMS
             */
            if (srcEnvVar.getName().equals("JAVA_JVM_AGENT")) {
                srcEnvVar.setValue("-javaagent:/jmx_prometheus_javaagent-0.16.1.jar=9999:/prometheus-jmx-config.yaml -javaagent:/arms-agent/arms-bootstrap-1.7.0-SNAPSHOT.jar -Darms.licenseKey=ib04e3ad3a@2a60bfc4abfe2d0 -Darms.appName=$(APP_NAME)");
            }
            newEnvVars.add(srcEnvVar);
        }
        optionalContainer.get().getEnv().clear();
        /**
         * 重新设置环境变量
         */
        optionalContainer.get().setEnv(newEnvVars);
        /**
         * 更新 Deployment
         */
        KubernetesDeploymentDriver.create(kubernetesConfig.getKubernetes(), NAMESPACE, deployment);
        print("---------------------------------------------------------------------------");
        print("应用名称: " + appName);
        print("---------------------------------------------------------------------------");

    }

}
