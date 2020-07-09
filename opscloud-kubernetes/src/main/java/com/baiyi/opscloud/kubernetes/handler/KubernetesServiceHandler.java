package com.baiyi.opscloud.kubernetes.handler;

import com.baiyi.opscloud.kubernetes.client.KubernetesClientContainer;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceList;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.ParameterNamespaceListVisitFromServerGetDeleteRecreateWaitApplicable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/7/1 9:55 上午
 * @Version 1.0
 */
@Component
public class KubernetesServiceHandler {

    @Resource
    private KubernetesClientContainer kubernetesClientContainer;

    public ServiceList getServiceList(String clusterName, String namespace) {
        return kubernetesClientContainer.getClient(clusterName).services().inNamespace(namespace).list();
    }

    public Service getService(String clusterName, String namespace, String name) {
        return kubernetesClientContainer.getClient(clusterName).services().inNamespace(namespace).withName(name).get();
    }

    public boolean deleteService(String clusterName, String namespace, String name) {
        Service service = kubernetesClientContainer.getClient(clusterName).services().inNamespace(namespace).withName(name).get();
        if (service == null) return true;
        return kubernetesClientContainer.getClient(clusterName).services().inNamespace(namespace).withName(name).delete();
    }

    public Service  createService(String clusterName, String namespace, String serviceYAML) {
        Service service = getServiceByYAML(clusterName, serviceYAML);
        return kubernetesClientContainer.getClient(clusterName).services().inNamespace(namespace).create(service);
    }

    public  Service createOrReplaceService(String clusterName, String namespace, String serviceYAML) {
        Service service = getServiceByYAML(clusterName, serviceYAML);
        return kubernetesClientContainer.getClient(clusterName).services().inNamespace(namespace).createOrReplace(service);
    }

    private  Service getServiceByYAML(String clusterName, String serviceYAML) throws RuntimeException {
        InputStream is = new ByteArrayInputStream(serviceYAML.getBytes());
        KubernetesClient kubernetesClient = kubernetesClientContainer.getClient(clusterName);
        ParameterNamespaceListVisitFromServerGetDeleteRecreateWaitApplicable<HasMetadata, Boolean> p = kubernetesClient.load(is);
        List<HasMetadata> resources = p.get();
        if (resources.isEmpty()) // 配置文件为空
            throw new RuntimeException("转换配置文件错误");
        HasMetadata resource = resources.get(0);
        if (resource instanceof Service)
            return (Service) resource;
        throw new RuntimeException("类型不匹配");
    }

}
