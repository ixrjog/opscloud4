package com.baiyi.opscloud.datasource.kubernetes.client;

import com.baiyi.opscloud.common.datasource.KubernetesDsInstanceConfig;
import com.baiyi.opscloud.core.util.SystemEnvUtil;
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

    public static KubernetesClient build(KubernetesDsInstanceConfig.Kubernetes kubernetes) {
        System.setProperty(io.fabric8.kubernetes.client.Config.KUBERNETES_KUBECONFIG_FILE,
                buildKubeconfigPath(kubernetes));
        io.fabric8.kubernetes.client.Config config = new ConfigBuilder()
                //.withMasterUrl kubeconfg中获取
                .withTrustCerts(true)
                .build();
        config.setConnectionTimeout(CONNECTION_TIMEOUT);
        config.setRequestTimeout(REQUEST_TIMEOUT);
        return new DefaultKubernetesClient(config);
    }

    private static String buildKubeconfigPath(KubernetesDsInstanceConfig.Kubernetes kubernetes) {
        String path = Joiner.on("/").join(kubernetes.getKubeconfig().getPath(), io.fabric8.kubernetes.client.Config.KUBERNETES_KUBECONFIG_FILE);
        return SystemEnvUtil.renderEnvHome(path);
    }
}
