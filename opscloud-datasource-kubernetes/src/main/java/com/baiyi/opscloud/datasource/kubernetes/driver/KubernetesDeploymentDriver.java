package com.baiyi.opscloud.datasource.kubernetes.driver;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.client.KubeClient;
import com.baiyi.opscloud.datasource.kubernetes.util.KubernetesUtil;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentList;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/25 3:49 下午
 * @Version 1.0
 */
public class KubernetesDeploymentDriver {

    public static List<Deployment> listDeployment(KubernetesConfig.Kubernetes kubernetes) {
        DeploymentList deploymenList = KubeClient.build(kubernetes)
                .apps().deployments().list();
        return deploymenList.getItems();
    }

    public static List<Deployment> listDeployment(KubernetesConfig.Kubernetes kubernetes, String namespace) {
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
    public static Deployment getDeployment(KubernetesConfig.Kubernetes kubernetes, String namespace, String name) {
        return KubeClient.build(kubernetes)
                .apps()
                .deployments()
                .inNamespace(namespace)
                .withName(name)
                .get();
    }

    /**
     * 创建无状态
     *
     * @param kubernetes
     * @param namespace
     * @param content
     * @return
     */
    public static Deployment createDeployment(KubernetesConfig.Kubernetes kubernetes, String namespace, String content) {
        KubernetesClient kuberClient = KubeClient.build(kubernetes);
        Deployment deployment = toDeployment(kuberClient, content);
        return kuberClient.apps()
                .deployments()
                .inNamespace(namespace)
                .create(deployment);
    }

    /**
     * 创建无状态
     * @param kubernetes
     * @param content
     * @return
     */
    public static Deployment createDeployment(KubernetesConfig.Kubernetes kubernetes, String content) {
        KubernetesClient kuberClient = KubeClient.build(kubernetes);
        Deployment deployment = toDeployment(kuberClient, content);
        return kuberClient.apps()
                .deployments()
                .create(deployment);
    }

    /**
     * 创建或更新无状态
     *
     * @param kubernetes
     * @param namespace
     * @param content
     * @return
     */
    public static Deployment createOrReplaceDeployment(KubernetesConfig.Kubernetes kubernetes, String namespace, String content) {
        KubernetesClient kuberClient = KubeClient.build(kubernetes);
        Deployment deployment = toDeployment(kuberClient, content);
        return createOrReplaceDeployment(kubernetes, namespace, deployment);
    }

    /**
     * 创建或更新无状态
     * @param kubernetes
     * @param content
     * @return
     */
    public static Deployment createOrReplaceDeployment(KubernetesConfig.Kubernetes kubernetes, String content) {
        KubernetesClient kuberClient = KubeClient.build(kubernetes);
        Deployment deployment = toDeployment(kuberClient, content);
        return createOrReplaceDeployment(kubernetes, deployment);
    }



    /**
     * 创建或更新无状态
     *
     * @param kubernetes
     * @param namespace
     * @param deployment
     * @return
     */
    public static Deployment createOrReplaceDeployment(KubernetesConfig.Kubernetes kubernetes, String namespace, Deployment deployment) {
        return KubeClient.build(kubernetes)
                .apps()
                .deployments()
                .inNamespace(namespace)
                .createOrReplace(deployment);
    }

    public static Deployment createOrReplaceDeployment(KubernetesConfig.Kubernetes kubernetes, Deployment deployment) {
        return KubeClient.build(kubernetes).apps()
                .deployments()
                .inNamespace(deployment.getMetadata().getNamespace())
                .createOrReplace(deployment);
    }

//    public static Deployment replaceDeployment(KubernetesConfig.Kubernetes kubernetes, Deployment deployment) {
//        return KubeClient.build(kubernetes).apps()
//                .deployments()
//                .inNamespace(deployment.getMetadata().getNamespace())
//                .replace(deployment);
//    }

    /**
     * 配置文件转换为无状态资源
     *
     * @param kuberClient
     * @param content     YAML
     * @return
     * @throws RuntimeException
     */
    public static Deployment toDeployment(KubernetesClient kuberClient, String content) throws RuntimeException {
        HasMetadata resource =  KubernetesUtil.toResource(kuberClient,content);
        if (resource instanceof io.fabric8.kubernetes.api.model.apps.Deployment)
            return (Deployment) resource;
        throw new RuntimeException("Deployment配置文件类型不匹配!");
    }

}
