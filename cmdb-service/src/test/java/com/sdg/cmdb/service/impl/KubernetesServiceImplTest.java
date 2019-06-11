package com.sdg.cmdb.service.impl;


import com.alibaba.fastjson.JSON;
import com.sdg.cmdb.domain.kubernetes.KubernetesServiceCluster;
import com.sdg.cmdb.domain.kubernetes.KubernetesServiceDO;
import com.sdg.cmdb.domain.server.EnvType;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class KubernetesServiceImplTest {

    @Autowired
    private KubernetesServiceImpl kubernetesService;


    @Test
    public void test() {
        NodeList nodeList = kubernetesService.listNode("k8s-test");
        System.err.println(JSON.toJSONString(nodeList));
    }


    @Test
    public void testRC() {
        ReplicationControllerList list = kubernetesService.listRC("k8s-test");
        System.err.println(JSON.toJSONString(list));
    }


    @Test
    public void testPods() {
        PodList list = kubernetesService.listPods("k8s-test");
        for (Pod p : list.getItems()) {
            if (p.getMetadata().getNamespace().equals("test")) {
                //System.err.println(JSON.toJSONString(p));
                System.err.println("HostIP:"+p.getStatus().getHostIP());
                for(Container c: p.getSpec().getContainers()){
                    System.err.println( "ContainerName:" +  c.getName());
                }
                System.err.println( "Phase:" +  p.getStatus().getPhase());
               // p.getStatus().getPodIP();
            }
        }
       // System.err.println(JSON.toJSONString(list));
    }

    @Test
    public void testApps() {
        MixedOperation<Endpoints, EndpointsList, DoneableEndpoints, Resource<Endpoints, DoneableEndpoints>> ep = kubernetesService.getEndpoints("k8s-test");
        System.err.println(JSON.toJSONString(ep));
    }

    @Test
    public void test2() {
        NamespaceList nsList = kubernetesService.getNamespaceList("k8s-test");
        //  System.err.println(nsList);

        for (Namespace n : nsList.getItems()) {
            System.err.println(JSON.toJSONString(n.getMetadata()));
        }
    }

    @Test
    public void test3() {
        ServiceList serviceList = kubernetesService.getServiceList("k8s-test");
        //    System.err.println(serviceList);
        for (Service service : serviceList.getItems()) {

            if(service.getSpec().getSelector() != null && service.getSpec().getSelector().containsKey("app")){
                String name = service.getSpec().getSelector().get("app");
                if( name.indexOf("promotion-platform") != -1){
                    System.err.println(JSON.toJSONString(service.getMetadata()));
                    System.err.println(JSON.toJSONString(service.getSpec()));
                    System.err.println(JSON.toJSONString(service.getStatus()));
                }
            }


        }

    }

    @Test
    public void test4() {
        kubernetesService.syncCluster("k8s-test");
    }

    @Test
    public void test44() {
        kubernetesService.syncDubbo();
    }

    @Test
    public void test5() {
        KubernetesServiceCluster kubernetesServiceCluster = kubernetesService.getServerList(171, EnvType.EnvTypeEnum.test.getCode(), "http", 4);
        System.err.println(JSON.toJSONString(kubernetesServiceCluster.getServerList()));
    }


    @Test
    public void testGetServcie() {
        KubernetesServiceDO kubernetesServiceDO = kubernetesService.getService("com.ggj.item.audit.api.AuditSampleApi", "k8s-test","test");
        System.err.println(kubernetesServiceDO);
    }


    @Test
    public void test99() {
        kubernetesService.syncClusterLabel("k8s-test");

    }

}
