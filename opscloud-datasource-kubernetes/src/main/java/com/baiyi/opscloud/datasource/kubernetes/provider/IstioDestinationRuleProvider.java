package com.baiyi.opscloud.datasource.kubernetes.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.core.comparer.AssetComparer;
import com.baiyi.opscloud.core.comparer.AssetComparerBuilder;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.BaseAssetProvider;
import com.baiyi.opscloud.datasource.kubernetes.converter.DestinationRuleConverter;
import com.baiyi.opscloud.datasource.kubernetes.driver.IstioDestinationRuleDriver;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesNamespaceDriver;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.google.common.collect.Lists;
import io.fabric8.istio.api.networking.v1alpha3.DestinationRule;
import io.fabric8.kubernetes.api.model.Namespace;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_ISTIO_DESTINATION_RULE;

/**
 * @Author baiyi
 * @Date 2023/10/8 13:45
 * @Version 1.0
 */
@Component
public class IstioDestinationRuleProvider extends BaseAssetProvider<DestinationRule> {

    @Resource
    private IstioDestinationRuleProvider istioDestinationRuleProvider;

    @Override
    @SingleTask(name = PULL_ISTIO_DESTINATION_RULE, lockTime = "5m")
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
                .compareOfActive()
                .build();
    }

    @Override
    protected List<DestinationRule> listEntities(DsInstanceContext dsInstanceContext) {
        KubernetesConfig.Kubernetes kubernetes = buildConfig(dsInstanceContext.getDsConfig());
        List<Namespace> namespaces = KubernetesNamespaceDriver.list(buildConfig(dsInstanceContext.getDsConfig()));
        List<DestinationRule> entities = Lists.newArrayList();
        namespaces.forEach(e ->
                entities.addAll(IstioDestinationRuleDriver.list(kubernetes, e.getMetadata().getName()))
        );
        return entities;
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.KUBERNETES.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.ISTIO_DESTINATION_RULE.name();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(istioDestinationRuleProvider);
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, DestinationRule entity) {
        return DestinationRuleConverter.toAssetContainer(dsInstance, entity);
    }

}