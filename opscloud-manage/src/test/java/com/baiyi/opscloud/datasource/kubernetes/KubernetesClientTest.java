package com.baiyi.opscloud.datasource.kubernetes;

import com.baiyi.opscloud.BaseUnit;
import io.fabric8.kubernetes.api.model.NamespaceList;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import org.junit.jupiter.api.Test;

import static com.baiyi.opscloud.datasource.kubernetes.client.KubeClient.CONNECTION_TIMEOUT;
import static com.baiyi.opscloud.datasource.kubernetes.client.KubeClient.REQUEST_TIMEOUT;

/**
 * @Author baiyi
 * @Date 2022/1/25 1:24 PM
 * @Version 1.0
 */
public class KubernetesClientTest extends BaseUnit {

    private final static String token = "";

    @Test
    void test() {
        System.setProperty(io.fabric8.kubernetes.client.Config.KUBERNETES_KUBECONFIG_FILE,
                "/Users/liangjian/opscloud-data/kubernetes/eks-test/kubeconfig");

        io.fabric8.kubernetes.client.Config config = new ConfigBuilder()
                //.withMasterUrl kubeconfg中获取
                .withCaCertData(token)
                .withTrustCerts(true)
                .build();
        // /Users/liangjian/opscloud-data/kubernetes/eks-test/kubeconfig
        config.setConnectionTimeout(CONNECTION_TIMEOUT);
        config.setRequestTimeout(REQUEST_TIMEOUT);
        DefaultKubernetesClient defaultKubernetesClient = new DefaultKubernetesClient(config);

        NamespaceList nl = defaultKubernetesClient.namespaces().list();
        print(nl);
    }


}
