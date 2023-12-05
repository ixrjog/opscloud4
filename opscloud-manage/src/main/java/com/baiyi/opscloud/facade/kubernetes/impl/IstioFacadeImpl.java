package com.baiyi.opscloud.facade.kubernetes.impl;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.driver.IstioDestinationRuleDriver;
import com.baiyi.opscloud.datasource.kubernetes.driver.IstioVirtualServiceDriver;
import com.baiyi.opscloud.datasource.kubernetes.exception.KubernetesException;
import com.baiyi.opscloud.domain.param.kubernetes.IstioParam;
import com.baiyi.opscloud.facade.kubernetes.IstioFacade;
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
public class IstioFacadeImpl extends BaseKubernetesConfig implements IstioFacade {

    @Override
    public VirtualService getIstioVirtualService(IstioParam.GetResource getResource) {
        KubernetesConfig kubernetesConfig = getKubernetesConfig(getResource.getInstanceId());
        try {
            return IstioVirtualServiceDriver.get(kubernetesConfig.getKubernetes(), getResource.getNamespace(), getResource.getName());
        } catch (Exception e) {
            throw new KubernetesException(e.getMessage());
        }
    }

    @Override
    public VirtualService updateIstioVirtualService(IstioParam.UpdateResource updateResource) {
        KubernetesConfig kubernetesConfig = getKubernetesConfig(updateResource.getInstanceId());
        try {
            return IstioVirtualServiceDriver.update(kubernetesConfig.getKubernetes(), updateResource.getResourceYaml());
        } catch (Exception e) {
            throw new KubernetesException(e.getMessage());
        }
    }

    @Override
    public VirtualService createIstioVirtualService(IstioParam.CreateResource createResource) {
        KubernetesConfig kubernetesConfig = getKubernetesConfig(createResource.getInstanceId());
        try {
            return IstioVirtualServiceDriver.create(kubernetesConfig.getKubernetes(), createResource.getResourceYaml());
        } catch (Exception e) {
            throw new KubernetesException(e.getMessage());
        }
    }

    @Override
    public DestinationRule getIstioDestinationRule(IstioParam.GetResource getResource) {
        KubernetesConfig kubernetesConfig = getKubernetesConfig(getResource.getInstanceId());
        try {
            return IstioDestinationRuleDriver.get(kubernetesConfig.getKubernetes(), getResource.getNamespace(), getResource.getName());
        } catch (Exception e) {
            throw new KubernetesException(e.getMessage());
        }
    }

    @Override
    public DestinationRule updateIstioDestinationRule(IstioParam.UpdateResource updateResource) {
        KubernetesConfig kubernetesConfig = getKubernetesConfig(updateResource.getInstanceId());
        try {
            return IstioDestinationRuleDriver.update(kubernetesConfig.getKubernetes(), updateResource.getResourceYaml());
        } catch (Exception e) {
            throw new KubernetesException(e.getMessage());
        }
    }

    @Override
    public DestinationRule createIstioDestinationRule(IstioParam.CreateResource createResource) {
        KubernetesConfig kubernetesConfig = getKubernetesConfig(createResource.getInstanceId());
        try {
            return IstioDestinationRuleDriver.create(kubernetesConfig.getKubernetes(), createResource.getResourceYaml());
        } catch (Exception e) {
            throw new KubernetesException(e.getMessage());
        }
    }

}
