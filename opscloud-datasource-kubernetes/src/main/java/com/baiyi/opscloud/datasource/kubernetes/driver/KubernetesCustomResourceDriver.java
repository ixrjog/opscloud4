package com.baiyi.opscloud.datasource.kubernetes.driver;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.client.MyKubernetesClientBuilder;
import io.fabric8.kubernetes.api.model.apiextensions.v1.CustomResourceDefinition;

import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @Author baiyi
 * @Date 2023/8/23 09:48
 * @Version 1.0
 */
@Slf4j
public class KubernetesCustomResourceDriver {

    /**
     * 创建自定义资源
     *
     * @param kubernetes
     * @param content
     */
    public static CustomResourceDefinition create(KubernetesConfig.Kubernetes kubernetes, String content) {
        try (KubernetesClient kc = MyKubernetesClientBuilder.build(kubernetes)) {
            InputStream is = new ByteArrayInputStream(content.getBytes());
            CustomResourceDefinition crd = kc.apiextensions().v1()
                    .customResourceDefinitions()
                    .load(is)
                    .create();
            return kc.apiextensions().v1().customResourceDefinitions().resource(crd).create();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

}