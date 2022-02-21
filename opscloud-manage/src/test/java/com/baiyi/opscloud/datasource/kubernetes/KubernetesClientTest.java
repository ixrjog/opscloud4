package com.baiyi.opscloud.datasource.kubernetes;

import com.baiyi.opscloud.datasource.kubernetes.base.BaseKubernetesTest;
import io.fabric8.kubernetes.api.model.NamespaceList;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2022/1/25 1:24 PM
 * @Version 1.0
 */
public class KubernetesClientTest extends BaseKubernetesTest {

    @Test
    void test() {
        DefaultKubernetesClient defaultKubernetesClient = buildClient();
        NamespaceList nl = defaultKubernetesClient.namespaces().list();
        print(nl);
    }

}
