package com.baiyi.opscloud.datasource.kubernetes;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.common.util.BeetlUtil;
import com.baiyi.opscloud.datasource.kubernetes.base.BaseKubernetesTest;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesDeploymentDriver;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesServiceDriver;
import com.google.common.collect.ImmutableMap;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;

/**
 * @Author 修远
 * @Date 2023/8/11 2:49 PM
 * @Since 1.0
 */
@Slf4j
public class KubernetesServiceTest extends BaseKubernetesTest {

    private static final String SERVICE_TEMP = """
            ---
            apiVersion: v1
            kind: Service
            metadata:
              name: ${appName}
              namespace: ${envName}
              labels:
                env: ${envName}
                micrometer-prometheus-discovery: 'true'
            spec:
              ports:
                - name: http
                  port: 80
                  protocol: TCP
                  targetPort: 8080
                - name: http-mgmt
                  port: 8081
                  protocol: TCP
                  targetPort: 8081
              selector:
                app: ${appName}-${envName}
              sessionAffinity: None
              type: ClusterIP
            """;


    @Test
    void serviceGetTest() {
        KubernetesConfig kubernetesConfig = getConfigById(KubernetesClusterConfigs.ACK_FRANKFURT_DEV);
        Service service = KubernetesServiceDriver.get(kubernetesConfig.getKubernetes(), "dev", "sms");
        print(service);
    }


    @Test
    void serviceCreateTest() {
        final String envName = "dev";
        KubernetesConfig kubernetesConfig = getConfigById(KubernetesClusterConfigs.ACK_FRANKFURT_DEV);
        List<Deployment> deploymentList = KubernetesDeploymentDriver.list(kubernetesConfig.getKubernetes(), envName);
        deploymentList.forEach(deployment -> {
            String name = deployment.getMetadata().getName();
            if (name.endsWith("-" + envName)) {
                try {
                    String appName = name.substring(0, name.length() - envName.length() - 1);
                    serviceCreate(kubernetesConfig.getKubernetes(), appName, envName);
                } catch (Exception e) {
                    log.error(name);
                }
            }
        });
    }

    private void serviceCreate(KubernetesConfig.Kubernetes kubernetes, String appName, String envName) throws Exception {
        Service service = KubernetesServiceDriver.get(kubernetes, envName, appName);
        if (ObjectUtils.isEmpty(service)) {
            Map<String, Object> contentMap = new ImmutableMap.Builder<String, Object>()
                    .put("appName", appName)
                    .put("envName", envName)
                    .build();
            String content = BeetlUtil.renderTemplate(SERVICE_TEMP, contentMap);
            KubernetesServiceDriver.create(kubernetes, content);
        }
    }
}
