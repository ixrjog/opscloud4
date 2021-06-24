package com.baiyi.caesar.datasource.kubernetes.handler;

import com.baiyi.caesar.common.datasource.config.DsKubernetesConfig;
import com.baiyi.caesar.datasource.kubernetes.client.KubeClient;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.NamespaceList;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/6/24 9:11 下午
 * @Version 1.0
 */
public class KubernetesNamespaceHandler {

    public static List<Namespace> listNamespace(DsKubernetesConfig.Kubernetes kubernetes) {
        NamespaceList namespaceList = KubeClient.build(kubernetes).namespaces().list();
        return namespaceList.getItems().stream().filter(e -> filter(kubernetes, e)
        ).collect(Collectors.toList());
    }

    private static boolean filter(DsKubernetesConfig.Kubernetes kubernetes, Namespace namespace) {
        for (String s : kubernetes.getNamespace().getFilter()) {
            if (namespace.getMetadata().getName().equalsIgnoreCase(s))
                return false;
        }
        return true;
    }
}
