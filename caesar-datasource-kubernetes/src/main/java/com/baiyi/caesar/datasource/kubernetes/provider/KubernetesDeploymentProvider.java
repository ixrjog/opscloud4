package com.baiyi.caesar.datasource.kubernetes.provider;

import com.baiyi.caesar.common.annotation.SingleTask;
import com.baiyi.caesar.common.datasource.KubernetesDsInstanceConfig;
import com.baiyi.caesar.common.datasource.config.DsKubernetesConfig;
import com.baiyi.caesar.common.type.DsAssetTypeEnum;
import com.baiyi.caesar.common.type.DsTypeEnum;
import com.baiyi.caesar.datasource.builder.AssetContainer;
import com.baiyi.caesar.datasource.factory.AssetProviderFactory;
import com.baiyi.caesar.datasource.kubernetes.convert.DeploymentAssetConvert;
import com.baiyi.caesar.datasource.kubernetes.handler.KubernetesDeploymentHandler;
import com.baiyi.caesar.datasource.kubernetes.handler.KubernetesNamespaceHandler;
import com.baiyi.caesar.datasource.model.DsInstanceContext;
import com.baiyi.caesar.datasource.provider.asset.BaseAssetProvider;
import com.baiyi.caesar.datasource.util.AssetUtil;
import com.baiyi.caesar.domain.generator.caesar.DatasourceConfig;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstance;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstanceAsset;
import com.google.common.collect.Lists;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/25 4:20 下午
 * @Version 1.0
 */
@Component
public class KubernetesDeploymentProvider extends BaseAssetProvider<Deployment> {

    @Resource
    private KubernetesDeploymentProvider kubernetesDeploymentProvider;

    @Override
    public String getInstanceType() {
        return DsTypeEnum.KUBERNETES.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeEnum.KUBERNETES_DEPLOYMENT.getType();
    }

    private DsKubernetesConfig.Kubernetes buildConfig(DatasourceConfig dsConfig) {
        return dsFactory.build(dsConfig, KubernetesDsInstanceConfig.class).getKubernetes();
    }

    @Override
    protected List<Deployment> listEntries(DsInstanceContext dsInstanceContext) {
        DsKubernetesConfig.Kubernetes kubernetes = buildConfig(dsInstanceContext.getDsConfig());
        List<Namespace> namespaces = KubernetesNamespaceHandler.listNamespace(buildConfig(dsInstanceContext.getDsConfig()));
        List<Deployment> deployments = Lists.newArrayList();
        namespaces.forEach(e ->
                deployments.addAll(KubernetesDeploymentHandler.listDeployment(kubernetes, e.getMetadata().getName()))
        );
        return deployments;
    }

    @Override
    @SingleTask(name = "PullKubernetesDeployment", lockTime = 300)
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    @Override
    protected boolean equals(DatasourceInstanceAsset asset, DatasourceInstanceAsset preAsset) {
        if (!AssetUtil.equals(preAsset.getAssetKey(), asset.getAssetKey()))
            return false;
        if (!AssetUtil.equals(preAsset.getAssetKey2(), asset.getAssetKey2()))
            return false;
        if (!AssetUtil.equals(preAsset.getName(), asset.getName()))
            return false;
        if (preAsset.getIsActive() != asset.getIsActive())
            return false;
        return true;
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, Deployment entry) {
        return DeploymentAssetConvert.toAssetContainer(dsInstance, entry);
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(kubernetesDeploymentProvider);
    }
}
