package com.baiyi.opscloud.datasource.kubernetes.client.provider;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.core.util.SystemEnvUtil;
import com.baiyi.opscloud.datasource.kubernetes.client.KubeClient;
import com.google.common.base.Joiner;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

/**
 * @Author baiyi
 * @Date 2022/9/14 09:40
 * @Version 1.0
 */
public class DefaultKubernetesProvider {

    public static KubernetesClient buildWithDefault(KubernetesConfig.Kubernetes kubernetes) {
        initConfig(kubernetes);
        io.fabric8.kubernetes.client.Config config = new ConfigBuilder()
                .withTrustCerts(true)
                .withWebsocketTimeout(KubeClient.Config.WEBSOCKET_TIMEOUT)
                .withConnectionTimeout(KubeClient.Config.CONNECTION_TIMEOUT)
                .withRequestTimeout(KubeClient.Config.REQUEST_TIMEOUT)
                .build();
        // 6.x
        // return new KubernetesClientBuilder().withConfig(config).build();
        return new DefaultKubernetesClient(config);
    }

    private static void initConfig(KubernetesConfig.Kubernetes kubernetes) {
        System.setProperty(io.fabric8.kubernetes.client.Config.KUBERNETES_KUBECONFIG_FILE,
                toKubeconfigPath(kubernetes));
    }

    private static String toKubeconfigPath(KubernetesConfig.Kubernetes kubernetes) {
        String path = Joiner.on("/").join(kubernetes.getKubeconfig().getPath(), io.fabric8.kubernetes.client.Config.KUBERNETES_KUBECONFIG_FILE);
        return SystemEnvUtil.renderEnvHome(path);
    }

}
