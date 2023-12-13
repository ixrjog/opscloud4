package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.param.kubernetes.KubernetesIstioParam;
import io.fabric8.istio.api.networking.v1alpha3.VirtualService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2023/10/8 17:54
 * @Version 1.0
 */
class KubernetesControllerTest extends BaseUnit {

    @Resource
    private KubernetesController kubernetesController;

    @Test
    void ddd() {
        KubernetesIstioParam.GetResource getResource = KubernetesIstioParam.GetResource.builder()
                .instanceId(37)
                .namespace("dev")
                .name("basic-data-dev")
                .build();
        HttpResult<VirtualService> httpResult = kubernetesController.getIstioVirtualService(getResource);
        print(httpResult);
    }

}