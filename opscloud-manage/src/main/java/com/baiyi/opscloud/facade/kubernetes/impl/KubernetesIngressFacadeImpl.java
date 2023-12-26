package com.baiyi.opscloud.facade.kubernetes.impl;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesIngressDriver;
import com.baiyi.opscloud.datasource.kubernetes.exception.KubernetesException;
import com.baiyi.opscloud.domain.param.kubernetes.KubernetesIngressParam;
import com.baiyi.opscloud.facade.kubernetes.KubernetesIngressFacade;
import io.fabric8.kubernetes.api.model.networking.v1.Ingress;
import org.springframework.stereotype.Component;


/**
 * @Author baiyi
 * @Date 2023/12/13 10:51
 * @Version 1.0
 */
@Component
public class KubernetesIngressFacadeImpl extends BaseKubernetesConfig implements KubernetesIngressFacade {

    @Override
    public Ingress get(KubernetesIngressParam.GetResource getResource) {
        KubernetesConfig kubernetesConfig = getKubernetesConfig(getResource.getInstanceId());
        try {
            return KubernetesIngressDriver.get(kubernetesConfig.getKubernetes(), getResource);
        } catch (Exception e) {
            throw new KubernetesException(e.getMessage());
        }
    }

    @Override
    public Ingress update(KubernetesIngressParam.UpdateResource updateResource) {
        KubernetesConfig kubernetesConfig = getKubernetesConfig(updateResource.getInstanceId());
        try {
            return KubernetesIngressDriver.update(kubernetesConfig.getKubernetes(), updateResource);
        } catch (Exception e) {
            throw new KubernetesException(e.getMessage());
        }
    }

    @Override
    public Ingress create(KubernetesIngressParam.CreateResource createResource) {
        KubernetesConfig kubernetesConfig = getKubernetesConfig(createResource.getInstanceId());
        try {
            return KubernetesIngressDriver.create(kubernetesConfig.getKubernetes(), createResource);
        } catch (Exception e) {
            throw new KubernetesException(e.getMessage());
        }
    }

}