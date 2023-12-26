package com.baiyi.opscloud.facade.kubernetes;

import com.baiyi.opscloud.domain.param.kubernetes.KubernetesServiceParam;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.StatusDetails;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2023/12/13 11:05
 * @Version 1.0
 */
public interface KubernetesServiceFacade {

    Service get(KubernetesServiceParam.GetResource getResource);

    Service update(KubernetesServiceParam.UpdateResource updateResource);

    Service create(KubernetesServiceParam.CreateResource createResource);

    List<StatusDetails> delete(KubernetesServiceParam.DeleteResource deleteResource);

}