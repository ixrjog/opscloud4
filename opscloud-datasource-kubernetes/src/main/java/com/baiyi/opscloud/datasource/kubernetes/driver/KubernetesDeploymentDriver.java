package com.baiyi.opscloud.datasource.kubernetes.driver;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.client.MyKubernetesClientBuilder;
import com.baiyi.opscloud.datasource.kubernetes.exception.KubernetesDeploymentException;
import com.google.common.collect.Maps;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.PodTemplateSpec;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentList;
import io.fabric8.kubernetes.api.model.apps.DeploymentSpec;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2023/3/30 11:21
 * @Version 1.0
 */
@Slf4j
public class KubernetesDeploymentDriver {

    public static final String REDEPLOY_TIMESTAMP = "redeploy-timestamp";

    /**
     * 重启容器
     *
     * @param kubernetes
     * @param deployment
     */
    public static void redeploy(KubernetesConfig.Kubernetes kubernetes, Deployment deployment) {
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
        update(kubernetes, deployment);
    }

    public static Deployment create(KubernetesConfig.Kubernetes kubernetes, String content) {
        try (KubernetesClient kc = MyKubernetesClientBuilder.build(kubernetes)) {
            Deployment deployment = toDeployment(kc, content);
            // 删除资源版本
            deployment.getMetadata().setResourceVersion(null);
            return create(kubernetes, deployment);
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public static Deployment create(KubernetesConfig.Kubernetes kubernetes, String namespace, Deployment deployment) {
        // 删除资源版本
        deployment.getMetadata().setResourceVersion(null);
        try (KubernetesClient kc = MyKubernetesClientBuilder.build(kubernetes)) {
            return kc.apps()
                    .deployments()
                    .inNamespace(namespace)
                    .resource(deployment)
                    .create();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public static Deployment update(KubernetesConfig.Kubernetes kubernetes, String namespace, Deployment deployment) {
        try (KubernetesClient kc = MyKubernetesClientBuilder.build(kubernetes)) {
            return kc.apps()
                    .deployments()
                    .inNamespace(namespace)
                    .resource(deployment)
                    .update();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    /**
     * 扩容
     *
     * @param kubernetes
     * @param namespace
     * @param name
     * @param replicas
     * @throws KubernetesDeploymentException
     */
    public static void scale(KubernetesConfig.Kubernetes kubernetes, String namespace, String name, int replicas) throws KubernetesDeploymentException {
        Deployment deployment = get(kubernetes, namespace, name);
        final Integer nowReplicas = Optional.ofNullable(deployment)
                .map(Deployment::getSpec)
                .map(DeploymentSpec::getReplicas)
                .orElseThrow(() -> new KubernetesDeploymentException("扩容失败: 读取副本数量错误！"));
        // 更新副本数
        if (nowReplicas >= replicas) {
            throw new KubernetesDeploymentException("只能扩容 nowReplicas={}, newReplicas={} ！", nowReplicas, replicas);
        }
        try (KubernetesClient kc = MyKubernetesClientBuilder.build(kubernetes)) {
            kc.apps()
                    .deployments()
                    .inNamespace(namespace)
                    .withName(name)
                    .scale(replicas);
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    /**
     * 缩容
     *
     * @param kubernetes
     * @param namespace
     * @param name
     * @param replicas
     * @throws KubernetesDeploymentException
     */
    public static void reduce(KubernetesConfig.Kubernetes kubernetes, String namespace, String name, int replicas) throws KubernetesDeploymentException {
        Deployment deployment = get(kubernetes, namespace, name);
        final Integer nowReplicas = Optional.ofNullable(deployment)
                .map(Deployment::getSpec)
                .map(DeploymentSpec::getReplicas)
                .orElseThrow(() -> new KubernetesDeploymentException("缩容失败: 读取副本数量错误！"));
        // 更新副本数
        if (replicas < 1) {
            throw new KubernetesDeploymentException("指定副本数不能少于1, replicas={} ！", replicas);
        }
        if (replicas >= nowReplicas) {
            throw new KubernetesDeploymentException("只能缩容 nowReplicas={}, newReplicas={} ！", nowReplicas, replicas);
        }
        try (KubernetesClient kc = MyKubernetesClientBuilder.build(kubernetes)) {
            kc.apps()
                    .deployments()
                    .inNamespace(namespace)
                    .withName(name)
                    .scale(replicas);
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public static Deployment update(KubernetesConfig.Kubernetes kubernetes, Deployment deployment) {
        try (KubernetesClient kc = MyKubernetesClientBuilder.build(kubernetes)) {
            return kc.apps()
                    .deployments()
                    .inNamespace(deployment.getMetadata().getNamespace())
                    .resource(deployment)
                    .update();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public static List<Deployment> list(KubernetesConfig.Kubernetes kubernetes, String namespace) {
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

    private static Deployment create(KubernetesConfig.Kubernetes kubernetes, Deployment deployment) {
        // 删除资源版本
        deployment.getMetadata().setResourceVersion(null);
        try (KubernetesClient kc = MyKubernetesClientBuilder.build(kubernetes)) {
            return kc.apps()
                    .deployments()
                    .inNamespace(deployment.getMetadata().getNamespace())
                    .resource(deployment)
                    .create();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public static Deployment get(KubernetesConfig.Kubernetes kubernetes, String namespace, String name) {
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

    public static void delete(KubernetesConfig.Kubernetes kubernetes, Deployment deployment) {
        try (KubernetesClient kc = MyKubernetesClientBuilder.build(kubernetes)) {
            kc.apps()
                    .deployments()
                    .inNamespace(deployment.getMetadata().getNamespace())
                    .resource(deployment)
                    .delete();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    private static Deployment toDeployment(KubernetesClient kubernetesClient, String content) {
        InputStream is = new ByteArrayInputStream(content.getBytes());
        return kubernetesClient
                .apps()
                .deployments()
                .load(is)
                .item();
    }

}