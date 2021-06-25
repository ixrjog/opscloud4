package com.baiyi.caesar.datasource.kubernetes.client;

import com.baiyi.caesar.common.datasource.config.DsKubernetesConfig;
import com.google.common.base.Joiner;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author baiyi
 * @Date 2021/6/24 5:07 下午
 * @Version 1.0
 */
@Slf4j
public class KubeClient {

    private static final int CONNECTION_TIMEOUT = 30 * 1000;
    private static final int REQUEST_TIMEOUT = 30 * 1000;

    public static KubernetesClient build(DsKubernetesConfig.Kubernetes kubernetes) {
        System.setProperty(io.fabric8.kubernetes.client.Config.KUBERNETES_KUBECONFIG_FILE,
                buildKubeconfPath(kubernetes));

        io.fabric8.kubernetes.client.Config config = new ConfigBuilder()
                //.withMasterUrl(ocKubernetesCluster.getMasterUrl())
                //.withMasterUrl("https://121.40.168.164:6443")
                .withTrustCerts(true)
                .build();
        config.setConnectionTimeout(CONNECTION_TIMEOUT);
        config.setRequestTimeout(REQUEST_TIMEOUT);
        return new DefaultKubernetesClient(config);
    }

    private static String buildKubeconfPath(DsKubernetesConfig.Kubernetes kubernetes) {
        String path = Joiner.on("/").join(kubernetes.getKubeconfig().getPath(), io.fabric8.kubernetes.client.Config.KUBERNETES_KUBECONFIG_FILE);
        return path.replace("${HOME}", System.getenv("HOME"));
    }
}
