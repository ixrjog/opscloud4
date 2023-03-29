package com.baiyi.opscloud.datasource.kubernetes.driver;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.client.MyKubernetesClientBuilder;
import com.baiyi.opscloud.datasource.kubernetes.exception.KubernetesDeploymentException;
import com.baiyi.opscloud.datasource.kubernetes.exception.KubernetesException;
import com.baiyi.opscloud.datasource.kubernetes.util.KubernetesUtil;
import com.google.common.collect.Maps;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.PodTemplateSpec;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentList;
import io.fabric8.kubernetes.api.model.apps.DeploymentSpec;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @Author baiyi
 * @Date 2021/6/25 3:49 下午
 * @Version 1.0
 */
@Slf4j
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
        redeployDeployment(kubernetes, deployment);
    }

    /**
     * 重启容器
     *
     * @param kubernetes
     * @param deployment
     */
    public static void redeployDeployment(KubernetesConfig.Kubernetes kubernetes, Deployment deployment) {
        if (deployment == null) {
            return;
        }
        Optional<Map<String, String>> optionalAnnotations = Optional.of(deployment)
                .map(Deployment::getSpec)
                .map(DeploymentSpec::getTemplate)
                .map(PodTemplateSpec::getMetadata)
                .map(ObjectMeta::getAnnotations);
        if (optionalAnnotations.isPresent()) {
            deployment.getSpec().getTemplate().getMetadata().getAnnotations()
                    .put(REDEPLOY_TIMESTAMP, String.valueOf(System.currentTimeMillis()));
        } else {
            Map<String, String> annotations = Maps.newHashMap();
            annotations.put(REDEPLOY_TIMESTAMP, String.valueOf(System.currentTimeMillis()));
            deployment.getSpec().getTemplate().getMetadata().setAnnotations(annotations);
        }
        createOrReplaceDeployment(kubernetes, deployment);
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
        if (!optionalReplicas.isPresent()) {
            throw new KubernetesDeploymentException("无法获取Deployment参数！");
        }
        // 更新副本数
        if (optionalReplicas.get() >= replicas) {
            throw new KubernetesDeploymentException("副本数不正确，只能扩容不能缩容！");
        }
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
        if (nowReplicas >= replicas) {
            throw new KubernetesDeploymentException("Deployment扩容失败: 只能扩容不能缩容 nowReplicas={}, newReplicas={} ！", nowReplicas, replicas);
        }
        MyKubernetesClientBuilder.build(kubernetes)
                .apps()
                .deployments()
                .inNamespace(namespace)
                .withName(name)
                .scale(replicas);
    }

    public static List<Deployment> listDeployment(KubernetesConfig.Kubernetes kubernetes) {
        try (KubernetesClient kc = MyKubernetesClientBuilder.build(kubernetes)) {
            DeploymentList deploymentList = kc
                    .apps().deployments().list();
            return deploymentList.getItems();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public static List<Deployment> listDeployment(KubernetesConfig.Kubernetes kubernetes, String namespace) {
        try (KubernetesClient kc = MyKubernetesClientBuilder.build(kubernetes)) {
            DeploymentList deploymentList = kc
                    .apps()
                    .deployments()
                    .inNamespace(namespace)
                    .list();
            if (CollectionUtils.isEmpty(deploymentList.getItems())) {
                return Collections.emptyList();
            }
            return deploymentList.getItems();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    /**
     * @param kubernetes
     * @param namespace
     * @param name       podName
     * @return
     */
    public static Deployment getDeployment(KubernetesConfig.Kubernetes kubernetes, String namespace, String name) {
        try (KubernetesClient kc = MyKubernetesClientBuilder.build(kubernetes)) {
            return kc.apps()
                    .deployments()
                    .inNamespace(namespace)
                    .withName(name)
                    .get();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    /**
     * @param kubernetes
     * @param namespace
     * @param name       podName
     * @return
     */
    public static Deployment rolloutSetImageEquivalentTest(KubernetesConfig.Kubernetes kubernetes, String namespace, String name, String imageName, String image) {
        try (KubernetesClient kc = MyKubernetesClientBuilder.build(kubernetes)) {
            return kc.apps()
                    .deployments()
                    .inNamespace(namespace)
                    .withName(name)
                    .rolling()
                    .updateImage(Collections.singletonMap(imageName, image));
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
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
        try (KubernetesClient kc = MyKubernetesClientBuilder.build(kubernetes)) {
            Deployment deployment = toDeployment(kc, content);
            return kc.apps()
                    .deployments()
                    .inNamespace(namespace)
                    .create(deployment);
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    /**
     * 创建无状态
     *
     * @param kubernetes
     * @param content
     * @return
     */
    public static Deployment createDeployment(KubernetesConfig.Kubernetes kubernetes, String content) {
        try (KubernetesClient kc = MyKubernetesClientBuilder.build(kubernetes)) {
            Deployment deployment = toDeployment(kc, content);
            return kc.apps()
                    .deployments()
                    .create(deployment);
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
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
        try (KubernetesClient kc = MyKubernetesClientBuilder.build(kubernetes)) {
            Deployment deployment = toDeployment(kc, content);
            return createOrReplaceDeployment(kubernetes, namespace, deployment);
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    /**
     * 创建或更新无状态
     *
     * @param kubernetes
     * @param content
     * @return
     */
    public static Deployment createOrReplaceDeployment(KubernetesConfig.Kubernetes kubernetes, String content) {
        try (KubernetesClient kc = MyKubernetesClientBuilder.build(kubernetes)) {
            Deployment deployment = toDeployment(kc, content);
            return createOrReplaceDeployment(kubernetes, deployment);
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
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
        try (KubernetesClient kc = MyKubernetesClientBuilder.build(kubernetes)) {
            return kc.apps()
                    .deployments()
                    .inNamespace(namespace)
                    .createOrReplace(deployment);
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public static Deployment createOrReplaceDeployment(KubernetesConfig.Kubernetes kubernetes, Deployment deployment) {
        try (KubernetesClient kc = MyKubernetesClientBuilder.build(kubernetes)) {
            return kc.apps()
                    .deployments()
                    .inNamespace(deployment.getMetadata().getNamespace())
                    .createOrReplace(deployment);
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    /**
     * 配置文件转换为无状态资源
     *
     * @param kubernetesClient
     * @param content          YAML
     * @return
     * @throws RuntimeException
     */
    public static Deployment toDeployment(KubernetesClient kubernetesClient, String content) throws KubernetesException {
        HasMetadata resource = KubernetesUtil.toResource(kubernetesClient, content);
        if (resource instanceof io.fabric8.kubernetes.api.model.apps.Deployment) {
            return (Deployment) resource;
        }
        throw new KubernetesException("Kubernetes deployment 配置文件类型不匹配!");
    }

}
