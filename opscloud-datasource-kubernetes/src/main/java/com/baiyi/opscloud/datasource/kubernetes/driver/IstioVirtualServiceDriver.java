package com.baiyi.opscloud.datasource.kubernetes.driver;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.client.istio.IstioClientBuilder;
import io.fabric8.istio.api.networking.v1alpha3.VirtualService;
import io.fabric8.istio.client.IstioClient;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @Author baiyi
 * @Date 2023/10/7 16:25
 * @Version 1.0
 */
@Slf4j
public class IstioVirtualServiceDriver {

    public static VirtualService create(KubernetesConfig.Kubernetes kubernetes, String content) {
        try (IstioClient ic = IstioClientBuilder.build(kubernetes)) {
            InputStream is = new ByteArrayInputStream(content.getBytes());
            VirtualService virtualService = ic.v1alpha3()
                    .virtualServices()
                    .load(is)
                    .item();
            return ic.v1alpha3()
                    .virtualServices()
                    .resource(virtualService)
                    .create();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

}
