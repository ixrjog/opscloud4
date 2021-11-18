package com.baiyi.opscloud.datasource.kubernetes.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.common.constant.enums.DsTypeEnum;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.datasource.kubernetes.convert.DeploymentAssetConvert;
import com.baiyi.opscloud.datasource.kubernetes.datasource.KubernetesDeploymentDatasource;
import com.baiyi.opscloud.datasource.kubernetes.datasource.KubernetesNamespaceDatasource;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.BaseAssetProvider;
import com.baiyi.opscloud.core.util.AssetUtil;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.google.common.collect.Lists;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

import static com.baiyi.opscloud.common.constant.SingleTaskConstants.PULL_KUBERNETES_DEPLOYMENT;

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

    private KubernetesConfig.Kubernetes buildConfig(DatasourceConfig dsConfig) {
        return dsConfigHelper.build(dsConfig, KubernetesConfig.class).getKubernetes();
    }

    @Override
    protected List<Deployment> listEntities(DsInstanceContext dsInstanceContext) {
        KubernetesConfig.Kubernetes kubernetes = buildConfig(dsInstanceContext.getDsConfig());
        List<Namespace> namespaces = KubernetesNamespaceDatasource.listNamespace(buildConfig(dsInstanceContext.getDsConfig()));
        List<Deployment> deployments = Lists.newArrayList();
        namespaces.forEach(e ->
                deployments.addAll(KubernetesDeploymentDatasource.listDeployment(kubernetes, e.getMetadata().getName()))
        );
        return deployments;
    }

    @Override
    @SingleTask(name = PULL_KUBERNETES_DEPLOYMENT , lockTime = "5m")
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
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, Deployment entity) {
        return DeploymentAssetConvert.toAssetContainer(dsInstance, entity);
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(kubernetesDeploymentProvider);
    }
}
