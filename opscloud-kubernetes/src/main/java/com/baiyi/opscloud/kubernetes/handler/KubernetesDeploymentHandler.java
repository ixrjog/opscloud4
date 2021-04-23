package com.baiyi.opscloud.kubernetes.handler;

import com.baiyi.opscloud.kubernetes.client.KubernetesClientContainer;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentList;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/30 9:54 上午
 * @Version 1.0
 */
@Component("KubernetesDeploymentHandler")
public class KubernetesDeploymentHandler {

    @Resource
    private KubernetesClientContainer kubernetesClientContainer;

    public Deployment createDeployment(String clusterName, String namespace, String deploymentYAML) {
        Deployment deployment = getDeploymentByYAML(clusterName, deploymentYAML);
        return kubernetesClientContainer.getClient(clusterName).apps().deployments().inNamespace(namespace).create(deployment);
    }

    public Deployment createOrReplaceDeployment(String clusterName, String namespace, String deploymentYAML) {
        Deployment deployment = getDeploymentByYAML(clusterName, deploymentYAML);
        return createOrReplaceDeployment(clusterName, namespace, deployment);
    }

    public Deployment createOrReplaceDeployment(String clusterName, String namespace, Deployment deployment) {
        return kubernetesClientContainer.getClient(clusterName).apps().deployments().inNamespace(namespace).createOrReplace(deployment);
    }

    private Deployment getDeploymentByYAML(String clusterName, String deploymentYAML) throws RuntimeException {
        InputStream is = new ByteArrayInputStream(deploymentYAML.getBytes());
        List<HasMetadata> resources = kubernetesClientContainer.getClient(clusterName).load(is).get();
        if (resources.isEmpty()) // 配置文件为空
            throw new RuntimeException("转换配置文件错误");
        HasMetadata resource = resources.get(0);
        if (resource instanceof io.fabric8.kubernetes.api.model.apps.Deployment)
            return (Deployment) resource;
        throw new RuntimeException("类型不匹配");
    }

    public DeploymentList getDeploymentList(String clusterName, String namespace) {
        return kubernetesClientContainer.getClient(clusterName).apps().deployments().inNamespace(namespace).list();
    }

    public Deployment getDeployment(String clusterName, String namespace, String name) {
        return kubernetesClientContainer.getClient(clusterName).apps().deployments().inNamespace(namespace).withName(name).get();
    }

    public boolean deleteDeployment(String clusterName, String namespace, String name) {
        Deployment deployment = kubernetesClientContainer.getClient(clusterName).apps().deployments().inNamespace(namespace).withName(name).get();
        if (deployment == null) return true;
        return kubernetesClientContainer.getClient(clusterName).apps().deployments().inNamespace(namespace).withName(name).delete();
    }

}
