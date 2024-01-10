package com.baiyi.opscloud.datasource.kubernetes.client.istio;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import io.fabric8.istio.client.IstioClient;

/**
 * @Author baiyi
 * @Date 2023/10/7 15:24
 * @Version 1.0
 */
public class IstioClientBuilder {

    public static IstioClient build(KubernetesConfig.Kubernetes kubernetes) {
        return IstioClientFactory.newClient(kubernetes);
    }

}