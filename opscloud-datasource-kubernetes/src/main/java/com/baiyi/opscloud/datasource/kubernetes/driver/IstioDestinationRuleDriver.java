package com.baiyi.opscloud.datasource.kubernetes.driver;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.client.istio.IstioClientBuilder;
import io.fabric8.istio.api.networking.v1alpha3.DestinationRule;
import io.fabric8.istio.api.networking.v1alpha3.DestinationRuleList;
import io.fabric8.istio.client.IstioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2023/10/7 16:34
 * @Version 1.0
 */
@Slf4j
public class IstioDestinationRuleDriver {

    public static DestinationRule create(KubernetesConfig.Kubernetes kubernetes, String content) {
        try (IstioClient ic = IstioClientBuilder.build(kubernetes)) {
            InputStream is = new ByteArrayInputStream(content.getBytes());
            DestinationRule destinationRule = ic.v1alpha3()
                    .destinationRules()
                    .load(is)
                    .item();
            return ic.v1alpha3().destinationRules().resource(destinationRule).create();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public static List<DestinationRule> list(KubernetesConfig.Kubernetes kubernetes, String namespace) {
        try (IstioClient ic = IstioClientBuilder.build(kubernetes)) {
            DestinationRuleList destinationRuleList = ic.v1alpha3()
                    .destinationRules()
                    .inNamespace(namespace)
                    .list();
            if (CollectionUtils.isEmpty(destinationRuleList.getItems())) {
                return Collections.emptyList();
            }
            return destinationRuleList.getItems();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

}
