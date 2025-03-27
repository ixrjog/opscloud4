package com.baiyi.opscloud.datasource.kubernetes.pre;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.common.exception.common.OCException;
import com.baiyi.opscloud.common.util.StringFormatter;
import com.baiyi.opscloud.datasource.kubernetes.base.BaseKubernetesTest;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesDeploymentDriver;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesServiceDriver;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.constants.ApplicationResTypeEnum;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.param.application.ApplicationResourceParam;
import com.baiyi.opscloud.domain.vo.application.ApplicationResourceVO;
import com.baiyi.opscloud.facade.application.ApplicationFacade;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2023/3/30 16:12
 * @Version 1.0
 */
@Slf4j
public class KubernetesPreTest extends BaseKubernetesTest {

    private final static String NAMESPACE = "pre";

    @Resource
    private DsInstanceAssetService dsInstanceAssetService;
    @Resource
    private ApplicationService applicationService;
    @Resource
    private ApplicationFacade applicationFacade;

    @Test
    void update() {
        KubernetesConfig kubernetesConfig = getConfigById(KubernetesClusterConfigs.EKS_PROD);
        List<Deployment> deploymentList = KubernetesDeploymentDriver.list(kubernetesConfig.getKubernetes(), NAMESPACE);
        for (int i = 0; i < deploymentList.size(); i++) {
            // index namespace name
            String appName = deploymentList.get(i).getMetadata().getName();

            print(StringFormatter.arrayFormat("{} {} {}", i, deploymentList.get(i).getMetadata().getNamespace(),
                    appName));

            Deployment deployment = deploymentList.get(i);
            Optional<Container> optionalContainer =
                    deployment.getSpec().getTemplate().getSpec().getContainers().stream().filter(
                            c -> c.getName().equals("consul-agent")).findFirst();
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

        Deployment deployment = KubernetesDeploymentDriver.get(kubernetesConfig.getKubernetes(), NAMESPACE,
                deploymentName);
        if (deployment == null) return;
        /**
         * 移除X-Ray容器
         */
        for (int i = 0; i < deployment.getSpec().getTemplate().getSpec().getContainers().size(); i++) {
            if (deployment.getSpec().getTemplate().getSpec().getContainers().get(i).getName().equals(
                    "adot-collector")) {
                deployment.getSpec().getTemplate().getSpec().getContainers().remove(i);
                break;
            }
        }

        /**
         * 查询应用容器
         */
        Optional<Container> optionalContainer = deployment.getSpec().getTemplate().getSpec().getContainers().stream().filter(
                c -> c.getName().startsWith(appName)).findFirst();
        if (optionalContainer.isEmpty()) {
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
                srcEnvVar.setValue(
                        "-javaagent:/jmx_prometheus_javaagent-0.16.1.jar=9999:/prometheus-jmx-config.yaml -javaagent:/arms-agent/arms-bootstrap-1.7.0-SNAPSHOT.jar -Darms.licenseKey=ib04e3ad3a@2a60bfc4abfe2d0 -Darms.appName=$(APP_NAME)");
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

    @Test
    void createPreDept() {
        KubernetesConfig prodConfig = getConfigById(KubernetesClusterConfigs.EKS_PROD);
        List<Deployment> deploymentList = KubernetesDeploymentDriver.list(prodConfig.getKubernetes(), NAMESPACE);
        KubernetesConfig preConfig = getConfigById(KubernetesClusterConfigs.EKS_PRE);
        deploymentList.forEach(deployment -> {
            KubernetesDeploymentDriver.create(preConfig.getKubernetes(), NAMESPACE, deployment);
        });
    }

    @Test
    void createPreService() {
        KubernetesConfig prodConfig = getConfigById(KubernetesClusterConfigs.EKS_PROD);
        List<Service> serviceList = KubernetesServiceDriver.list(prodConfig.getKubernetes(), NAMESPACE);
        KubernetesConfig preConfig = getConfigById(KubernetesClusterConfigs.EKS_PRE);
        serviceList.forEach(service -> {
            service.getMetadata().setUid(null);
            service.getMetadata().setResourceVersion(null);
            service.getSpec().setClusterIP(null);
            service.getSpec().setClusterIPs(null);
            KubernetesServiceDriver.create(preConfig.getKubernetes(), service);
        });
    }


    @Test
    void bindPreRes() {
        List<DatasourceInstanceAsset> assetList = dsInstanceAssetService.listByInstanceAssetType(
                "3592e35e387f45adac441878cebdf219", DsAssetTypeConstants.KUBERNETES_DEPLOYMENT.name());
        assetList.forEach(a -> {
            Application application = applicationService.getByName(a.getName());
            ApplicationResourceParam.Resource resource = ApplicationResourceParam.Resource.builder()
                    .applicationId(application.getId())
                    .businessId(a.getId())
                    .businessType(BusinessTypeEnum.ASSET.getType())
                    .comment(a.getAssetId())
                    .name(a.getAssetId())
                    .resourceType(ApplicationResTypeEnum.KUBERNETES_DEPLOYMENT.name())
                    .virtualResource(false)
                    .build();
            try {
                applicationFacade.bindApplicationResource(resource);
            } catch (OCException exception) {
                log.info("应用已绑定，{}", a.getName());
            }
        });
    }

    @Test
    void bindChannelProdRes() {
        final List<String> appNameList = Lists.newArrayList("tz-tigopesa-channel", "tz-selcom-channel",
                "tz-pos-uba-itex-channel", "tz-pos-ni-nbc-channel", "tz-nbc-channel", "tz-mpesa-channel",
                "tz-halopesa-channel", "tz-fasthub-channel", "tz-creditinfo-channel", "tz-channel", "tz-airtel-channel",
                "scheduler-channel", "qa-basic-service", "posp-outway", "posp-channel-route", "posp-channel-encryption",
                "posp-channel-companion", "pos-alert-webhook", "paystack", "paynet-switch-center",
                "paynet-ng-iso-channel", "pay-route", "nibss", "ng-zenith-channel", "ng-wgb-direct-channel",
                "ng-wgb-channel", "ng-wajegame-channel", "ng-vertofx-channel", "ng-up-channel", "ng-uba-new-channel",
                "ng-uba-channel", "ng-tripsdotcom-channel", "ng-transfer-coralpay-channel", "ng-surebet247-channel",
                "ng-sterling-channel", "ng-stanbic-channel", "ng-sporty-channel", "ng-smile-channel",
                "ng-pos-zone-channel", "ng-pos-upsl-channel", "ng-pos-polaris-channel", "ng-pos-nibss-channel",
                "ng-pos-kimono-channel", "ng-pos-access-channel", "ng-phedc-channel", "ng-paripesa-channel",
                "ng-parimatch-channel", "ng-palmpayinnertransfer-channel", "ng-palmpay-server-channel",
                "ng-palmpay-blooms-channel", "ng-opay-channel", "ng-onexbet-channel", "ng-oneloopwebsite-channel",
                "ng-oneloop-channel", "ng-nomi-channel", "ng-ninemobile-channel", "ng-nibsskyc-channel",
                "ng-nibss-flexibank-channel", "ng-nibss-channel", "ng-nibss-blooms-channel", "ng-new-onexbet-channel",
                "ng-new-mtn-channel", "ng-new-fidelity-channel", "ng-new-ekedc-channel", "ng-new-buypower-channel",
                "ng-nairabet-channel", "ng-naijabet-channel", "ng-mtn-cvm-channel", "ng-msport-channel",
                "ng-monokyc-channel", "ng-moniepoint-channel", "ng-moment-channel", "ng-mobifin-channel",
                "ng-kuda-channel", "ng-kedc-channel", "ng-jedc-channel", "ng-issuer-isw-channel",
                "ng-irecharge-channel", "ng-interswitch-channel", "ng-ilot-channel", "ng-ikedc-channel",
                "ng-hydrogen-channel", "ng-habaripay-channel", "ng-gtb-channel", "ng-globucketdata-channel",
                "ng-glo-channel", "ng-geniex-channel", "ng-flexi-server-channel", "ng-firs-channel",
                "ng-fidelity-channel", "ng-fdc-channel", "ng-fcmb-channel", "ng-fairmoney-channel",
                "ng-etranzact-channel", "ng-ekedc-channel", "ng-easywin-channel", "ng-easywin-channe",
                "ng-easypay-channel", "ng-dojah-channel", "ng-dml-channel", "ng-demmyglobal-channel",
                "ng-credequity-channel", "ng-coralpay-channel", "ng-common-callback", "ng-channel", "ng-cgate-channel",
                "ng-card-uba-channel", "ng-card-paystack-channel", "ng-card-gtb-channel", "ng-blusalt-channel",
                "ng-blooms-server-channel", "ng-betwinner-channel", "ng-betway-channel", "ng-betnaija-channel",
                "ng-betking-channel", "ng-betgr8-channel", "ng-betcorrect-channel", "ng-betbuzz-channel",
                "ng-betbonanza-channel", "ng-betbaba-channel", "ng-betano-channel", "ng-baxi-channel",
                "ng-bangbet-channel", "ng-axa-channel", "ng-airtel-channel", "ng-africa365-channel",
                "ng-accessbet-channel", "ng-access-channel", "multi-itniotech-channel", "ke-creditinfo-channel",
                "ke-choicebank-channel", "ke-channel", "ghana", "gh-pos-uba-itex-channel", "gh-pos-gtb-channel",
                "gh-gtb-transfer-channel", "flutterwave", "finance-switch-distribution", "finance-switch-channel",
                "config-server", "channel-sms-center", "channel-item-center", "channel-center", "bd-ssl-channel",
                "bd-porichoy-channel", "bd-nagad-channel", "bd-bkash-channel");
        appNameList.forEach(appName -> {
            Application application = applicationService.getByName(appName);
            if (application != null) {
                ApplicationResourceParam.ResourcePageQuery pageQuery = ApplicationResourceParam.ResourcePageQuery.builder()
                        .appResType(ApplicationResTypeEnum.KUBERNETES_DEPLOYMENT.name())
                        .applicationId(application.getId())
                        .businessType(5)
                        .instanceId(102)
                        .instanceUuid("ef62b8666c3e492a8e8bb76c0c7739af")
                        .page(1)
                        .length(5)
                        .queryName(appName)
                        .build();
                DataTable<ApplicationResourceVO.Resource> table = applicationFacade.previewApplicationResourcePage(
                        pageQuery);
                if (table.getTotalNum() == 1) {
                    ApplicationResourceVO.Resource appResource = table.getData().get(0);
                    ApplicationResourceParam.Resource resource = ApplicationResourceParam.Resource.builder()
                            .applicationId(application.getId())
                            .businessId(appResource.getBusinessId())
                            .businessType(BusinessTypeEnum.ASSET.getType())
                            .comment(appResource.getComment())
                            .name(appResource.getName())
                            .resourceType(ApplicationResTypeEnum.KUBERNETES_DEPLOYMENT.name())
                            .virtualResource(false)
                            .build();
                    try {
                        applicationFacade.bindApplicationResource(resource);
                    } catch (OCException exception) {
                        log.info("应用已绑定，{}", appName);
                    }
                } else {
                    log.info("应用异常，{}", appName);
                }
            }
        });
    }


    @Test
    void updatePreDept() {
        KubernetesConfig preConfig = getConfigById(KubernetesClusterConfigs.EKS_PRE);
        List<Deployment> deploymentList = KubernetesDeploymentDriver.list(preConfig.getKubernetes(), NAMESPACE);
        Deployment accountDeployment = KubernetesDeploymentDriver.get(preConfig.getKubernetes(), NAMESPACE, "account");
        ResourceRequirements resourceRequirements = accountDeployment.getSpec().getTemplate().getSpec().getContainers()
                .get(1).getResources();
        deploymentList.forEach(deployment -> {
            String appName = deployment.getMetadata().getName();
            Optional<Container> optionalContainer =
                    deployment.getSpec().getTemplate().getSpec().getContainers().stream().filter(
                            c -> c.getName().startsWith(appName)).findFirst();
            if (optionalContainer.isPresent()) {
                Container container = optionalContainer.get();
                // resource
                container.setResources(resourceRequirements);
                // terminationGracePeriodSeconds
                deployment.getSpec().getTemplate().getSpec().setTerminationGracePeriodSeconds(45L);
                // env
                List<EnvVar> envVars = container.getEnv();
                // JAVA_OPTS
                Optional<EnvVar> jvmEnv = envVars.stream().filter(env -> env.getName().equals("JAVA_OPTS")).findFirst();
                jvmEnv.ifPresent(envVar -> envVar.setValue(
                        "-Xms512M -Xmx2048M -Xmn1024M -XX:MetaspaceSize=128M -XX:MaxMetaspaceSize=256M -Xss256K -XX:+HeapDumpOnOutOfMemoryError -XX:+PrintGCDateStamps -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=80"));
                // JAVA_JVM_AGENT
                Optional<EnvVar> javaAgentEnv = envVars.stream().filter(
                        env -> env.getName().equals("JAVA_JVM_AGENT")).findFirst();
                javaAgentEnv.ifPresent(envVar -> {
                    envVar.setValue(" ");
                    envVar.setValueFrom(null);
                });
                // APP_NAME
                if (envVars.stream().noneMatch(env -> env.getName().equals("APP_NAME"))) {
                    EnvVar appNameEnvVar = new EnvVar("APP_NAME", deployment.getMetadata().getLabels().get("app"),
                            null);
                    envVars.addFirst(appNameEnvVar);
                }

                KubernetesDeploymentDriver.update(preConfig.getKubernetes(), NAMESPACE, deployment);
            } else {
                print(deployment.getMetadata().getName());
            }
        });
    }

    @Test
    void updateAckPreDept() {
        KubernetesConfig grayConfig = getConfigById(KubernetesClusterConfigs.ACK_FRANKFURT_SIT);
        Deployment srcDeployment = KubernetesDeploymentDriver.get(grayConfig.getKubernetes(), "gray",
                "mgw-core-aliyun-gray");
        ResourceRequirements resourceRequirements = srcDeployment.getSpec().getTemplate().getSpec().getContainers().getFirst().getResources();


        Probe startupProbe = srcDeployment.getSpec().getTemplate().getSpec().getContainers().getFirst().getStartupProbe();
        Probe livenessProbe = srcDeployment.getSpec().getTemplate().getSpec().getContainers().getFirst().getLivenessProbe();
        Probe readinessProbe = srcDeployment.getSpec().getTemplate().getSpec().getContainers().getFirst().getReadinessProbe();

        List<Deployment> deploymentList = KubernetesDeploymentDriver.list(grayConfig.getKubernetes(), "pre");
        deploymentList.forEach(deployment -> {
            String appName = deployment.getMetadata().getName();
            Optional<Container> optionalContainer =
                    deployment.getSpec().getTemplate().getSpec().getContainers().stream().filter(
                            c -> c.getName().startsWith(appName)).findFirst();
            if (optionalContainer.isPresent()) {
                Container container = optionalContainer.get();
                // resource
                container.setResources(resourceRequirements);
                // terminationGracePeriodSeconds
                deployment.getSpec().getTemplate().getSpec().setTerminationGracePeriodSeconds(45L);
                // replicas
                if (deployment.getSpec().getReplicas() != 0) {
                    deployment.getSpec().setReplicas(1);
                }
                // probe
                container.setLivenessProbe(livenessProbe);
                container.setReadinessProbe(readinessProbe);
                container.setStartupProbe(startupProbe);
                // env
                List<EnvVar> envVars = container.getEnv();
                // JAVA_OPTS
                Optional<EnvVar> jvmEnv = envVars.stream().filter(env -> env.getName().equals("JAVA_OPTS")).findFirst();
                jvmEnv.ifPresent(envVar -> envVar.setValue(
                        "-Xms512M -Xmx2048M -Xmn1024M -XX:MetaspaceSize=128M -XX:MaxMetaspaceSize=256M -Xss256K -XX:+HeapDumpOnOutOfMemoryError -XX:+PrintGCDateStamps -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=80"));
                // APP_NAME
                if (envVars.stream().noneMatch(env -> env.getName().equals("APP_NAME"))) {
                    EnvVar appNameEnvVar = new EnvVar("APP_NAME", deployment.getMetadata().getLabels().get("app"),
                            null);
                    envVars.addFirst(appNameEnvVar);
                }
                KubernetesDeploymentDriver.update(grayConfig.getKubernetes(), "pre", deployment);
            } else {
                print(deployment.getMetadata().getName());
            }
        });
    }
}
