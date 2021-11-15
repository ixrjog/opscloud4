package com.baiyi.opscloud.datasource.kubernetes.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.common.constant.enums.DsTypeEnum;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.datasource.kubernetes.convert.PodAssetConvert;
import com.baiyi.opscloud.datasource.kubernetes.handler.KubernetesNamespaceHandler;
import com.baiyi.opscloud.datasource.kubernetes.handler.KubernetesPodHandler;
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
import io.fabric8.kubernetes.api.model.Pod;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    private KubernetesConfig.Kubernetes buildConfig(DatasourceConfig dsConfig) {
        return dsConfigHelper.build(dsConfig, KubernetesConfig.class).getKubernetes();
    }

    @Override
    public List<AssetContainer> queryAssets(int dsInstanceId, Map<String, String> params) {
        DsInstanceContext dsInstanceContext = buildDsInstanceContext(dsInstanceId);
        KubernetesConfig.Kubernetes kubernetes = buildConfig(dsInstanceContext.getDsConfig());
        List<Pod> pods = KubernetesPodHandler.listPod(kubernetes, params.get("namespace"), params.get("deploymentName"));
        return pods.stream().map(e->
            toAssetContainer(dsInstanceContext.getDsInstance(),e)
        ).collect(Collectors.toList());
    }

    @Override
    protected List<Pod> listEntries(DsInstanceContext dsInstanceContext) {
        KubernetesConfig.Kubernetes kubernetes = buildConfig(dsInstanceContext.getDsConfig());
        List<Namespace> namespaces = KubernetesNamespaceHandler.listNamespace(buildConfig(dsInstanceContext.getDsConfig()));
        List<Pod> pods = Lists.newArrayList();
        namespaces.forEach(e ->
                pods.addAll(KubernetesPodHandler.listPod(kubernetes, e.getMetadata().getName()))
        );
        return pods;
    }

    @Override
    @SingleTask(name = "PullKubernetesPod", lockTime = "5m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    @Override
    protected boolean equals(DatasourceInstanceAsset asset, DatasourceInstanceAsset preAsset) {
        if (!AssetUtil.equals(preAsset.getAssetId(), asset.getAssetId()))
            return false;
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
