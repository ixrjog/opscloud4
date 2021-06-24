package com.baiyi.caesar.datasource.kubernetes.handler;

import com.baiyi.caesar.common.datasource.config.DsKubernetesConfig;
import com.baiyi.caesar.datasource.kubernetes.client.KubeClient;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/24 11:16 下午
 * @Version 1.0
 */
public class KubernetesPodHandler {

    public static List<Pod> listPod(DsKubernetesConfig.Kubernetes kubernetes) {
        PodList podList = KubeClient.build(kubernetes)
                .pods()
                .list();
        return podList.getItems();
    }

    public static List<Pod> listPod(DsKubernetesConfig.Kubernetes kubernetes, String namespace) {
        PodList podList = KubeClient.build(kubernetes)
                .pods()
                .inNamespace(namespace)
                .list();
        if (CollectionUtils.isEmpty(podList.getItems()))
            return Collections.emptyList();
        return podList.getItems();
    }

    /**
     *
     * @param kubernetes
     * @param namespace
     * @param name podName
     * @return
     */
    public static Pod getPod(DsKubernetesConfig.Kubernetes kubernetes, String namespace, String name) {
        return KubeClient.build(kubernetes)
                .pods()
                .inNamespace(namespace)
                .withName(name)
                .get();
    }
}
