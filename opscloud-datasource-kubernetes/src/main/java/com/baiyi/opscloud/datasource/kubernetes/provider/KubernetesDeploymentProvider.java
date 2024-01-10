package com.baiyi.opscloud.datasource.kubernetes.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.core.comparer.AssetComparer;
import com.baiyi.opscloud.core.comparer.AssetComparerBuilder;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.AbstractAssetRelationProvider;
import com.baiyi.opscloud.datasource.kubernetes.converter.DeploymentAssetConverter;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesDeploymentDriver;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesNamespaceDriver;
import com.baiyi.opscloud.domain.base.SimpleBusiness;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.service.tag.BusinessTagService;
import com.baiyi.opscloud.service.tag.TagService;
import com.google.common.collect.Lists;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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

    @Resource
    private BusinessTagService businessTagService;

    @Resource
    private TagService tagService;

    @Override
    @SingleTask(name = PULL_KUBERNETES_DEPLOYMENT, lockTime = "5m")
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

    /**
     * 给资产打数据源实例中的地域标签
     *
     * @param dsInstanceContext
     * @param asset
     */
    @Override
    protected void postEnterEntity( DatasourceInstanceAsset asset) {
        DatasourceInstance instance = dsInstanceService.getByUuid( asset.getInstanceUuid());
        SimpleBusiness query = SimpleBusiness.builder()
                .businessType(BusinessTypeEnum.DATASOURCE_INSTANCE.getType())
                .businessId(instance.getId())
                .build();
        List<BusinessTag> bizTags = businessTagService.queryByBusiness(query);
        if (CollectionUtils.isEmpty(bizTags)) {
            return;
        }
        for (BusinessTag bizTag : bizTags) {
            Tag tag = tagService.getById(bizTag.getTagId());
            if (tag.getTagKey().startsWith("@")) {
                BusinessTag businessTag = BusinessTag.builder()
                        .businessId(asset.getId())
                        .businessType(BusinessTypeEnum.ASSET.getType())
                        .tagId(tag.getId())
                        .build();
                if (businessTagService.countByBusinessTag(businessTag) == 0) {
                    businessTagService.add(businessTag);
                    break;
                }
            }
        }
    }

    @Override
    public String getTargetAssetKey() {
        return DsAssetTypeConstants.KUBERNETES_NAMESPACE.name();
    }

}