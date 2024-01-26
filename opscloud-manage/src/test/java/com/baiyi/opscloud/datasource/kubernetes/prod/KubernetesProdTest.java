package com.baiyi.opscloud.datasource.kubernetes.prod;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.base.BaseKubernetesTest;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesDeploymentDriver;
import com.google.common.collect.Lists;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Author 修远
 * @Date 2023/9/15 10:39 AM
 * @Since 1.0
 */

@Slf4j
public class KubernetesProdTest extends BaseKubernetesTest {

    private final static String NAMESPACE = "prod";
    private final static String ENV_NAME = "prod";

    private final static List<String> apps = Lists.newArrayList("c-bff-product-1", "finance-account-core-prod", "finance-bff-product", "finance-saving-product-prod", "loan-1", "loan-2", "loan-canary", "member-center-1", "okcard-risk-control-1", "okcard-risk-control-2", "okcard-risk-control-canary", "postpay-channel-1", "postpay-channel-2", "postpay-channel-canary", "postpay-marketing-1", "postpay-marketing-2", "postpay-marketing-canary", "sms-1", "sms-2", "trade-1", "trade-2", "trade-3", "trade-canary");

    @Test
    void changeName() {
        KubernetesConfig kubernetesConfig = getConfigById(KubernetesClusterConfigs.ACK_FRANKFURT_PROD);
        List<Deployment> deploymentList = KubernetesDeploymentDriver.list(kubernetesConfig.getKubernetes(), NAMESPACE);
        deploymentList.forEach(deployment -> {
            try {
                String name = deployment.getMetadata().getLabels().get("app");
                if (name.endsWith("-" + ENV_NAME)) {
                    try {
                        String appName = name.substring(0, name.length() - ENV_NAME.length() - 1);
                        Optional<Container> optionalContainer =
                                deployment.getSpec().getTemplate().getSpec().getContainers().stream().filter(c -> c.getName().startsWith(appName)).findFirst();
                        if (optionalContainer.isPresent()) {
                            Container container = optionalContainer.get();
                            // resource
                            container.setName(appName);
                            try {
                                KubernetesDeploymentDriver.update(kubernetesConfig.getKubernetes(), NAMESPACE, deployment);
                            } catch (Exception e) {
                                print(deployment.getMetadata().getName());
                            }
                        } else {
                            print(deployment.getMetadata().getName());
                        }
                    } catch (Exception e) {
                        log.error(name);
                    }
                }
            } catch (Exception e) {
                log.error(deployment.getMetadata().getName());
            }
        });
    }


    @Test
    void updateArmsAckOne() {
        KubernetesConfig kubernetesConfig = getConfigById(KubernetesClusterConfigs.EKS_PROD);
        List<Deployment> deploymentList = KubernetesDeploymentDriver.list(kubernetesConfig.getKubernetes(), NAMESPACE);
//        List<Deployment> deploymentList = Lists.newArrayList();
//        Deployment leoDemo1Deployment = KubernetesDeploymentDriver.get(kubernetesConfig.getKubernetes(), NAMESPACE, "leo-demo-1");
//        deploymentList.add(leoDemo1Deployment);
//        Deployment leoDemo2Deployment = KubernetesDeploymentDriver.get(kubernetesConfig.getKubernetes(), NAMESPACE, "leo-demo-2");
//        deploymentList.add(leoDemo2Deployment);
//        Deployment leoDemo3Deployment = KubernetesDeploymentDriver.get(kubernetesConfig.getKubernetes(), NAMESPACE, "leo-demo-3");
//        deploymentList.add(leoDemo3Deployment);

        Deployment leoDemoDeployment = KubernetesDeploymentDriver.get(kubernetesConfig.getKubernetes(), NAMESPACE, "leo-demo-canary");

        Container leoDemoContainer =
                leoDemoDeployment.getSpec().getTemplate().getSpec().getContainers().stream().filter(c -> c.getName().startsWith("leo-demo")).findFirst().get();

        List<EnvVar> leoDemoEnvVars = leoDemoContainer.getEnv();
        EnvVar leoDemoGroupEnv = leoDemoEnvVars.stream().filter(env -> env.getName().equals("GROUP")).findFirst().get();

        deploymentList.forEach(deployment -> {
            String name = deployment.getMetadata().getLabels().get("app");
            if (name.endsWith("-" + ENV_NAME)) {
                try {
                    String appName = name.substring(0, name.length() - ENV_NAME.length() - 1);
                    Optional<Container> optionalContainer =
                            deployment.getSpec().getTemplate().getSpec().getContainers().stream().filter(c -> c.getName().startsWith(appName)).findFirst();
                    if (optionalContainer.isPresent()) {
                        Container container = optionalContainer.get();
                        // env
                        List<EnvVar> envVars = container.getEnv();
                        // GROUP
                        if (envVars.stream().noneMatch(env -> env.getName().equals("GROUP"))) {
                            envVars.add(0, leoDemoGroupEnv);
                        } else {
                            EnvVar groupEnv = envVars.stream().filter(env -> env.getName().equals("GROUP")).findFirst().get();
                            envVars.remove(groupEnv);
                            envVars.add(0, leoDemoGroupEnv);
                        }
                        // SPRING_CUSTOM_OPTS
                        Optional<EnvVar> agentEnv = envVars.stream().filter(env -> env.getName().equals("SPRING_CUSTOM_OPTS")).findFirst();
                        agentEnv.ifPresent(envVar -> {
                            if (envVar.getValue().equals("-Dspring.cloud.consul.discovery.hostname=$(HOSTIP) -Dspring.cloud.consul.host=$(HOSTIP)")) {
                                envVar.setValue("-Dspring.cloud.consul.discovery.hostname=$(HOSTIP) -Dspring.cloud.consul.host=$(HOSTIP) -Dapollo.label=$(GROUP)");
                            } else {
                                print(deployment.getMetadata().getName() + "SPRING_CUSTOM_OPTS: " + envVar.getValue());
                            }
                        });
                        try {
                            KubernetesDeploymentDriver.update(kubernetesConfig.getKubernetes(), NAMESPACE, deployment);
                        } catch (Exception e) {
                            print(deployment.getMetadata().getName());
                        }
                    } else {
                        print(deployment.getMetadata().getName() + "不存在");
                    }
                } catch (Exception e) {
                    log.error(name);
                }
            }
        });

    }


    @Test
    void updateIstio() {
        KubernetesConfig kubernetesConfig = getConfigById(KubernetesClusterConfigs.ACK_FRANKFURT_PROD);

        Deployment demoDeployment = KubernetesDeploymentDriver.get(kubernetesConfig.getKubernetes(), NAMESPACE, "offline-pay-product-canary");
        Container demoContainer =
                demoDeployment.getSpec().getTemplate().getSpec().getContainers().stream().filter(c -> c.getName().startsWith("offline-pay-product")).findFirst().get();
        List<EnvVar> demoEnvVars = demoContainer.getEnv();
        EnvVar groupEnv = demoEnvVars.stream().filter(env -> env.getName().equals("GROUP")).findFirst().get();

        String config = demoDeployment.getSpec().getTemplate().getMetadata().getAnnotations().get("proxy.istio.io/config");

        List<Deployment> deploymentList = KubernetesDeploymentDriver.list(kubernetesConfig.getKubernetes(), NAMESPACE);
        deploymentList.forEach(deployment -> {
            if (deployment.getMetadata().getName().endsWith("-canary")) {
                String name = deployment.getMetadata().getLabels().get("app");
                if (Strings.isNotBlank(name) && name.endsWith("-prod")) {
                    try {
                        String appName = name.substring(0, name.length() - 5);
                        Optional<Container> optionalContainer =
                                deployment.getSpec().getTemplate().getSpec().getContainers().stream().filter(c -> c.getName().startsWith(appName)).findFirst();
                        if (optionalContainer.isPresent()) {
                            Container container = optionalContainer.get();
                            deployment.getSpec().getTemplate().getSpec().setTerminationGracePeriodSeconds(45L);
                            // labels
                            Map<String, String> labels = deployment.getSpec().getTemplate().getMetadata().getLabels();

                            if (labels.containsKey("group")) {
                                labels.put("sidecar.istio.io/inject", "true");
                                deployment.getSpec().getTemplate().getMetadata().setLabels(labels);

                                Map<String, String> annotations = deployment.getSpec().getTemplate().getMetadata().getAnnotations();
                                annotations.put("proxy.istio.io/config", config);
                                deployment.getSpec().getTemplate().getMetadata().setAnnotations(annotations);

                                // env
                                List<EnvVar> envVars = container.getEnv();
                                // GROUP
                                if (envVars.stream().noneMatch(env -> env.getName().equals("GROUP"))) {
                                    envVars.add(0, groupEnv);
                                }
                                // SPRING_CUSTOM_OPTS
                                Optional<EnvVar> agentEnv = envVars.stream().filter(env -> env.getName().equals("SPRING_CUSTOM_OPTS")).findFirst();
                                agentEnv.ifPresent(envVar -> {
                                    if (Strings.isBlank(envVar.getValue())) {
                                        envVar.setValue("-Dapollo.label=$(GROUP)");
                                    } else if (envVar.getValue().equals("-Dspring.cloud.consul.discovery.hostname=$(HOSTIP) -Dspring.cloud.consul.host=$(HOSTIP)")) {
                                        envVar.setValue("-Dspring.cloud.consul.discovery.hostname=$(HOSTIP) -Dspring.cloud.consul.host=$(HOSTIP) -Dapollo.label=$(GROUP)");
                                    } else {
                                        if (!envVar.getValue().endsWith("-Dapollo.label=$(GROUP)")) {
                                            print(deployment.getMetadata().getName() + "SPRING_CUSTOM_OPTS: " + envVar.getValue());
                                        }
                                    }
                                });
                                try {
                                    KubernetesDeploymentDriver.update(kubernetesConfig.getKubernetes(), NAMESPACE, deployment);
                                } catch (Exception e) {
                                    print(deployment.getMetadata().getName());
                                }
                            } else {
                                print(appName + "no group label");
                            }
                        } else {
                            print(deployment.getMetadata().getName() + "container 不存在");
                        }
                    } catch (Exception e) {
                        log.error(name);
                    }
                }
            }
        });
    }


    @Test
    void eeexxx() {
        KubernetesConfig kubernetesConfig = getConfigById(KubernetesClusterConfigs.ACK_FRANKFURT_PROD);
        Deployment demoDeployment = KubernetesDeploymentDriver.get(kubernetesConfig.getKubernetes(), NAMESPACE, "offline-pay-product-canary");
        Container demoContainer =
                demoDeployment.getSpec().getTemplate().getSpec().getContainers().stream().filter(c -> c.getName().startsWith("offline-pay-product")).findFirst().get();
        List<EnvVar> demoEnvVars = demoContainer.getEnv();
        EnvVar groupEnv = demoEnvVars.stream().filter(env -> env.getName().equals("GROUP")).findFirst().get();
//        String config = demoDeployment.getSpec().getTemplate().getMetadata().getAnnotations().get("proxy.istio.io/config");
        String config = "terminationDrainDuration: 40s";


        apps.forEach(name -> {
            Deployment deployment = KubernetesDeploymentDriver.get(kubernetesConfig.getKubernetes(), NAMESPACE, name);

            String appName = deployment.getMetadata().getLabels().get("app");
            if (Strings.isNotBlank(appName) && appName.endsWith("-prod")) {
                String podName = appName.substring(0, appName.length() - 5);
                Optional<Container> optionalContainer =
                        deployment.getSpec().getTemplate().getSpec().getContainers().stream().filter(c -> c.getName().startsWith(podName)).findFirst();
                if (optionalContainer.isPresent()) {
                    Container container = optionalContainer.get();
                    deployment.getSpec().getTemplate().getSpec().setTerminationGracePeriodSeconds(45L);

                    Map<String, String> labels = deployment.getSpec().getTemplate().getMetadata().getLabels();
                    labels.put("sidecar.istio.io/inject", "true");
                    deployment.getSpec().getTemplate().getMetadata().setLabels(labels);

                    Map<String, String> annotations = deployment.getSpec().getTemplate().getMetadata().getAnnotations();
                    annotations.put("proxy.istio.io/config", config);
                    deployment.getSpec().getTemplate().getMetadata().setAnnotations(annotations);

                    // env
//                    List<EnvVar> envVars = container.getEnv();
//                    // GROUP
//                    if (envVars.stream().noneMatch(env -> env.getName().equals("GROUP"))) {
//                        envVars.add(0, groupEnv);
//                    } else {
//                        EnvVar old = envVars.stream().filter(env -> env.getName().equals("GROUP")).findFirst().get();
//                        envVars.remove(old);
//                        envVars.add(0, groupEnv);
//                    }
//                    // SPRING_CUSTOM_OPTS
//                    Optional<EnvVar> agentEnv = envVars.stream().filter(env -> env.getName().equals("SPRING_CUSTOM_OPTS")).findFirst();
//                    agentEnv.ifPresent(envVar -> {
//                        if (Strings.isBlank(envVar.getValue())) {
//                            envVar.setValue("-Dapollo.label=$(GROUP)");
//                        } else if (envVar.getValue().equals("-Dspring.cloud.consul.discovery.hostname=$(HOSTIP) -Dspring.cloud.consul.host=$(HOSTIP)")) {
//                            envVar.setValue("-Dspring.cloud.consul.discovery.hostname=$(HOSTIP) -Dspring.cloud.consul.host=$(HOSTIP) -Dapollo.label=$(GROUP)");
//                        } else {
//                            if (!envVar.getValue().endsWith("-Dapollo.label=$(GROUP)")) {
//                                print(deployment.getMetadata().getName() + "SPRING_CUSTOM_OPTS: " + envVar.getValue());
//                            }
//                        }
//                    });
                    try {
                        KubernetesDeploymentDriver.update(kubernetesConfig.getKubernetes(), NAMESPACE, deployment);
                    } catch (Exception e) {
                        print(deployment.getMetadata().getName());
                    }
                } else {
                    print(appName + "no group label");
                }
            }
        });
    }


    @Test
    void printGroup() {
        KubernetesConfig kubernetesConfig = getConfigById(KubernetesClusterConfigs.ACK_FRANKFURT_PROD);
        List<Deployment> deploymentList = KubernetesDeploymentDriver.list(kubernetesConfig.getKubernetes(), NAMESPACE);

        deploymentList.forEach(deployment -> {
            String name = deployment.getMetadata().getName();
            String appName = deployment.getMetadata().getLabels().get("app");
            if (Strings.isNotBlank(appName) && appName.endsWith("-prod")) {
                String podName = appName.substring(0, appName.length() - 5);
                Optional<Container> optionalContainer =
                        deployment.getSpec().getTemplate().getSpec().getContainers().stream().filter(c -> c.getName().startsWith(podName)).findFirst();
                if (optionalContainer.isPresent()) {
                    Container container = optionalContainer.get();
                    print(name + "    limits = " + container.getResources().getLimits() + "    requests = " + container.getResources().getRequests());
                } else {
                    print(appName + "no group label");
                }
            }


//            if (!name.endsWith("-canary") && !name.endsWith("-prod") && !name.endsWith("-1")) {
//                print(name);
//            }

//            if (name.endsWith("-canary") || name.endsWith("-prod") || name.endsWith("-1")) {
//                print(name);
//            }
        });

//        apps.forEach(name -> {
//            Deployment deployment = KubernetesDeploymentDriver.get(kubernetesConfig.getKubernetes(), NAMESPACE, name);
//            String appName = deployment.getMetadata().getLabels().get("app");
//            if (Strings.isNotBlank(appName) && appName.endsWith("-prod")) {
//                String podName = appName.substring(0, appName.length() - 5);
//                Optional<Container> optionalContainer =
//                        deployment.getSpec().getTemplate().getSpec().getContainers().stream().filter(c -> c.getName().startsWith(podName)).findFirst();
//                if (optionalContainer.isPresent()) {
//                    Container container = optionalContainer.get();
//                    // env
//                    List<EnvVar> envVars = container.getEnv();
//                    // GROUP
//                    if (envVars.stream().noneMatch(env -> env.getName().equals("GROUP"))) {
//                        print("no GROUP name= "+ name);
//                    }
//                } else {
//                    print(appName + "no group label");
//                }
//            } else {
//                print("appName=" + appName);
//            }
//        });
    }

    @Test
    void printIstioProxyConfig() {
        KubernetesConfig kubernetesConfig = getConfigById(KubernetesClusterConfigs.ACK_FRANKFURT_PROD);
        List<Deployment> deploymentList = KubernetesDeploymentDriver.list(kubernetesConfig.getKubernetes(), NAMESPACE);
        deploymentList.forEach(deployment -> {
            Map<String, String> labels = deployment.getSpec().getTemplate().getMetadata().getLabels();

            if ("true".equals(labels.get("sidecar.istio.io/inject"))) {
                Map<String, String> annotations = deployment.getSpec().getTemplate().getMetadata().getAnnotations();
                if (!annotations.containsKey("proxy.istio.io/config")) {
                    print(deployment.getMetadata().getName());
                }
            }
        });
    }

}
