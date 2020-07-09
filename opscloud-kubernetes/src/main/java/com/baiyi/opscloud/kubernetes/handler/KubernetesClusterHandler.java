package com.baiyi.opscloud.kubernetes.handler;

import com.baiyi.opscloud.kubernetes.client.KubernetesClientContainer;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.ReplicaSetList;
import io.fabric8.kubernetes.client.AppsAPIGroupClient;
import io.fabric8.kubernetes.client.dsl.AppsAPIGroupDSL;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/6/28 5:42 下午
 * @Version 1.0
 */
@Component
public class KubernetesClusterHandler {

    @Resource
    private KubernetesClientContainer kubernetesClientContainer;

    public NamespaceList getNamespaceList(String clusterName) {
        try {
            return kubernetesClientContainer.getClient(clusterName).namespaces().list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new NamespaceList();
    }

    public AppsAPIGroupDSL getApps(String clusterName) {
        try {
            return kubernetesClientContainer.getClient(clusterName).apps();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new AppsAPIGroupClient();
    }

    public ReplicationControllerList getReplicationControllerList(String clusterName, String namespace) {
        try {
            return kubernetesClientContainer.getClient(clusterName).replicationControllers().inNamespace(namespace).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ReplicationControllerList();
    }

    public ReplicaSetList getReplicaSetList(String clusterName, String namespace) {
        try {
            return kubernetesClientContainer.getClient(clusterName).apps().replicaSets().inNamespace(namespace).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ReplicaSetList();
    }




    public NodeList getNodeList(String clusterName) {
        try {
            return kubernetesClientContainer.getClient(clusterName).nodes().list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new NodeList();
    }

}
