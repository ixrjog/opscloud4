package com.baiyi.opscloud.facade.kubernetes.impl;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesServiceDriver;
import com.baiyi.opscloud.datasource.kubernetes.exception.KubernetesException;
import com.baiyi.opscloud.domain.param.kubernetes.KubernetesServiceParam;
import com.baiyi.opscloud.facade.kubernetes.KubernetesServiceFacade;
import io.fabric8.istio.api.networking.v1alpha3.EnvoyFilter;
import io.fabric8.kubernetes.api.model.Service;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2023/12/13 11:06
 * @Version 1.0
 */
@Component
public class KubernetesServiceFacadeImpl extends BaseKubernetesConfig implements KubernetesServiceFacade {

    @Override
    public Service getService(KubernetesServiceParam.GetResource getResource) {
        KubernetesConfig kubernetesConfig = getKubernetesConfig(getResource.getInstanceId());
        try {
            return KubernetesServiceDriver.get(kubernetesConfig.getKubernetes(), getResource.getNamespace(), getResource.getName());
        } catch (Exception e) {
            throw new KubernetesException(e.getMessage());
        }
    }

    @Override
    public Service updateService(KubernetesServiceParam.UpdateResource updateResource) {
        KubernetesConfig kubernetesConfig = getKubernetesConfig(updateResource.getInstanceId());
        try {
            return KubernetesServiceDriver.update(kubernetesConfig.getKubernetes(), updateResource.getResourceYaml());
        } catch (Exception e) {
            throw new KubernetesException(e.getMessage());
        }
    }

    @Override
    public Service createService(KubernetesServiceParam.CreateResource createResource) {
        KubernetesConfig kubernetesConfig = getKubernetesConfig(createResource.getInstanceId());
        try {
            return KubernetesServiceDriver.create(kubernetesConfig.getKubernetes(), createResource.getResourceYaml());
        } catch (Exception e) {
            throw new KubernetesException(e.getMessage());
        }
    }

}