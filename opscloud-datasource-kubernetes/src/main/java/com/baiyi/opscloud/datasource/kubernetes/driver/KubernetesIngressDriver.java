package com.baiyi.opscloud.datasource.kubernetes.driver;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.client.MyKubernetesClientBuilder;
import io.fabric8.kubernetes.api.model.networking.v1.Ingress;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2023/7/6 11:35
 * @Version 1.0
 */
@Slf4j
public class KubernetesIngressDriver {

//    public static List<Ingress> list(KubernetesConfig.Kubernetes kubernetes, String namespace) {
//        try (KubernetesClient kc = MyKubernetesClientBuilder.build(kubernetes)) {
//           return kc.network()
//                    .ingress()
//                    .inNamespace(namespace)
//                    .withName("oneloop-home-dev").item();
//
//        } catch (Exception e) {
//            log.warn(e.getMessage());
//            throw e;
//        }
//    }

    public static Ingress get(KubernetesConfig.Kubernetes kubernetes, String namespace, String name) {
        try (KubernetesClient kc = MyKubernetesClientBuilder.build(kubernetes)) {
            return kc.network()
                    .v1()
                    .ingresses()
                    .inNamespace(namespace)
                    .withName(name)
                    .item();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public static List<Ingress> list(KubernetesConfig.Kubernetes kubernetes, String namespace) {
        try (KubernetesClient kc = MyKubernetesClientBuilder.build(kubernetes)) {
            return kc.network()
                    .v1()
                    .ingresses()
                    .inNamespace(namespace)
                    .list()
                    .getItems();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public static List<Ingress> create(KubernetesConfig.Kubernetes kubernetes, String namespace) {
        try (KubernetesClient kc = MyKubernetesClientBuilder.build(kubernetes)) {
            return kc.network()
                    .v1()
                    .ingresses()
                    .inNamespace(namespace)
                    .list()
                    .getItems();

        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

}
