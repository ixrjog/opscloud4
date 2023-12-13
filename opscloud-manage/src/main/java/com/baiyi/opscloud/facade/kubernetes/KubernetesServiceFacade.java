package com.baiyi.opscloud.facade.kubernetes;

import com.baiyi.opscloud.domain.param.kubernetes.KubernetesServiceParam;
import io.fabric8.kubernetes.api.model.Service;

/**
 * @Author baiyi
 * @Date 2023/12/13 11:05
 * @Version 1.0
 */
public interface KubernetesServiceFacade {

    Service getService(KubernetesServiceParam.GetResource getResource);

    Service updateService(KubernetesServiceParam.UpdateResource updateResource);

    Service createService(KubernetesServiceParam.CreateResource createResource);

}
