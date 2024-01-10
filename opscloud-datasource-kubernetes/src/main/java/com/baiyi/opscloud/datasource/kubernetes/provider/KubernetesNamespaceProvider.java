package com.baiyi.opscloud.datasource.kubernetes.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.core.comparer.AssetComparer;
import com.baiyi.opscloud.core.comparer.AssetComparerBuilder;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.AbstractAssetRelationProvider;
import com.baiyi.opscloud.datasource.kubernetes.converter.NamespaceAssetConverter;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesNamespaceDriver;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.google.common.collect.Lists;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_KUBERNETES_NAMESPACE;

/**
 * @Author baiyi
 * @Date 2022/6/30 18:22
 * @Version 1.0
 */
@Component
public class KubernetesNamespaceProvider extends AbstractAssetRelationProvider<Namespace, Deployment> {

    @Resource
    private KubernetesNamespaceProvider kubernetesNamespaceProvider;

    @Override
    public String getInstanceType() {
        return DsTypeEnum.KUBERNETES.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.KUBERNETES_NAMESPACE.name();
    }

    private KubernetesConfig.Kubernetes buildConfig(DatasourceConfig dsConfig) {
        return dsConfigManager.build(dsConfig, KubernetesConfig.class).getKubernetes();
    }

    @Override
    protected List<Namespace> listEntities(DsInstanceContext dsInstanceContext) {
        return KubernetesNamespaceDriver.list(buildConfig(dsInstanceContext.getDsConfig()));
    }

    @Override
    protected List<Namespace> listEntities(DsInstanceContext dsInstanceContext, Deployment target) {
        return Lists.newArrayList(
                KubernetesNamespaceDriver.get(buildConfig(dsInstanceContext.getDsConfig()),
                target.getMetadata().getNamespace())
        );
    }

    @Override
    @SingleTask(name = PULL_KUBERNETES_NAMESPACE)
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    @Override
    protected AssetComparer getAssetComparer() {
        return AssetComparerBuilder.newBuilder()
                .compareOfName()
                .compareOfKey()
                .compareOfActive()
                .build();
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, Namespace entity) {
        return NamespaceAssetConverter.toAssetContainer(dsInstance, entity);
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(kubernetesNamespaceProvider);
    }

    @Override
    public String getTargetAssetKey() {
        return DsAssetTypeConstants.KUBERNETES_DEPLOYMENT.name();
    }

}