package com.baiyi.opscloud.datasource.kubernetes.arms;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.common.util.FunctionUtil;
import com.baiyi.opscloud.datasource.kubernetes.base.BaseKubernetesTest;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesDeploymentDriver;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.generator.opscloud.ApplicationResource;
import com.baiyi.opscloud.service.application.ApplicationResourceService;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.google.common.base.Joiner;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import jakarta.annotation.Resource;
import org.apache.logging.log4j.util.Strings;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2023/3/22 15:00
 * @Version 1.0
 */
public class KubernetesProdTest extends BaseKubernetesTest {

    private final static String NAMESPACE = "prod";

    private final static String RESOURCE_TYPE = DsAssetTypeConstants.KUBERNETES_DEPLOYMENT.name();

    @Resource
    private ApplicationService applicationService;

    @Resource
    private ApplicationResourceService applicationResourceService;

    /**
     * nibss, pay-route, ng-channel
     */

    @Test
    void bTest() {

        boolean onlyCanary = true;

        KubernetesConfig kubernetesConfig = getConfigById(KubernetesClusterConfigs.EKS_PROD);

        List<String> appNames = Lists.newArrayList(
                "sms"
        );
        appNames.forEach(appName -> {
            Application application = applicationService.getByName(appName);
            List<ApplicationResource> applicationResourceList = applicationResourceService.queryByApplication(application.getId(), RESOURCE_TYPE);
            List<ApplicationResource> prodResList = applicationResourceList.stream().filter(applicationResource ->
                    applicationResource.getName().startsWith(Joiner.on(":").join(NAMESPACE, appName))
            ).toList();
            prodResList.forEach(applicationResource -> {
                String name = applicationResource.getName().split(":")[1];
                FunctionUtil.isTureOrFalse(applicationResource.getName().endsWith("-canary"))
                        .withBoolean(
                                () -> ackOne(kubernetesConfig, appName, name, appName + "-canary"),
                                () -> {
                                    FunctionUtil.trueFunction(!onlyCanary)
                                            .withTrue(
                                                    () -> ackOne(kubernetesConfig, appName, name, appName + "-prod")
                                            );
                                }
                        );
            });
        });
    }

    private void ackOne(KubernetesConfig kubernetesConfig, String appName, String deploymentName, String armsName) {

        Deployment deployment = KubernetesDeploymentDriver.get(kubernetesConfig.getKubernetes(), NAMESPACE, deploymentName);
        if (deployment == null) return;
        /*
         * 查询应用容器
         */
        Optional<Container> optionalContainer =
                deployment.getSpec().getTemplate().getSpec().getContainers().stream()
                        .filter(c -> c.getName().startsWith(appName)).findFirst();

        if (optionalContainer.isPresent()) {
            Container container = optionalContainer.get();
            // terminationGracePeriodSeconds
            deployment.getSpec().getTemplate().getSpec().setTerminationGracePeriodSeconds(45L);
            // labels
            Map<String, String> labels = deployment.getSpec().getTemplate().getMetadata().getLabels();
            if (!labels.containsKey("armsPilotAutoEnable")) {
                labels.put("armsPilotAutoEnable", "on");
            }
            if (!labels.containsKey("armsPilotCreateAppName")) {
                labels.put("armsPilotCreateAppName", armsName);
            }
            deployment.getSpec().getTemplate().getMetadata().setLabels(labels);
            // env
            List<EnvVar> envVars = container.getEnv();
            // APP_NAME
            if (envVars.stream().noneMatch(env -> env.getName().equals("APP_NAME"))) {
                EnvVar appNameEnvVar = new EnvVar("APP_NAME", deployment.getMetadata().getLabels().get("app"), null);
                envVars.add(0, appNameEnvVar);
            }
            if (envVars.stream().noneMatch(env -> env.getName().equals("GROUP"))) {
                EnvVar appNameEnvVar = new EnvVar("GROUP", armsName, null);
                envVars.add(0, appNameEnvVar);
            }
            Optional<EnvVar> agentEnv = envVars.stream().filter(env -> env.getName().equals("JAVA_JVM_AGENT")).findFirst();
            agentEnv.ifPresent(envVar -> envVar.setValue(Strings.EMPTY));
            KubernetesDeploymentDriver.update(kubernetesConfig.getKubernetes(), NAMESPACE, deployment);
        } else {
            print(deployment.getMetadata().getName());
        }

    }

}
