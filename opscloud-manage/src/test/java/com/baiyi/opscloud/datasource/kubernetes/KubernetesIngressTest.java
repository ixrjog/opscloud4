package com.baiyi.opscloud.datasource.kubernetes;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.base.BaseKubernetesTest;
import com.baiyi.opscloud.datasource.kubernetes.client.MyKubernetesClientBuilder;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesIngressDriver;
import io.fabric8.kubernetes.api.model.networking.v1.Ingress;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2023/7/6 11:46
 * @Version 1.0
 */
public class KubernetesIngressTest extends BaseKubernetesTest {


    @Test
    void test() {
        KubernetesConfig kubernetesConfig = getConfigById(83);
        Ingress ingress = KubernetesIngressDriver.get(kubernetesConfig.getKubernetes(), "dev", "oneloop-home-dev");
        print(ingress);
    }

    @Test
    void test2() {
        KubernetesConfig kubernetesConfig = getConfigById(83);

        try (KubernetesClient kc = MyKubernetesClientBuilder.build(kubernetesConfig.getKubernetes())) {
            io.fabric8.kubernetes.api.model.networking.v1.Ingress il = kc.network().v1().ingresses().inNamespace("dev").withName("oneloop-home-dev").get();
            print(il);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // List<Ingress> ingresses = KubernetesIngressDriver.list(kubernetesConfig.getKubernetes(), "dev");
        // print(ingresses);
    }

}
