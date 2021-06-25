package com.baiyi.caesar.datasource.kubernetes.provider;

import com.baiyi.caesar.common.annotation.SingleTask;
import com.baiyi.caesar.common.datasource.KubernetesDsInstanceConfig;
import com.baiyi.caesar.common.datasource.config.DsKubernetesConfig;
import com.baiyi.caesar.common.type.DsAssetTypeEnum;
import com.baiyi.caesar.common.type.DsTypeEnum;
import com.baiyi.caesar.datasource.builder.AssetContainer;
import com.baiyi.caesar.datasource.factory.AssetProviderFactory;
import com.baiyi.caesar.datasource.kubernetes.convert.PodAssetConvert;
import com.baiyi.caesar.datasource.kubernetes.handler.KubernetesNamespaceHandler;
import com.baiyi.caesar.datasource.kubernetes.handler.KubernetesPodHandler;
import com.baiyi.caesar.datasource.model.DsInstanceContext;
import com.baiyi.caesar.datasource.provider.asset.BaseAssetProvider;
import com.baiyi.caesar.datasource.util.AssetUtil;
import com.baiyi.caesar.domain.generator.caesar.DatasourceConfig;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstance;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstanceAsset;
import com.google.common.collect.Lists;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.Pod;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/24 6:47 下午
 * @Version 1.0
 */
@Component
public class KubernetesPodProvider extends BaseAssetProvider<Pod> {

    @Resource
    private KubernetesPodProvider kubernetesPodProvider;

    @Override
    public String getInstanceType() {
        return DsTypeEnum.KUBERNETES.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeEnum.KUBERNETES_POD.getType();
    }

    private DsKubernetesConfig.Kubernetes buildConfig(DatasourceConfig dsConfig) {
        return dsFactory.build(dsConfig, KubernetesDsInstanceConfig.class).getKubernetes();
    }

    @Override
    protected List<Pod> listEntries(DsInstanceContext dsInstanceContext) {
        DsKubernetesConfig.Kubernetes kubernetes = buildConfig(dsInstanceContext.getDsConfig());
        List<Namespace> namespaces = KubernetesNamespaceHandler.listNamespace(buildConfig(dsInstanceContext.getDsConfig()));
        List<Pod> pods = Lists.newArrayList();
        namespaces.forEach(e ->
                pods.addAll(KubernetesPodHandler.listPod(kubernetes, e.getMetadata().getName()))
        );
        return pods;
    }

    @Override
    @SingleTask(name = "PullKubernetesPod", lockTime = 300)
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
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, Pod entry) {
        return PodAssetConvert.toAssetContainer(dsInstance, entry);
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(kubernetesPodProvider);
    }
}
