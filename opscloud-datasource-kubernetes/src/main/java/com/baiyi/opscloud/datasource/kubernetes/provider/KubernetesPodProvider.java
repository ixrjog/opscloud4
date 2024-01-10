package com.baiyi.opscloud.datasource.kubernetes.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.core.comparer.AssetComparer;
import com.baiyi.opscloud.core.comparer.AssetComparerBuilder;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.BaseAssetProvider;
import com.baiyi.opscloud.datasource.kubernetes.converter.PodAssetConverter;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesNamespaceDriver;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesPodDriver;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.google.common.collect.Lists;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.Pod;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_KUBERNETES_POD;

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
        return DsAssetTypeConstants.KUBERNETES_POD.name();
    }

    private KubernetesConfig.Kubernetes buildConfig(DatasourceConfig dsConfig) {
        return dsConfigManager.build(dsConfig, KubernetesConfig.class).getKubernetes();
    }

    @Override
    protected List<Pod> listEntities(DsInstanceContext dsInstanceContext) {
        KubernetesConfig.Kubernetes kubernetes = buildConfig(dsInstanceContext.getDsConfig());
        List<Namespace> namespaces = KubernetesNamespaceDriver.list(buildConfig(dsInstanceContext.getDsConfig()));
        List<Pod> pods = Lists.newArrayList();
        namespaces.forEach(e ->
                pods.addAll(KubernetesPodDriver.list(kubernetes, e.getMetadata().getName()))
        );
        return pods;
    }

    @Override
    @SingleTask(name = PULL_KUBERNETES_POD, lockTime = "5m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    @Override
    protected AssetComparer getAssetComparer() {
        return AssetComparerBuilder.newBuilder()
                .compareOfAssetId()
                .compareOfName()
                .compareOfKey()
                .compareOfKey2()
                .compareOfActive()
                .build();
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, Pod entity) {
        DsInstanceContext dsInstanceContext = buildDsInstanceContext(dsInstance.getId());
        KubernetesConfig.Kubernetes kubernetes = buildConfig(dsInstanceContext.getDsConfig());
        return PodAssetConverter.toAssetContainer(dsInstance, kubernetes, entity);
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(kubernetesPodProvider);
    }

}