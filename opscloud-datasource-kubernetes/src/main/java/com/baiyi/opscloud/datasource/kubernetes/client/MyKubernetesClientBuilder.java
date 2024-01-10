package com.baiyi.opscloud.datasource.kubernetes.client;

import com.baiyi.opscloud.common.constants.KubernetesProviders;
import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.client.provider.AmazonEksProvider;
import com.baiyi.opscloud.datasource.kubernetes.client.provider.DefaultKubernetesProvider;
import com.baiyi.opscloud.datasource.kubernetes.exception.KubernetesException;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.net.URISyntaxException;

/**
 * @Author baiyi
 * @Date 2021/6/24 5:07 下午
 * @Version 1.0
 */
@Slf4j
public class MyKubernetesClientBuilder {

    public interface Values {
        int CONNECTION_TIMEOUT = 60 * 1000;
        int REQUEST_TIMEOUT = 60 * 1000;
        int WEBSOCKET_TIMEOUT = 60 * 1000;
    }

    public static KubernetesClient build(KubernetesConfig.Kubernetes kubernetes) {
        if (StringUtils.isNotBlank(kubernetes.getProvider())) {
            return buildWithProvider(kubernetes);
        }
        return DefaultKubernetesProvider.buildClient(kubernetes);
    }

    /**
     * 按供应商构建 client
     *
     * @param kubernetes
     * @return
     */
    private static KubernetesClient buildWithProvider(KubernetesConfig.Kubernetes kubernetes) {
        if (KubernetesProviders.AMAZON_EKS.getDesc().equalsIgnoreCase(kubernetes.getProvider())) {
            try {
                return AmazonEksProvider.buildClientWithProvider(kubernetes);
            } catch (URISyntaxException e) {
                throw new KubernetesException("KubernetesClient error: {}", e.getMessage());
            }
        }
        throw new KubernetesException("无效的供应商: provider={}", kubernetes.getProvider());
    }

}