package com.baiyi.opscloud.datasource.kubernetes.driver;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.NamespaceList;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/6/24 9:11 下午
 * @Version 1.0
 */
@Slf4j
public class KubernetesNamespaceDriver {

    public static List<Namespace> listNamespace(KubernetesConfig.Kubernetes kubernetes) {
        try (KubernetesClient kc = KubernetesClientBuilder.build(kubernetes)) {
            NamespaceList namespaceList = kc.namespaces()
                    .list();
            return namespaceList.getItems().stream().filter(e -> filter(kubernetes, e)
            ).collect(Collectors.toList());
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public static Namespace getNamespace(KubernetesConfig.Kubernetes kubernetes, String namespace) {
        try (KubernetesClient kc = KubernetesClientBuilder.build(kubernetes)) {
            return kc.namespaces()
                    .withName(namespace)
                    .get();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    private static boolean filter(KubernetesConfig.Kubernetes kubernetes, Namespace namespace) {
        for (String s : kubernetes.getNamespace().getIgnore()) {
            if (namespace.getMetadata().getName().equalsIgnoreCase(s)) {
                return false;
            }
        }
        return true;
    }

}
