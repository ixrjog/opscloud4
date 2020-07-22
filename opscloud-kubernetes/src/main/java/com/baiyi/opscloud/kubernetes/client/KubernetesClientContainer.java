package com.baiyi.opscloud.kubernetes.client;

import com.baiyi.opscloud.domain.generator.opscloud.OcKubernetesCluster;
import com.baiyi.opscloud.kubernetes.confg.KubernetesConfig;
import com.baiyi.opscloud.service.kubernetes.OcKubernetesClusterService;
import com.google.common.collect.Maps;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/6/28 5:27 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class KubernetesClientContainer implements InitializingBean {

    private static final int CONNECTION_TIMEOUT = 30 * 1000;
    private static final int REQUEST_TIMEOUT = 30 * 1000;
    private static Map<String, KubernetesClient> clientContainer;

    @Resource
    private OcKubernetesClusterService ocKubernetesClusterService;

    @Resource
    private KubernetesConfig kubernetesConfig;

    public KubernetesClient getClient(String clusterName) {
        if (KubernetesClientContainer.clientContainer.containsKey(clusterName))
            return KubernetesClientContainer.clientContainer.get(clusterName);
        return null;
    }

    /**
     * 重置client,用于配置修改后
     */
    public void reset() {
        if (clientContainer != null && !clientContainer.isEmpty())
            clientContainer.keySet().forEach(k -> {
                KubernetesClient kubernetesClient = clientContainer.get(k);
                kubernetesClient = null;
            });
        clientContainer = null;
        initialClient();
    }

    private KubernetesClient buildClient(OcKubernetesCluster ocKubernetesCluster) {
        System.setProperty(io.fabric8.kubernetes.client.Config.KUBERNETES_KUBECONFIG_FILE, kubernetesConfig.acqKubeconfigPath(ocKubernetesCluster.getName()));
        io.fabric8.kubernetes.client.Config config = new ConfigBuilder()
                .withMasterUrl(ocKubernetesCluster.getMasterUrl())
                .withTrustCerts(true)
                .build();
        config.setConnectionTimeout(CONNECTION_TIMEOUT);
        config.setRequestTimeout(REQUEST_TIMEOUT);
        return new DefaultKubernetesClient(config);
    }

    private void initialClient() {
        KubernetesClientContainer.clientContainer = Maps.newHashMap();
        ocKubernetesClusterService.queryAll().forEach(e -> {
            try {
                KubernetesClient client = buildClient(e);
                KubernetesClientContainer.clientContainer.put(e.getName(), client);
            } catch (Exception ignored) {
            }
        });
    }

    @Override
    public void afterPropertiesSet() {
        initialClient();
    }
}
