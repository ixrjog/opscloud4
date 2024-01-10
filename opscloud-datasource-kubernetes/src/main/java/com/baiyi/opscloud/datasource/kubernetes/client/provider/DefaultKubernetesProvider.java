package com.baiyi.opscloud.datasource.kubernetes.client.provider;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.core.util.SystemEnvUtil;
import com.baiyi.opscloud.datasource.kubernetes.client.MyKubernetesClientBuilder;
import com.google.common.base.Joiner;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;

import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2022/9/14 09:40
 * @Version 1.0
 */
public class DefaultKubernetesProvider {

    /**
     * io.fabric8.kubernetes.client.Config.*
     */
    public static final String KUBERNETES_KUBECONFIG_FILE = "kubeconfig";
    public static final String KUBERNETES_REQUEST_TIMEOUT_SYSTEM_PROPERTY = "kubernetes.request.timeout";
    public static final String KUBERNETES_WEBSOCKET_TIMEOUT_SYSTEM_PROPERTY = "kubernetes.websocket.timeout";
    public static final String KUBERNETES_CONNECTION_TIMEOUT_SYSTEM_PROPERTY = "kubernetes.connection.timeout";

    /**
     * 5.0
     * return new DefaultKubernetesClient(config);
     *
     * @param kubernetes
     * @return
     */
    public static KubernetesClient buildClient(KubernetesConfig.Kubernetes kubernetes) {
        return new KubernetesClientBuilder()
                .withConfig(buildConfig(kubernetes))
                .build();
    }

    public static io.fabric8.kubernetes.client.Config buildConfig(KubernetesConfig.Kubernetes kubernetes) {
        preSet(kubernetes);
        return new ConfigBuilder()
                .withTrustCerts(true)
                // .withWebsocketTimeout(KubeClient.Config.WEBSOCKET_TIMEOUT)
                // .withConnectionTimeout(KubeClient.Config.CONNECTION_TIMEOUT)
                // .withRequestTimeout(KubeClient.Config.REQUEST_TIMEOUT)
                .build();
    }

    private static void preSet(KubernetesConfig.Kubernetes kubernetes) {
        System.setProperty(KUBERNETES_KUBECONFIG_FILE, toKubeconfigPath(kubernetes));
        System.setProperty(KUBERNETES_REQUEST_TIMEOUT_SYSTEM_PROPERTY,
                String.valueOf(MyKubernetesClientBuilder.Values.REQUEST_TIMEOUT));
        System.setProperty(KUBERNETES_WEBSOCKET_TIMEOUT_SYSTEM_PROPERTY,
                String.valueOf(MyKubernetesClientBuilder.Values.WEBSOCKET_TIMEOUT));
        System.setProperty(KUBERNETES_CONNECTION_TIMEOUT_SYSTEM_PROPERTY,
                String.valueOf(MyKubernetesClientBuilder.Values.CONNECTION_TIMEOUT));
    }

    private static String toKubeconfigPath(KubernetesConfig.Kubernetes kubernetes) {
        String kubeConfigPath = Optional.ofNullable(kubernetes)
                .map(KubernetesConfig.Kubernetes::getKubeconfig)
                .map(KubernetesConfig.Kubeconfig::getPath)
                .orElse("");
        String path = Joiner.on("/").join(kubeConfigPath,
                io.fabric8.kubernetes.client.Config.KUBERNETES_KUBECONFIG_FILE);
        return SystemEnvUtil.renderEnvHome(path);
    }

}