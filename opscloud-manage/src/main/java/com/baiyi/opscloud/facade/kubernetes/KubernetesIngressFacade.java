package com.baiyi.opscloud.facade.kubernetes;


import com.baiyi.opscloud.domain.param.kubernetes.KubernetesIngressParam;
import io.fabric8.kubernetes.api.model.networking.v1.Ingress;

/**
 * @Author baiyi
 * @Date 2023/12/13 10:51
 * @Version 1.0
 */
public interface KubernetesIngressFacade {

    Ingress get(KubernetesIngressParam.GetResource getResource);

    Ingress update(KubernetesIngressParam.UpdateResource updateResource);

    Ingress create(KubernetesIngressParam.CreateResource createResource);

}