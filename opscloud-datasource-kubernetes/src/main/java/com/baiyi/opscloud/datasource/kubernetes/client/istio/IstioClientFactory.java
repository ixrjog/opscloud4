package com.baiyi.opscloud.datasource.kubernetes.client.istio;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.client.provider.DefaultKubernetesProvider;
import io.fabric8.istio.client.DefaultIstioClient;
import io.fabric8.istio.client.IstioClient;

/**
 * @Author baiyi
 * @Date 2023/10/7 15:28
 * @Version 1.0
 */
public class IstioClientFactory {

    private IstioClientFactory() {
        throw new IllegalStateException("Utility class");
    }

    public static IstioClient newClient(KubernetesConfig.Kubernetes kubernetes) {
        DefaultKubernetesProvider.buildConfig(kubernetes);
        io.fabric8.kubernetes.client.Config cfg = DefaultKubernetesProvider.buildConfig(kubernetes);
        return new DefaultIstioClient(cfg);
    }

}