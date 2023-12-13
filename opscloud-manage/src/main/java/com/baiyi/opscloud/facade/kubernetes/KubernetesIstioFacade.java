package com.baiyi.opscloud.facade.kubernetes;

import com.baiyi.opscloud.domain.param.kubernetes.KubernetesIstioParam;
import io.fabric8.istio.api.networking.v1alpha3.DestinationRule;
import io.fabric8.istio.api.networking.v1alpha3.VirtualService;

/**
 * @Author baiyi
 * @Date 2023/10/8 15:55
 * @Version 1.0
 */
public interface KubernetesIstioFacade {

    VirtualService getIstioVirtualService(KubernetesIstioParam.GetResource getResource);

    VirtualService updateIstioVirtualService(KubernetesIstioParam.UpdateResource updateResource);

    VirtualService createIstioVirtualService(KubernetesIstioParam.CreateResource createResource);

    DestinationRule getIstioDestinationRule(KubernetesIstioParam.GetResource getResource);

    DestinationRule updateIstioDestinationRule(KubernetesIstioParam.UpdateResource updateResource);

    DestinationRule createIstioDestinationRule(KubernetesIstioParam.CreateResource createResource);

}
