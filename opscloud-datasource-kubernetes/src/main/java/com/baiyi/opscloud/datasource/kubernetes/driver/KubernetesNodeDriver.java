package com.baiyi.opscloud.datasource.kubernetes.driver;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.client.KubeClient;
import io.fabric8.kubernetes.api.model.Node;
import io.fabric8.kubernetes.api.model.NodeList;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/11/24 2:24 下午
 * @Version 1.0
 */
public class KubernetesNodeDriver {

    public static List<Node> listNode(KubernetesConfig.Kubernetes kubernetes) {
        NodeList nodeList = KubeClient.build(kubernetes).nodes().list();
        return nodeList.getItems();
    }

}
