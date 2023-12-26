package com.baiyi.opscloud.datasource.kubernetes.client.provider;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.client.MyKubernetesClientBuilder;
import com.baiyi.opscloud.datasource.kubernetes.client.provider.eks.AmazonEksGenerator;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;

/**
 * @Author baiyi
 * @Date 2022/9/14 09:37
 * @Version 1.0
 */
@Slf4j
@Component
public class AmazonEksProvider {

    private static AmazonEksGenerator amazonEksGenerator;

    public static final String KUBERNETES_REQUEST_TIMEOUT_SYSTEM_PROPERTY = "kubernetes.request.timeout";
    public static final String KUBERNETES_WEBSOCKET_TIMEOUT_SYSTEM_PROPERTY = "kubernetes.websocket.timeout";
    public static final String KUBERNETES_CONNECTION_TIMEOUT_SYSTEM_PROPERTY = "kubernetes.connection.timeout";

    @Autowired
    public void setAmazonEksHelper(AmazonEksGenerator amazonEksGenerator) {
        AmazonEksProvider.amazonEksGenerator = amazonEksGenerator;
    }

    /**
     * 按供应商构建 client
     *
     * @param kubernetes
     * @return
     */
    public static KubernetesClient buildClientWithProvider(KubernetesConfig.Kubernetes kubernetes) throws URISyntaxException {
        String token = amazonEksGenerator.generateEksToken(kubernetes.getAmazonEks());
        preSet(kubernetes);
        return build(kubernetes.getAmazonEks().getUrl(), token);
    }

    /**
     * 5.x
     * return new DefaultKubernetesClient(config);
     * <p>
     * 6.x 写法
     * return new KubernetesClientBuilder().withConfig(config).build();
     *
     * @param url
     * @param token
     * @return
     */
    private static KubernetesClient build(String url, String token) {
        io.fabric8.kubernetes.client.Config config = new ConfigBuilder()
                .withMasterUrl(url)
                .withOauthToken(token)
                .withTrustCerts(true)
                .withWatchReconnectInterval(60000)
                .build();

        return new KubernetesClientBuilder().withConfig(config).build();
    }

    /**
     * 注入配置
     *
     * @param kubernetes
     */
    private static void preSet(KubernetesConfig.Kubernetes kubernetes) {
        System.setProperty(KUBERNETES_REQUEST_TIMEOUT_SYSTEM_PROPERTY,
                String.valueOf(MyKubernetesClientBuilder.Values.REQUEST_TIMEOUT));
        System.setProperty(KUBERNETES_WEBSOCKET_TIMEOUT_SYSTEM_PROPERTY,
                String.valueOf(MyKubernetesClientBuilder.Values.WEBSOCKET_TIMEOUT));
        System.setProperty(KUBERNETES_CONNECTION_TIMEOUT_SYSTEM_PROPERTY,
                String.valueOf(MyKubernetesClientBuilder.Values.CONNECTION_TIMEOUT));
    }

}