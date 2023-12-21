package com.baiyi.opscloud.facade.kubernetes.impl;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.driver.IstioDestinationRuleDriver;
import com.baiyi.opscloud.datasource.kubernetes.driver.IstioVirtualServiceDriver;
import com.baiyi.opscloud.datasource.kubernetes.exception.KubernetesException;
import com.baiyi.opscloud.domain.param.kubernetes.KubernetesIstioParam;
import com.baiyi.opscloud.facade.kubernetes.KubernetesIstioFacade;
import io.fabric8.istio.api.networking.v1alpha3.DestinationRule;
import io.fabric8.istio.api.networking.v1alpha3.VirtualService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2023/10/8 15:55
 * @Version 1.0
 */
@Slf4j
@Component
public class KubernetesIstioFacadeImpl extends BaseKubernetesConfig implements KubernetesIstioFacade {

    @Override
    public VirtualService getIstioVirtualService(KubernetesIstioParam.GetResource getResource) {
        KubernetesConfig kubernetesConfig = getKubernetesConfig(getResource.getInstanceId());
        try {
            return IstioVirtualServiceDriver.get(kubernetesConfig.getKubernetes(), getResource.getNamespace(), getResource.getName());
        } catch (Exception e) {
            throw new KubernetesException(e.getMessage());
        }
    }

    @Override
    public VirtualService updateIstioVirtualService(KubernetesIstioParam.UpdateResource updateResource) {
        KubernetesConfig kubernetesConfig = getKubernetesConfig(updateResource.getInstanceId());
        try {
            return IstioVirtualServiceDriver.update(kubernetesConfig.getKubernetes(), updateResource.getResourceYaml());
        } catch (Exception e) {
            throw new KubernetesException(e.getMessage());
        }
    }

    @Override
    public VirtualService createIstioVirtualService(KubernetesIstioParam.CreateResource createResource) {
        KubernetesConfig kubernetesConfig = getKubernetesConfig(createResource.getInstanceId());
        try {
            return IstioVirtualServiceDriver.create(kubernetesConfig.getKubernetes(), createResource.getResourceYaml());
        } catch (Exception e) {
            throw new KubernetesException(e.getMessage());
        }
    }

    @Override
    public DestinationRule getIstioDestinationRule(KubernetesIstioParam.GetResource getResource) {
        KubernetesConfig kubernetesConfig = getKubernetesConfig(getResource.getInstanceId());
        try {
            return IstioDestinationRuleDriver.get(kubernetesConfig.getKubernetes(), getResource.getNamespace(), getResource.getName());
        } catch (Exception e) {
            throw new KubernetesException(e.getMessage());
        }
    }

    @Override
    public DestinationRule updateIstioDestinationRule(KubernetesIstioParam.UpdateResource updateResource) {
        KubernetesConfig kubernetesConfig = getKubernetesConfig(updateResource.getInstanceId());
        try {
            return IstioDestinationRuleDriver.update(kubernetesConfig.getKubernetes(), updateResource.getResourceYaml());
        } catch (Exception e) {
            throw new KubernetesException(e.getMessage());
        }
    }

    @Override
    public DestinationRule createIstioDestinationRule(KubernetesIstioParam.CreateResource createResource) {
        KubernetesConfig kubernetesConfig = getKubernetesConfig(createResource.getInstanceId());
        try {
            return IstioDestinationRuleDriver.create(kubernetesConfig.getKubernetes(), createResource.getResourceYaml());
        } catch (Exception e) {
            throw new KubernetesException(e.getMessage());
        }
    }

}