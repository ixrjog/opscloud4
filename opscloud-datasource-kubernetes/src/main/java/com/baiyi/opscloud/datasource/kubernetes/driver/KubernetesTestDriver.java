package com.baiyi.opscloud.datasource.kubernetes.driver;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.client.MyKubernetesClientBuilder;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.dsl.ExecWatch;
import io.fabric8.kubernetes.client.dsl.LogWatch;

/**
 * @Author baiyi
 * @Date 2021/6/28 1:37 下午
 * @Version 1.0
 */
public class KubernetesTestDriver {

    /**
     * @param kubernetes
     * @param namespace
     * @param name       podName
     * @return
     */
    public static Pod getPod(KubernetesConfig.Kubernetes kubernetes, String namespace, String name, String cmd) {
        String containerName ="";
        Pod pod = KubernetesPodDriver.get(kubernetes, namespace, name);
        ExecWatch watch = MyKubernetesClientBuilder.build(kubernetes).pods()
                .inNamespace(namespace)
                .withName(name)
               // .inContainer(containerName) // 如果Pod中只有一个容器，不需要指定
                .writingOutput(System.out)
                .exec("sh", "-c", cmd);
        return null;

    }


    // LogWatch handle = client.load('/workspace/pod.yml').watchLog(System.out);

    public static LogWatch getPodLogWatch(KubernetesConfig.Kubernetes kubernetes, String namespace, String name) {
        return MyKubernetesClientBuilder.build(kubernetes).pods()
                .inNamespace(namespace)
                .withName(name).watchLog(System.out);
    }

}