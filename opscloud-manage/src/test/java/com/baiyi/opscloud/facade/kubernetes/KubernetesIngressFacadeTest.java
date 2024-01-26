package com.baiyi.opscloud.facade.kubernetes;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.param.kubernetes.KubernetesIngressParam;
import io.fabric8.kubernetes.api.model.networking.v1.Ingress;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2024/1/4 19:55
 * @Version 1.0
 */
class KubernetesIngressFacadeTest extends BaseUnit {

    @Resource
    private KubernetesIngressFacade kubernetesIngressFacade;

    @Test
    void test(){
        KubernetesIngressParam.GetResource getResource = KubernetesIngressParam.GetResource.builder()
                .instanceId(38)
                .namespace("daily")
                .name("ad-daily.palmmerchant.com")
                .build();

       Ingress ingress = kubernetesIngressFacade.get(getResource);

       System.out.println(ingress);
    }


}