package com.baiyi.opscloud.datasource.kubernetes.util;

import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.networking.v1beta1.Ingress;
import io.fabric8.kubernetes.client.KubernetesClient;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @Author baiyi
 * @Date 2021/12/8 9:35 AM
 * @Version 1.0
 */
public class KubernetesUtil {

    private KubernetesUtil() {
    }

//    public static HasMetadata toResource(KubernetesClient kubernetesClient, String content) throws KubernetesException {
//        InputStream is = new ByteArrayInputStream(content.getBytes());
//        List<HasMetadata> resources = kubernetesClient.load(is).get();
//        if (resources.isEmpty()) {
//            throw new KubernetesException("转换配置文件错误!");
//        }
//        return resources.get(0);
//    }

    public static Service toService(KubernetesClient kubernetesClient, String yaml) {
        InputStream is = new ByteArrayInputStream(yaml.getBytes());
        return kubernetesClient
                .services()
                .load(is)
                .item();
    }

    public static Deployment toDeployment(KubernetesClient kubernetesClient, String yaml) {
        InputStream is = new ByteArrayInputStream(yaml.getBytes());
        return kubernetesClient
                .apps()
                .deployments()
                .load(is)
                .item();
    }

    public static Ingress toIngress(KubernetesClient kubernetesClient, String yaml) {
        InputStream is = new ByteArrayInputStream(yaml.getBytes());
        return kubernetesClient
                .network()
                .ingress()
                .load(is)
                .item();
    }

}
