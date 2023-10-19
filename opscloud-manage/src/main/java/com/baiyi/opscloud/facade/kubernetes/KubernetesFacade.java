package com.baiyi.opscloud.facade.kubernetes;

import com.baiyi.opscloud.domain.param.kubernetes.KubernetesParam;
import io.fabric8.kubernetes.api.model.apps.Deployment;

/**
 * @Author baiyi
 * @Date 2023/10/18 16:44
 * @Version 1.0
 */
public interface KubernetesFacade {

    Deployment getKubernetesDeployment(KubernetesParam.GetResource getResource);

}
