package com.baiyi.opscloud.datasource.kubernetes.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.common.constant.enums.DsTypeEnum;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.datasource.kubernetes.convert.NamespaceAssetConvert;
import com.baiyi.opscloud.datasource.kubernetes.drive.KubernetesNamespaceDrive;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.BaseAssetProvider;
import com.baiyi.opscloud.core.util.AssetUtil;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import io.fabric8.kubernetes.api.model.Namespace;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

import static com.baiyi.opscloud.common.constant.SingleTaskConstants.PULL_KUBERNETES_NAMESPACE;

/**
 * @Author baiyi
 * @Date 2021/6/24 9:25 下午
 * @Version 1.0
 */
@Component
public class KubernetesNamespaceProvider extends BaseAssetProvider<Namespace> {

    @Resource
    private KubernetesNamespaceProvider kubernetesNamespaceProvider;

    @Override
    public String getInstanceType() {
        return DsTypeEnum.KUBERNETES.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeEnum.KUBERNETES_NAMESPACE.getType();
    }

    private KubernetesConfig.Kubernetes buildConfig(DatasourceConfig dsConfig) {
        return dsConfigHelper.build(dsConfig, KubernetesConfig.class).getKubernetes();
    }

    @Override
    protected List<Namespace> listEntities(DsInstanceContext dsInstanceContext) {
        return KubernetesNamespaceDrive.listNamespace(buildConfig(dsInstanceContext.getDsConfig()));
    }

    @Override
    @SingleTask(name = PULL_KUBERNETES_NAMESPACE)
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    @Override
    protected boolean equals(DatasourceInstanceAsset asset, DatasourceInstanceAsset preAsset) {
        if (!AssetUtil.equals(preAsset.getAssetKey(), asset.getAssetKey()))
            return false;
        if (!AssetUtil.equals(preAsset.getName(), asset.getName()))
            return false;
        if (preAsset.getIsActive() != asset.getIsActive())
            return false;
        return true;
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, Namespace entity) {
        return NamespaceAssetConvert.toAssetContainer(dsInstance, entity);
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(kubernetesNamespaceProvider);
    }
}
