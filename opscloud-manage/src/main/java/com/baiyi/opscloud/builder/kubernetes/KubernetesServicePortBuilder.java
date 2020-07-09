package com.baiyi.opscloud.builder.kubernetes;

import com.baiyi.opscloud.bo.kubernetes.KubernetesServicePortBO;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcKubernetesService;
import com.baiyi.opscloud.domain.generator.opscloud.OcKubernetesServicePort;
import io.fabric8.kubernetes.api.model.ServicePort;

/**
 * @Author baiyi
 * @Date 2020/7/1 11:22 上午
 * @Version 1.0
 */
public class KubernetesServicePortBuilder {

    public static OcKubernetesServicePort build(OcKubernetesService ocKubernetesService, ServicePort servicePort) {
        KubernetesServicePortBO bo = KubernetesServicePortBO.builder()
                .serviceId(ocKubernetesService.getId())
                .name(servicePort.getName())
                .nodePort(servicePort.getNodePort())
                .port(servicePort.getPort())
                .targetPort(servicePort.getTargetPort().getIntVal())
                .protocol(servicePort.getProtocol())
                .build();
        return covert(bo);
    }

    private static OcKubernetesServicePort covert(KubernetesServicePortBO bo) {
        return BeanCopierUtils.copyProperties(bo, OcKubernetesServicePort.class);
    }
}
