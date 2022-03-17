package com.baiyi.opscloud.datasource.kubernetes.client;

import com.baiyi.opscloud.common.constants.KubernetesProviders;
import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.core.util.SystemEnvUtil;
import com.baiyi.opscloud.datasource.kubernetes.client.provider.AmazonEksProvider;
import com.google.common.base.Joiner;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
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
public class KubeClient {

    public static final int CONNECTION_TIMEOUT = 30 * 1000;
    public static final int REQUEST_TIMEOUT = 30 * 1000;

    public static KubernetesClient build(KubernetesConfig.Kubernetes kubernetes) {
        if(StringUtils.isNotBlank(kubernetes.getProvider()))
            return buildByProvider(kubernetes);
        System.setProperty(io.fabric8.kubernetes.client.Config.KUBERNETES_KUBECONFIG_FILE,
                buildKubeconfigPath(kubernetes));
        io.fabric8.kubernetes.client.Config config = new ConfigBuilder()
                .withTrustCerts(true)
                .build();
        config.setConnectionTimeout(CONNECTION_TIMEOUT);
        config.setRequestTimeout(REQUEST_TIMEOUT);
        return new DefaultKubernetesClient(config);
    }

    /**
     * 按供应商构建 client
     * @param kubernetes
     * @return
     */
    private static KubernetesClient buildByProvider(KubernetesConfig.Kubernetes kubernetes) {
        if (KubernetesProviders.AMAZON_EKS.getDesc().equalsIgnoreCase(kubernetes.getProvider())) {
            try {
                String token = AmazonEksProvider.generateEksToken(kubernetes.getAmazonEks());
                return build(kubernetes.getAmazonEks().getUrl(), token);
            } catch (URISyntaxException e) {
            }
        }
        return null;
    }

    private static KubernetesClient build(String url, String token) {
        io.fabric8.kubernetes.client.Config config = new ConfigBuilder()
                .withMasterUrl(url)
                .withOauthToken(token)
                .withTrustCerts(true)
                .build();
        config.setConnectionTimeout(CONNECTION_TIMEOUT);
        config.setRequestTimeout(REQUEST_TIMEOUT);
        return new DefaultKubernetesClient(config);
    }

    private static String buildKubeconfigPath(KubernetesConfig.Kubernetes kubernetes) {
        String path = Joiner.on("/").join(kubernetes.getKubeconfig().getPath(), io.fabric8.kubernetes.client.Config.KUBERNETES_KUBECONFIG_FILE);
        return SystemEnvUtil.renderEnvHome(path);
    }

}
