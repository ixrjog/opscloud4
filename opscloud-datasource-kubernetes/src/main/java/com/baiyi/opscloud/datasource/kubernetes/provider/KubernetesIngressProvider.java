package com.baiyi.opscloud.datasource.kubernetes.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.core.comparer.AssetComparer;
import com.baiyi.opscloud.core.comparer.AssetComparerBuilder;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.AbstractAssetRelationProvider;
import com.baiyi.opscloud.datasource.kubernetes.converter.IngressAssetConverter;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesIngressDriver;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesNamespaceDriver;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.google.common.collect.Lists;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.networking.v1.Ingress;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_KUBERNETES_INGRESS;

/**
 * @Author baiyi
 * @Date 2023/7/6 14:13
 * @Version 1.0
 */
@Component
public class KubernetesIngressProvider extends AbstractAssetRelationProvider<Ingress, Namespace> {

    @Resource
    private KubernetesIngressProvider kubernetesIngressProvider;

    @Override
    @SingleTask(name = PULL_KUBERNETES_INGRESS , lockTime = "5m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    private KubernetesConfig.Kubernetes buildConfig(DatasourceConfig dsConfig) {
        return dsConfigManager.build(dsConfig, KubernetesConfig.class).getKubernetes();
    }

    @Override
    protected AssetComparer getAssetComparer() {
        return AssetComparerBuilder.newBuilder()
                .compareOfName()
                .compareOfKey()
                .compareOfKey2()
                .build();
    }

    @Override
    protected List<Ingress> listEntities(DsInstanceContext dsInstanceContext) {
        KubernetesConfig.Kubernetes kubernetes = buildConfig(dsInstanceContext.getDsConfig());
        List<Namespace> namespaces = KubernetesNamespaceDriver.list(buildConfig(dsInstanceContext.getDsConfig()));
        List<Ingress> entities = Lists.newArrayList();
        namespaces.forEach(e ->
                entities.addAll(KubernetesIngressDriver.list(kubernetes, e.getMetadata().getName()))
        );
        return entities;
    }

    @Override
    protected List<Ingress> listEntities(DsInstanceContext dsInstanceContext, Namespace target) {
        KubernetesConfig.Kubernetes kubernetes = buildConfig(dsInstanceContext.getDsConfig());
        return KubernetesIngressDriver.list(kubernetes, target.getMetadata().getName());
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.KUBERNETES.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.KUBERNETES_INGRESS.name();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(kubernetesIngressProvider);
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, Ingress entity) {
        return IngressAssetConverter.toAssetContainer(dsInstance, entity);
    }

    @Override
    public String getTargetAssetKey() {
        return DsAssetTypeConstants.KUBERNETES_NAMESPACE.name();
    }

}