package com.baiyi.opscloud.facade.kubernetes.impl;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.common.exception.common.OCException;
import com.baiyi.opscloud.core.factory.DsConfigHelper;
import com.baiyi.opscloud.datasource.kubernetes.driver.IstioDestinationRuleDriver;
import com.baiyi.opscloud.datasource.kubernetes.driver.IstioVirtualServiceDriver;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.param.kubernetes.IstioParam;
import com.baiyi.opscloud.facade.kubernetes.IstioFacade;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import io.fabric8.istio.api.networking.v1alpha3.DestinationRule;
import io.fabric8.istio.api.networking.v1alpha3.VirtualService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2023/10/8 15:55
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class IstioFacadeImpl implements IstioFacade {

    private final DsInstanceService dsInstanceService;

    private final DsConfigHelper dsConfigHelper;

    @Override
    public VirtualService getIstioVirtualService(IstioParam.GetResource getResource) {
        KubernetesConfig kubernetesConfig = getKubernetesConfig(getResource.getInstanceId());
        return IstioVirtualServiceDriver.get(kubernetesConfig.getKubernetes(), getResource.getNamespace(), getResource.getName());
    }

    @Override
    public VirtualService updateIstioVirtualService(IstioParam.UpdateResource updateResource) {
        KubernetesConfig kubernetesConfig = getKubernetesConfig(updateResource.getInstanceId());
        return IstioVirtualServiceDriver.update(kubernetesConfig.getKubernetes(), updateResource.getResourceYaml());
    }

    @Override
    public VirtualService createIstioVirtualService(IstioParam.CreateResource createResource) {
        KubernetesConfig kubernetesConfig = getKubernetesConfig(createResource.getInstanceId());
        return IstioVirtualServiceDriver.create(kubernetesConfig.getKubernetes(), createResource.getResourceYaml());
    }

    @Override
    public DestinationRule getIstioDestinationRule(IstioParam.GetResource getResource) {
        KubernetesConfig kubernetesConfig = getKubernetesConfig(getResource.getInstanceId());
        return IstioDestinationRuleDriver.get(kubernetesConfig.getKubernetes(), getResource.getNamespace(), getResource.getName());
    }

    @Override
    public DestinationRule updateIstioDestinationRule(IstioParam.UpdateResource updateResource) {
        KubernetesConfig kubernetesConfig = getKubernetesConfig(updateResource.getInstanceId());
        return IstioDestinationRuleDriver.update(kubernetesConfig.getKubernetes(), updateResource.getResourceYaml());
    }

    @Override
    public DestinationRule createIstioDestinationRule(IstioParam.CreateResource createResource) {
        KubernetesConfig kubernetesConfig = getKubernetesConfig(createResource.getInstanceId());
        return IstioDestinationRuleDriver.create(kubernetesConfig.getKubernetes(), createResource.getResourceYaml());
    }

    private KubernetesConfig getKubernetesConfig(int instanceId) {
        DatasourceInstance datasourceInstance = dsInstanceService.getById(instanceId);
        if (datasourceInstance == null) {
            throw new OCException("数据源实例不存在: instanceId={}", instanceId);
        }
        if (!DsTypeEnum.KUBERNETES.name().equals(datasourceInstance.getInstanceType())) {
            throw new OCException("数据源实例类型不正确: instanceType={}", datasourceInstance.getInstanceType());
        }
        return dsConfigHelper.buildKubernetesConfig(datasourceInstance.getUuid());
    }

}
