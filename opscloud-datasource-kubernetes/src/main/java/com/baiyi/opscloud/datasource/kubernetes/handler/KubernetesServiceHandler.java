package com.baiyi.opscloud.datasource.kubernetes.handler;

import com.baiyi.opscloud.common.datasource.config.DsKubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.client.KubeClient;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceList;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/25 3:55 下午
 * @Version 1.0
 */
public class KubernetesServiceHandler {

    public static List<Service> listService(DsKubernetesConfig.Kubernetes kubernetes) {
        ServiceList serviceList = KubeClient.build(kubernetes)
                .services()
                .list();
        return serviceList.getItems();
    }

    public static List<Service> listService(DsKubernetesConfig.Kubernetes kubernetes, String namespace) {
        ServiceList serviceList = KubeClient.build(kubernetes)
                .services()
                .inNamespace(namespace)
                .list();
        if (CollectionUtils.isEmpty(serviceList .getItems()))
            return Collections.emptyList();
        return serviceList .getItems();
    }

    /**
     * @param kubernetes
     * @param namespace
     * @param name       podName
     * @return
     */
    public static Service getService(DsKubernetesConfig.Kubernetes kubernetes, String namespace, String name) {
        return KubeClient.build(kubernetes)
                .services()
                .inNamespace(namespace)
                .withName(name)
                .get();
    }
}
