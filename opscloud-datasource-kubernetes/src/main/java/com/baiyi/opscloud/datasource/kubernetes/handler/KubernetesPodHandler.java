package com.baiyi.opscloud.datasource.kubernetes.handler;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.core.factory.DsConfigManager;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.datasource.kubernetes.converter.PodAssetConverter;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesPodDriver;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.service.datasource.DsConfigService;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import io.fabric8.kubernetes.api.model.Pod;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2023/3/28 15:07
 * @Version 1.0
 */
@AllArgsConstructor
@Component
public class KubernetesPodHandler {

    private final DsConfigManager dsConfigManager;

    private final DsInstanceService dsInstanceService;

    private final DsConfigService dsConfigService;

    protected DsInstanceContext buildDsInstanceContext(int dsInstanceId) {
        DatasourceInstance dsInstance = dsInstanceService.getById(dsInstanceId);
        return buildDsInstanceContext(dsInstance);
    }

    protected DsInstanceContext buildDsInstanceContext(DatasourceInstance dsInstance) {
        return DsInstanceContext.builder()
                .dsInstance(dsInstance)
                .dsConfig(dsConfigService.getById(dsInstance.getConfigId()))
                .build();
    }

    public List<AssetContainer> queryAssetsByDeployment(int dsInstanceId, String namespace, String deployment) {
        DsInstanceContext dsInstanceContext = buildDsInstanceContext(dsInstanceId);
        KubernetesConfig.Kubernetes kubernetes = buildConfig(dsInstanceContext.getDsConfig());
        List<Pod> pods = KubernetesPodDriver.list(kubernetes, namespace, deployment);
        return pods.stream().map(e ->
                toAssetContainer(dsInstanceContext.getDsInstance(), e)
        ).collect(Collectors.toList());
    }

    private KubernetesConfig.Kubernetes buildConfig(DatasourceConfig dsConfig) {
        return dsConfigManager.build(dsConfig, KubernetesConfig.class).getKubernetes();
    }

    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, Pod entity) {
        DsInstanceContext dsInstanceContext = buildDsInstanceContext(dsInstance.getId());
        KubernetesConfig.Kubernetes kubernetes = buildConfig(dsInstanceContext.getDsConfig());
        return PodAssetConverter.toAssetContainer(dsInstance, kubernetes, entity);
    }

}