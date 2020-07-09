package com.baiyi.opscloud.kubernetes;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.facade.KubernetesFacade;
import com.baiyi.opscloud.kubernetes.handler.KubernetesClusterHandler;
import com.baiyi.opscloud.kubernetes.handler.KubernetesDeploymentHandler;
import com.baiyi.opscloud.kubernetes.handler.KubernetesPodHandler;
import com.baiyi.opscloud.kubernetes.handler.KubernetesServiceHandler;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentList;
import io.fabric8.kubernetes.api.model.apps.ReplicaSetList;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/6/28 5:49 下午
 * @Version 1.0
 */
public class KubernetesTest extends BaseUnit {

    @Resource
    private KubernetesClusterHandler kubernetesClusterHandler;

    @Resource
    private KubernetesPodHandler kubernetesPodHandler;

    @Resource
    private KubernetesDeploymentHandler kubernetesDeploymentHandler;

    @Resource
    private KubernetesServiceHandler kubernetesServiceHandler;

    @Resource
    private KubernetesFacade kubernetesFacade;

    @Test
    void getNamespaceListTest() {
        NamespaceList namespaceList = kubernetesClusterHandler.getNamespaceList("k8s-test");
        System.err.println(namespaceList);
    }

    @Test
    void getReplicaSetListTest() {
        ReplicaSetList rs = kubernetesClusterHandler.getReplicaSetList("k8s-test", "test");
        System.err.println(rs);
    }

    @Test
    void getServiceListTest() {
        ServiceList sl = kubernetesServiceHandler.getServiceList("k8s-test", "test");
        System.err.println(sl);
    }

    @Test
    void getDeploymentListTest() {
        DeploymentList dl = kubernetesDeploymentHandler.getDeploymentList("k8s-test", "test");
        List<Deployment> items = dl.getItems();
        items.forEach(e -> System.err.println(e));
        System.err.println(dl.getItems());
    }

    @Test
    void getNodeListTest() {
        NodeList nodeList = kubernetesClusterHandler.getNodeList("k8s-test");
        System.err.println(nodeList.getItems());
    }

    @Test
    void getPodListTest() {
        //  abtest-frontend-deployment-77cc6d9789-lb7km
        PodList podList = kubernetesPodHandler.getPodList("k8s-test", "test");
        List<Pod> pods = podList.getItems().stream().filter(e -> e.getStatus().getPhase().equals("Running")).collect(Collectors.toList());
        // 1220
        System.err.println(pods);
    }

    @Test
    void getPodList2Test() {
        //  abtest-frontend-deployment-77cc6d9789-lb7km
        PodList podList = kubernetesPodHandler.getPodList("k8s-test", "test");
        List<Pod> pods = podList.getItems().stream().filter(e -> e.getStatus().getPhase().equals("Running")).collect(Collectors.toList());
        // 1220
        System.err.println(pods);
    }

    @Test
    void getPodListByLabelTest() {
        PodList podList = kubernetesPodHandler.getPodListByLabel("k8s-test", "test", "beauty-service-daily");
        System.err.println(podList.getItems());
    }

    @Test
    void getPodTest() {
        Pod pod = kubernetesPodHandler.getPod("k8s-test", "test", "abtest-frontend-deployment-77cc6d9789-lb7km");
        System.err.println(pod);
    }

    @Test
    void syncKubernetesDeploymentTest() {
        kubernetesFacade.syncKubernetesDeployment(3);
    }

    @Test
    void syncKubernetesServiceTest() {
        kubernetesFacade.syncKubernetesService(3);
    }

}