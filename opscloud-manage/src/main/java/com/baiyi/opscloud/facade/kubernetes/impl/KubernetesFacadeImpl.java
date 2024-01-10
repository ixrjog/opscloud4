package com.baiyi.opscloud.facade.kubernetes.impl;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesDeploymentDriver;
import com.baiyi.opscloud.domain.param.kubernetes.KubernetesParam;
import com.baiyi.opscloud.facade.kubernetes.KubernetesFacade;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2023/10/18 16:44
 * @Version 1.0
 */
@Component
public class KubernetesFacadeImpl extends BaseKubernetesConfig implements KubernetesFacade {

    @Override
    public Deployment getKubernetesDeployment(KubernetesParam.GetResource getResource) {
        KubernetesConfig kubernetesConfig = getKubernetesConfig(getResource.getInstanceId());
        return KubernetesDeploymentDriver.get(kubernetesConfig.getKubernetes(), getResource.getNamespace(), getResource.getName());
    }

}