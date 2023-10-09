package com.baiyi.opscloud.facade.kubernetes;

import com.baiyi.opscloud.domain.param.kubernetes.IstioParam;
import io.fabric8.istio.api.networking.v1alpha3.DestinationRule;
import io.fabric8.istio.api.networking.v1alpha3.VirtualService;

/**
 * @Author baiyi
 * @Date 2023/10/8 15:55
 * @Version 1.0
 */
public interface IstioFacade {

    VirtualService getIstioVirtualService(IstioParam.GetResource getResource);

    VirtualService updateIstioVirtualService(IstioParam.UpdateResource updateResource);

    VirtualService createIstioVirtualService(IstioParam.CreateResource createResource);

    DestinationRule getIstioDestinationRule(IstioParam.GetResource getResource);

    DestinationRule updateIstioDestinationRule(IstioParam.UpdateResource updateResource);

    DestinationRule createIstioDestinationRule(IstioParam.CreateResource createResource);

}
