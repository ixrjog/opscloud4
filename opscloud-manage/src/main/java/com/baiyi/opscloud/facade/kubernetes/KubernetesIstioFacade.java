package com.baiyi.opscloud.facade.kubernetes;

import com.baiyi.opscloud.domain.param.kubernetes.KubernetesIstioParam;
import io.fabric8.istio.api.networking.v1alpha3.DestinationRule;
import io.fabric8.istio.api.networking.v1alpha3.EnvoyFilter;
import io.fabric8.istio.api.networking.v1alpha3.VirtualService;
import io.fabric8.kubernetes.api.model.StatusDetails;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2023/10/8 15:55
 * @Version 1.0
 */
public interface KubernetesIstioFacade {

    VirtualService getVirtualService(KubernetesIstioParam.GetResource getResource);

    VirtualService updateVirtualService(KubernetesIstioParam.UpdateResource updateResource);

    VirtualService createVirtualService(KubernetesIstioParam.CreateResource createResource);

    List<StatusDetails> deleteVirtualService(KubernetesIstioParam.DeleteResource deleteResource);

    DestinationRule getDestinationRule(KubernetesIstioParam.GetResource getResource);

    DestinationRule updateDestinationRule(KubernetesIstioParam.UpdateResource updateResource);

    List<StatusDetails> deleteDestinationRule(KubernetesIstioParam.DeleteResource deleteResource);

    DestinationRule createDestinationRule(KubernetesIstioParam.CreateResource createResource);

    EnvoyFilter getEnvoyFilter(KubernetesIstioParam.GetResource getResource);

    EnvoyFilter updateEnvoyFilter(KubernetesIstioParam.UpdateResource updateResource);

    EnvoyFilter createEnvoyFilter(KubernetesIstioParam.CreateResource createResource);

    List<StatusDetails> deleteEnvoyFilter(KubernetesIstioParam.DeleteResource deleteResource);

}