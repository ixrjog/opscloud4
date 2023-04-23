package com.baiyi.opscloud.datasource.kubernetes.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.AbstractAssetRelationProvider;
import com.baiyi.opscloud.core.util.AssetUtil;
import com.baiyi.opscloud.datasource.kubernetes.converter.DeploymentAssetConverter;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesNamespaceDriver;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesDeploymentDriver;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.google.common.collect.Lists;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.List;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_KUBERNETES_DEPLOYMENT;

/**
 * @Author baiyi
 * @Date 2022/6/30 17:55
 * @Version 1.0
 */
@Component
public class KubernetesDeploymentProvider extends AbstractAssetRelationProvider<Deployment, Namespace> {

    @Resource
    private KubernetesDeploymentProvider kubernetesDeploymentProvider;

    @Override
    @SingleTask(name = PULL_KUBERNETES_DEPLOYMENT, lockTime = "5m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    private KubernetesConfig.Kubernetes buildConfig(DatasourceConfig dsConfig) {
        return dsConfigHelper.build(dsConfig, KubernetesConfig.class).getKubernetes();
    }

    @Override
    protected boolean equals(DatasourceInstanceAsset asset, DatasourceInstanceAsset preAsset) {
        if (!AssetUtil.equals(preAsset.getAssetKey(), asset.getAssetKey())) {
            return false;
        }
        if (!AssetUtil.equals(preAsset.getAssetKey2(), asset.getAssetKey2())) {
            return false;
        }
        if (!AssetUtil.equals(preAsset.getName(), asset.getName())) {
            return false;
        }
        if (!preAsset.getIsActive().equals(asset.getIsActive())) {
            return false;
        }
        return true;
    }

    @Override
    protected List<Deployment> listEntities(DsInstanceContext dsInstanceContext) {
        KubernetesConfig.Kubernetes kubernetes = buildConfig(dsInstanceContext.getDsConfig());
        List<Namespace> namespaces = KubernetesNamespaceDriver.list(buildConfig(dsInstanceContext.getDsConfig()));
        List<Deployment> entities = Lists.newArrayList();
        namespaces.forEach(e ->
                entities.addAll(KubernetesDeploymentDriver.list(kubernetes, e.getMetadata().getName()))
        );
        return entities;
    }

    @Override
    protected List<Deployment> listEntities(DsInstanceContext dsInstanceContext, Namespace target) {
        KubernetesConfig.Kubernetes kubernetes = buildConfig(dsInstanceContext.getDsConfig());
        return KubernetesDeploymentDriver.list(kubernetes, target.getMetadata().getName());
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.KUBERNETES.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.KUBERNETES_DEPLOYMENT.name();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(kubernetesDeploymentProvider);
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, Deployment entity) {
        return DeploymentAssetConverter.toAssetContainer(dsInstance, entity);
    }

    @Override
    public String getTargetAssetKey() {
        return DsAssetTypeConstants.KUBERNETES_NAMESPACE.name();
    }


}
