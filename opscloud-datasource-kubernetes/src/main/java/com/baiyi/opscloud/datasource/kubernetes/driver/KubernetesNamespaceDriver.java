package com.baiyi.opscloud.datasource.kubernetes.driver;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.client.KubeClient;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.NamespaceList;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/6/24 9:11 下午
 * @Version 1.0
 */
public class KubernetesNamespaceDriver {

    public static List<Namespace> listNamespace(KubernetesConfig.Kubernetes kubernetes) {
        NamespaceList namespaceList = KubeClient.build(kubernetes)
                .namespaces()
                .list();
        return namespaceList.getItems().stream().filter(e -> filter(kubernetes, e)
        ).collect(Collectors.toList());
    }

    private static boolean filter(KubernetesConfig.Kubernetes kubernetes, Namespace namespace) {
        for (String s : kubernetes.getNamespace().getIgnore()) {
            if (namespace.getMetadata().getName().equalsIgnoreCase(s))
                return false;
        }
        return true;
    }
}
