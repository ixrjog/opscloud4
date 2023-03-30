package com.baiyi.opscloud.datasource.kubernetes;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.base.BaseKubernetesTest;
import com.baiyi.opscloud.datasource.kubernetes.client.MyKubernetesClientBuilder;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesPodDriver;
import io.fabric8.kubernetes.api.model.ContainerStatus;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.LogWatch;
import org.junit.jupiter.api.Test;
import org.springframework.web.socket.TextMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2022/7/9 12:01
 * @Version 1.0
 */
public class KubernetesPodTest extends BaseKubernetesTest {

    @Test
    void getLogTest() {
        String l = KubernetesPodDriver.getLog(getConfig().getKubernetes(), "prod", "posp-7878b5d9ff-cf95v", "aaaa");
        print(l);
    }

    @Test
    void getLogTest2() {
        String l = KubernetesPodDriver.getLog(getConfig().getKubernetes(), "prod", "mail-5c5b8dd569-vb6kd", "mail");
        print(l);
    }


    @Test
    void getLogTest3() {
        try {
            KubernetesConfig kubernetesConfig = getConfigById(KubernetesClusterConfigs.ACK_DEV);

            KubernetesClient kc = MyKubernetesClientBuilder.build(kubernetesConfig.getKubernetes());
            kc.pods()
                    .inNamespace("dev")
                    .withName("merchant-rss-dev-766874c898-654hr")
                    .inContainer("merchant-rss-dev")
                    .withLogWaitTimeout(0)
                    .watchLog(System.out);
            Thread.sleep(10 * 1000L);
            kc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void getLogTest4() {
        try {
            KubernetesClient kubernetesClient = MyKubernetesClientBuilder.build(getConfigById(KubernetesClusterConfigs.ACK_DEV).getKubernetes());

            LogWatch logWatch = kubernetesClient.pods().inNamespace("dev").withName("merchant-rss-dev-645c8964db-l4vk7").inContainer("merchant-rss-dev").withPrettyOutput().watchLog(System.out);
            InputStream is = logWatch.getOutput();
            Thread.sleep(10 * 1000L);


            InputStream output = logWatch.getOutput();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(output));
            String line;
            try {
                while ((line = bufferedReader.readLine()) != null) {
                    print(new TextMessage(line));
                }
            } catch (IOException e) {

            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    void getLogTest5() {
        KubernetesConfig kubernetesConfig = getConfigById(KubernetesClusterConfigs.EKS_TEST);
        while (true) {
            List<Pod> pods = KubernetesPodDriver.list(kubernetesConfig.getKubernetes(), "test", "c-front");
            for (Pod pod : pods) {
                Optional<ContainerStatus> optCs = pod.getStatus().getContainerStatuses().stream().filter(e -> e.getName().equals("c-front")).findFirst();
                if (!optCs.isPresent()) continue;
                ContainerStatus cs = optCs.get();
                if (cs.getState().getTerminated() != null)
                    print(cs.getState());
            }
        }
    }

}
