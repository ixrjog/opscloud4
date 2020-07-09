package com.baiyi.opscloud.kubernetes.handler;

import com.baiyi.opscloud.kubernetes.client.KubernetesClientContainer;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/7/4 3:58 下午
 * @Version 1.0
 */
@Component
public class KubernetesPodHandler {

    @Resource
    private KubernetesClientContainer kubernetesClientContainer;

    public PodList getPodList(String clusterName, String namespace) {
        try {
            return kubernetesClientContainer.getClient(clusterName).pods().inNamespace(namespace).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PodList();
    }

    public Pod getPod(String clusterName, String namespace, String name) {
        try {
            return kubernetesClientContainer.getClient(clusterName).pods().inNamespace(namespace).withName(name).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public PodList getPodListByLabel(String clusterName, String namespace, String label) {
        return getPodListByLabel(clusterName, namespace, "app", label);
    }

    public PodList getPodListByLabel(String clusterName, String namespace, String labelName, String labelVaule) {
        try {
            return kubernetesClientContainer.getClient(clusterName).pods().inNamespace(namespace).withLabel(labelName, labelVaule).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
