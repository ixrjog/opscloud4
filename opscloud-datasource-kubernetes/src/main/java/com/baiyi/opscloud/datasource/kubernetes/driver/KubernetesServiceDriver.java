package com.baiyi.opscloud.datasource.kubernetes.driver;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.client.KubeClient;
import com.baiyi.opscloud.datasource.kubernetes.util.KubernetesUtil;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceList;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/25 3:55 下午
 * @Version 1.0
 */
public class KubernetesServiceDriver {

    public static List<Service> listService(KubernetesConfig.Kubernetes kubernetes) {
        ServiceList serviceList = KubeClient.build(kubernetes)
                .services()
                .list();
        return serviceList.getItems();
    }

    public static List<Service> listService(KubernetesConfig.Kubernetes kubernetes, String namespace) {
        ServiceList serviceList = KubeClient.build(kubernetes)
                .services()
                .inNamespace(namespace)
                .list();
        if (CollectionUtils.isEmpty(serviceList.getItems()))
            return Collections.emptyList();
        return serviceList.getItems();
    }

    /**
     * @param kubernetes
     * @param namespace
     * @param name       podName
     * @return
     */
    public static Service getService(KubernetesConfig.Kubernetes kubernetes, String namespace, String name) {
        return KubeClient.build(kubernetes)
                .services()
                .inNamespace(namespace)
                .withName(name)
                .get();
    }

    public static Service createOrReplaceService(KubernetesConfig.Kubernetes kubernetes, Service service) {
        return KubeClient.build(kubernetes)
                .services()
                .inNamespace(service.getMetadata().getNamespace())
                .createOrReplace(service);
    }

    public static Service createOrReplaceService(KubernetesConfig.Kubernetes kubernetes, String content) {
        KubernetesClient kuberClient = KubeClient.build(kubernetes);
        Service service = toService(kuberClient, content);
        return createOrReplaceService(kubernetes, service);
    }

    /**
     * 配置文件转换为服务资源
     *
     * @param kuberClient
     * @param content     YAML
     * @return
     * @throws RuntimeException
     */
    public static Service toService(KubernetesClient kuberClient, String content) throws RuntimeException {
        HasMetadata resource =  KubernetesUtil.toResource(kuberClient,content);
        if (resource instanceof Service)
            return (Service) resource;
        throw new RuntimeException("类型不匹配");
    }

}
