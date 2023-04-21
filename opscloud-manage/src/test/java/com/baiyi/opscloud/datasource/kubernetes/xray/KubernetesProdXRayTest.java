package com.baiyi.opscloud.datasource.kubernetes.xray;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.base.BaseKubernetesTest;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesDeploymentDriver;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.generator.opscloud.ApplicationResource;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.service.application.ApplicationResourceService;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import jakarta.annotation.Resource;
import org.assertj.core.util.Lists;

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
    private ApplicationResourceService applicationResourceService;

    @Resource
    private DsInstanceAssetService dsInstanceAssetService;

    void bTest() {
        List<Application> apps = applicationService.queryAll();
        KubernetesConfig kubernetesConfig = getConfigById(BaseKubernetesTest.KubernetesClusterConfigs.EKS_PROD);
        for (Application application : apps) {
            //  Application application = applicationService.getByName(appName);
            // 查询应用绑定的所有生产环境的Deployment
            List<ApplicationResource> resources = applicationResourceService.queryByApplication(application.getId(), DsAssetTypeConstants.KUBERNETES_DEPLOYMENT.name());
            String appName = application.getName();

            List<DatasourceInstanceAsset> assets = Lists.newArrayList();
            for (ApplicationResource resource : resources) {
                if (resource.getName().startsWith("prod:")) {
                    DatasourceInstanceAsset asset = dsInstanceAssetService.getById(resource.getBusinessId());
                    if (!asset.getName().endsWith("-canary")) {
                        assets.add(asset);
                    }
                }
            }

            for (DatasourceInstanceAsset asset : assets) {
                print("appName=" + appName + ", deploymentName=" + asset.getName());
                oneTest(kubernetesConfig, appName, asset.getName(), "prod");
            }
        }
    }

    /**
     * 单个Deployment(Canary) 启用ARMS
     */

    private void oneTest(KubernetesConfig kubernetesConfig, String appName, String deploymentName, String envName) {
        /*
         * ARMS中应用的名称
         */
        final String armsAppName = appName + "-" + envName;

        Deployment deployment = KubernetesDeploymentDriver.get(kubernetesConfig.getKubernetes(), NAMESPACE, deploymentName);
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
        KubernetesDeploymentDriver.update(kubernetesConfig.getKubernetes(), NAMESPACE, deployment);
        print("---------------------------------------------------------------------------");
        print("应用名称: " + appName);
        print("---------------------------------------------------------------------------");
    }

}
