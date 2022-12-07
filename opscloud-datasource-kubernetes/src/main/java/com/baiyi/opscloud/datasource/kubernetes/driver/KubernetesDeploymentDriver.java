package com.baiyi.opscloud.datasource.kubernetes.driver;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.common.exception.common.OCRuntimeException;
import com.baiyi.opscloud.datasource.kubernetes.client.KubeClient;
import com.baiyi.opscloud.datasource.kubernetes.exception.KubernetesDeploymentException;
import com.baiyi.opscloud.datasource.kubernetes.util.KubernetesUtil;
import com.google.common.collect.Maps;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.PodTemplateSpec;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentList;
import io.fabric8.kubernetes.api.model.apps.DeploymentSpec;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @Author baiyi
 * @Date 2021/6/25 3:49 下午
 * @Version 1.0
 */
public class KubernetesDeploymentDriver {

    public static final String REDEPLOY_TIMESTAMP = "redeploy-timestamp";

    /**
     * 重启容器
     *
     * @param kubernetes
     * @param namespace
     * @param name
     */
    public static void redeployDeployment(KubernetesConfig.Kubernetes kubernetes, String namespace, String name) {
        Deployment deployment = getDeployment(kubernetes, namespace, name);
        if (deployment == null) return;
        Optional<Map<String, String>> optionalAnnotations = Optional.of(deployment)
                .map(Deployment::getSpec)
                .map(DeploymentSpec::getTemplate)
                .map(PodTemplateSpec::getMetadata)
                .map(ObjectMeta::getAnnotations);
        if (optionalAnnotations.isPresent()) {
            deployment.getSpec().getTemplate().getMetadata().getAnnotations()
                    .put(REDEPLOY_TIMESTAMP, String.valueOf(new Date().getTime()));
        } else {
            Map<String, String> annotations = Maps.newHashMap();
            annotations.put(REDEPLOY_TIMESTAMP, String.valueOf(new Date().getTime()));
            deployment.getSpec().getTemplate().getMetadata().setAnnotations(annotations);
        }
        createOrReplaceDeployment(kubernetes, namespace, deployment);
    }

    /**
     * 扩容
     *
     * @param kubernetes
     * @param namespace
     * @param name
     * @param replicas   扩容后的副本数
     */
    @Deprecated
    public static void scaleDeploymentReplicas(KubernetesConfig.Kubernetes kubernetes, String namespace, String name, Integer replicas) throws KubernetesDeploymentException {
        Deployment deployment = getDeployment(kubernetes, namespace, name);
        Optional<Integer> optionalReplicas = Optional.ofNullable(deployment)
                .map(Deployment::getSpec)
                .map(DeploymentSpec::getReplicas);
        if (!optionalReplicas.isPresent())
            throw new KubernetesDeploymentException("无法获取Deployment参数！");
        // 更新副本数
        if (optionalReplicas.get() >= replicas)
            throw new KubernetesDeploymentException("副本数不正确，只能扩容不能缩容！");
        deployment.getSpec().setReplicas(replicas);
        createOrReplaceDeployment(kubernetes, namespace, deployment);
    }

    /**
     * 扩容
     *
     * @param kubernetes
     * @param namespace
     * @param name
     * @param replicas   扩容后的副本数
     */
    public static void scaleDeploymentReplicas2(KubernetesConfig.Kubernetes kubernetes, String namespace, String name, Integer replicas) throws KubernetesDeploymentException {
        Deployment deployment = getDeployment(kubernetes, namespace, name);
        final Integer nowReplicas = Optional.ofNullable(deployment)
                .map(Deployment::getSpec)
                .map(DeploymentSpec::getReplicas)
                .orElseThrow(() -> new KubernetesDeploymentException("Deployment扩容失败: 读取副本数量错误！"));
        // 更新副本数
        if (nowReplicas >= replicas)
            throw new KubernetesDeploymentException("Deployment扩容失败: 只能扩容不能缩容 nowReplicas={}, newReplicas={} ！", nowReplicas, replicas);
        KubeClient.build(kubernetes)
                .apps()
                .deployments()
                .inNamespace(namespace)
                .withName(name)
                .scale(replicas);
    }

    public static List<Deployment> listDeployment(KubernetesConfig.Kubernetes kubernetes) {
        DeploymentList deploymenList = KubeClient.build(kubernetes)
                .apps().deployments().list();
        Deployment deployment = new Deployment();
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
     * @param kubernetes
     * @param namespace
     * @param name       podName
     * @return
     */
    public static Deployment rolloutSetImageEquivalentTest(KubernetesConfig.Kubernetes kubernetes, String namespace, String name, String imageName, String image) {
        return KubeClient.build(kubernetes)
                .apps()
                .deployments()
                .inNamespace(namespace)
                .withName(name)
                .rolling()
                .updateImage(Collections.singletonMap(imageName, image));
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
     *
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
     *
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
    public static Deployment toDeployment(KubernetesClient kuberClient, String content) throws OCRuntimeException {
        HasMetadata resource = KubernetesUtil.toResource(kuberClient, content);
        if (resource instanceof io.fabric8.kubernetes.api.model.apps.Deployment)
            return (Deployment) resource;
        throw new OCRuntimeException("Kubernetes deployment 配置文件类型不匹配!");
    }

}
