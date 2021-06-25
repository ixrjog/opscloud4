package com.baiyi.caesar.datasource.kubernetes.provider;

import com.baiyi.caesar.common.annotation.SingleTask;
import com.baiyi.caesar.common.datasource.KubernetesDsInstanceConfig;
import com.baiyi.caesar.common.datasource.config.DsKubernetesConfig;
import com.baiyi.caesar.common.type.DsAssetTypeEnum;
import com.baiyi.caesar.common.type.DsTypeEnum;
import com.baiyi.caesar.datasource.builder.AssetContainer;
import com.baiyi.caesar.datasource.factory.AssetProviderFactory;
import com.baiyi.caesar.datasource.model.DsInstanceContext;
import com.baiyi.caesar.datasource.provider.asset.BaseAssetProvider;
import com.baiyi.caesar.datasource.util.AssetUtil;
import com.baiyi.caesar.domain.generator.caesar.DatasourceConfig;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstance;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstanceAsset;
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
        Pod pod = new Pod();
        //   return GitlabUserHandler.queryUsers(buildConfig(dsInstanceContext.getDsConfig()));
        return null;
    }

    @Override
    @SingleTask(name = "PullKubernetesPod", lockTime = 300)
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    @Override
    protected boolean equals(DatasourceInstanceAsset asset, DatasourceInstanceAsset preAsset) {
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
        // return GitlabAssetConvert.toAssetContainer(dsInstance, entry);
        return null;
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(kubernetesPodProvider);
    }
}
