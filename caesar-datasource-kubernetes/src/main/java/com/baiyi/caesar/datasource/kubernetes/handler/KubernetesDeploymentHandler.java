package com.baiyi.caesar.datasource.kubernetes.handler;

import com.baiyi.caesar.common.datasource.config.DsKubernetesConfig;
import com.baiyi.caesar.datasource.kubernetes.client.KubeClient;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentList;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/25 3:49 下午
 * @Version 1.0
 */
public class KubernetesDeploymentHandler {

    public static List<Deployment> listDeployment(DsKubernetesConfig.Kubernetes kubernetes) {
        DeploymentList deploymenList = KubeClient.build(kubernetes)
                .apps().deployments().list();
        return deploymenList.getItems();
    }

    public static List<Deployment> listDeployment(DsKubernetesConfig.Kubernetes kubernetes, String namespace) {
        DeploymentList deploymenList = KubeClient.build(kubernetes)
                .apps()
                .deployments()
                .inNamespace(namespace)
                .list();
        if (CollectionUtils.isEmpty(deploymenList.getItems()))
            return Collections.emptyList();
        return deploymenList.getItems();
    }

    /**
     * @param kubernetes
     * @param namespace
     * @param name       podName
     * @return
     */
    public static Deployment getDeployment(DsKubernetesConfig.Kubernetes kubernetes, String namespace, String name) {
        return KubeClient.build(kubernetes)
                .apps()
                .deployments()
                .inNamespace(namespace)
                .withName(name)
                .get();
    }
}
