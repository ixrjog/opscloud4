package com.baiyi.opscloud.datasource.kubernetes;

import com.baiyi.opscloud.datasource.kubernetes.base.BaseKubernetesTest;
import com.baiyi.opscloud.datasource.kubernetes.client.KubeClient;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesPodDriver;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2022/7/9 12:01
 * @Version 1.0
 */
public class KubernetesPodTest extends BaseKubernetesTest {

    @Test
    void getLogTest() {
        String l = KubernetesPodDriver.getPodLog(getConfig().getKubernetes(), "prod", "posp-7878b5d9ff-cf95v", "aaaa");
        print(l);
    }

    @Test
    void getLogTest2() {
        String l = KubernetesPodDriver.getPodLog(getConfig().getKubernetes(), "prod", "mail-5c5b8dd569-vb6kd", "mail");
        print(l);
    }


    @Test
    void getLogTest3() {
        try {
            KubernetesClient kc = KubeClient.build(getConfigById(KubernetesClusterConfigs.ACK_DEV).getKubernetes());
            kc.pods()
                    .inNamespace("dev")
                    .withName("merchant-rss-dev-5479c9c66b-hh8qj")
                    .inContainer("merchant-rss-dev")
                    .tailingLines(100)
                    .watchLog(System.out);
            Thread.sleep(10 * 1000L);
            kc.close();
        } catch (
                Exception e) {
            e.printStackTrace();
        }

    }

}
