package com.baiyi.opscloud.facade.kubernetes.impl;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesServiceDriver;
import com.baiyi.opscloud.datasource.kubernetes.exception.KubernetesException;
import com.baiyi.opscloud.domain.param.kubernetes.KubernetesServiceParam;
import com.baiyi.opscloud.facade.kubernetes.KubernetesServiceFacade;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.StatusDetails;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2023/12/13 11:06
 * @Version 1.0
 */
@Component
public class KubernetesServiceFacadeImpl extends BaseKubernetesConfig implements KubernetesServiceFacade {

    @Override
    public Service get(KubernetesServiceParam.GetResource getResource) {
        KubernetesConfig kubernetesConfig = getKubernetesConfig(getResource.getInstanceId());
        try {
            return KubernetesServiceDriver.get(kubernetesConfig.getKubernetes(), getResource);
        } catch (Exception e) {
            throw new KubernetesException(e.getMessage());
        }
    }

    @Override
    public Service update(KubernetesServiceParam.UpdateResource updateResource) {
        KubernetesConfig kubernetesConfig = getKubernetesConfig(updateResource.getInstanceId());
        try {
            return KubernetesServiceDriver.update(kubernetesConfig.getKubernetes(), updateResource);
        } catch (Exception e) {
            throw new KubernetesException(e.getMessage());
        }
    }

    @Override
    public Service create(KubernetesServiceParam.CreateResource createResource) {
        KubernetesConfig kubernetesConfig = getKubernetesConfig(createResource.getInstanceId());
        try {
            return KubernetesServiceDriver.create(kubernetesConfig.getKubernetes(), createResource);
        } catch (Exception e) {
            throw new KubernetesException(e.getMessage());
        }
    }

    @Override
    public List<StatusDetails> delete(KubernetesServiceParam.DeleteResource deleteResource) {
        KubernetesConfig kubernetesConfig = getKubernetesConfig(deleteResource.getInstanceId());
        try {
            return KubernetesServiceDriver.delete(kubernetesConfig.getKubernetes(), deleteResource);
        } catch (Exception e) {
            throw new KubernetesException(e.getMessage());
        }
    }

}