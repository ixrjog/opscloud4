package com.baiyi.opscloud.datasource.kubernetes.client.istio;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.client.provider.AmazonEksProvider;
import com.baiyi.opscloud.datasource.kubernetes.client.provider.DefaultKubernetesProvider;
import io.fabric8.istio.client.DefaultIstioClient;
import io.fabric8.istio.client.IstioClient;
import org.apache.commons.lang3.StringUtils;

import java.net.URISyntaxException;

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
        try {
            io.fabric8.kubernetes.client.Config cfg = buildConfigWithProvider(kubernetes);
            return new DefaultIstioClient(cfg);
        } catch (URISyntaxException e) {
            return null;
        }
    }

    private static io.fabric8.kubernetes.client.Config buildConfigWithProvider(KubernetesConfig.Kubernetes kubernetes) throws URISyntaxException {
        if (StringUtils.isNotBlank(kubernetes.getProvider())) {
            return AmazonEksProvider.buildConfig(kubernetes);
        }
        return DefaultKubernetesProvider.buildConfig(kubernetes);
    }

}