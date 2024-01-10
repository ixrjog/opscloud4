package com.baiyi.opscloud.facade.kubernetes.impl;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.driver.IstioDestinationRuleDriver;
import com.baiyi.opscloud.datasource.kubernetes.driver.IstioEnvoyFilterDriver;
import com.baiyi.opscloud.datasource.kubernetes.driver.IstioVirtualServiceDriver;
import com.baiyi.opscloud.datasource.kubernetes.exception.KubernetesException;
import com.baiyi.opscloud.domain.param.kubernetes.KubernetesIstioParam;
import com.baiyi.opscloud.facade.kubernetes.KubernetesIstioFacade;
import io.fabric8.istio.api.networking.v1alpha3.DestinationRule;
import io.fabric8.istio.api.networking.v1alpha3.EnvoyFilter;
import io.fabric8.istio.api.networking.v1alpha3.VirtualService;
import io.fabric8.kubernetes.api.model.StatusDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2023/10/8 15:55
 * @Version 1.0
 */
@Slf4j
@Component
public class KubernetesIstioFacadeImpl extends BaseKubernetesConfig implements KubernetesIstioFacade {

    @Override
    public VirtualService getVirtualService(KubernetesIstioParam.GetResource getResource) {
        KubernetesConfig kubernetesConfig = getKubernetesConfig(getResource.getInstanceId());
        try {
            return IstioVirtualServiceDriver.get(kubernetesConfig.getKubernetes(), getResource);
        } catch (Exception e) {
            throw new KubernetesException(e.getMessage());
        }
    }

    @Override
    public VirtualService updateVirtualService(KubernetesIstioParam.UpdateResource updateResource) {
        KubernetesConfig kubernetesConfig = getKubernetesConfig(updateResource.getInstanceId());
        try {
            return IstioVirtualServiceDriver.update(kubernetesConfig.getKubernetes(), updateResource);
        } catch (Exception e) {
            throw new KubernetesException(e.getMessage());
        }
    }

    @Override
    public VirtualService createVirtualService(KubernetesIstioParam.CreateResource createResource) {
        KubernetesConfig kubernetesConfig = getKubernetesConfig(createResource.getInstanceId());
        try {
            return IstioVirtualServiceDriver.create(kubernetesConfig.getKubernetes(), createResource);
        } catch (Exception e) {
            throw new KubernetesException(e.getMessage());
        }
    }

    @Override
    public List<StatusDetails> deleteVirtualService(KubernetesIstioParam.DeleteResource deleteResource) {
        KubernetesConfig kubernetesConfig = getKubernetesConfig(deleteResource.getInstanceId());
        try {
            return IstioVirtualServiceDriver.delete(kubernetesConfig.getKubernetes(), deleteResource);
        } catch (Exception e) {
            throw new KubernetesException(e.getMessage());
        }
    }

    @Override
    public DestinationRule getDestinationRule(KubernetesIstioParam.GetResource getResource) {
        KubernetesConfig kubernetesConfig = getKubernetesConfig(getResource.getInstanceId());
        try {
            return IstioDestinationRuleDriver.get(kubernetesConfig.getKubernetes(), getResource);
        } catch (Exception e) {
            throw new KubernetesException(e.getMessage());
        }
    }

    @Override
    public DestinationRule updateDestinationRule(KubernetesIstioParam.UpdateResource updateResource) {
        KubernetesConfig kubernetesConfig = getKubernetesConfig(updateResource.getInstanceId());
        try {
            return IstioDestinationRuleDriver.update(kubernetesConfig.getKubernetes(), updateResource);
        } catch (Exception e) {
            throw new KubernetesException(e.getMessage());
        }
    }

    @Override
    public List<StatusDetails> deleteDestinationRule(KubernetesIstioParam.DeleteResource deleteResource) {
        KubernetesConfig kubernetesConfig = getKubernetesConfig(deleteResource.getInstanceId());
        try {
            return IstioDestinationRuleDriver.delete(kubernetesConfig.getKubernetes(), deleteResource);
        } catch (Exception e) {
            throw new KubernetesException(e.getMessage());
        }
    }

    @Override
    public DestinationRule createDestinationRule(KubernetesIstioParam.CreateResource createResource) {
        KubernetesConfig kubernetesConfig = getKubernetesConfig(createResource.getInstanceId());
        try {
            return IstioDestinationRuleDriver.create(kubernetesConfig.getKubernetes(), createResource);
        } catch (Exception e) {
            throw new KubernetesException(e.getMessage());
        }
    }

    @Override
    public EnvoyFilter getEnvoyFilter(KubernetesIstioParam.GetResource getResource) {
        KubernetesConfig kubernetesConfig = getKubernetesConfig(getResource.getInstanceId());
        try {
            return IstioEnvoyFilterDriver.get(kubernetesConfig.getKubernetes(), getResource);
        } catch (Exception e) {
            throw new KubernetesException(e.getMessage());
        }
    }

    @Override
    public EnvoyFilter updateEnvoyFilter(KubernetesIstioParam.UpdateResource updateResource) {
        KubernetesConfig kubernetesConfig = getKubernetesConfig(updateResource.getInstanceId());
        try {
            return IstioEnvoyFilterDriver.update(kubernetesConfig.getKubernetes(), updateResource);
        } catch (Exception e) {
            throw new KubernetesException(e.getMessage());
        }
    }

    @Override
    public EnvoyFilter createEnvoyFilter(KubernetesIstioParam.CreateResource createResource) {
        KubernetesConfig kubernetesConfig = getKubernetesConfig(createResource.getInstanceId());
        try {
            return IstioEnvoyFilterDriver.create(kubernetesConfig.getKubernetes(), createResource);
        } catch (Exception e) {
            throw new KubernetesException(e.getMessage());
        }
    }

    @Override
    public List<StatusDetails> deleteEnvoyFilter(KubernetesIstioParam.DeleteResource deleteResource) {
        KubernetesConfig kubernetesConfig = getKubernetesConfig(deleteResource.getInstanceId());
        try {
            return IstioEnvoyFilterDriver.delete(kubernetesConfig.getKubernetes(), deleteResource);
        } catch (Exception e) {
            throw new KubernetesException(e.getMessage());
        }
    }

}