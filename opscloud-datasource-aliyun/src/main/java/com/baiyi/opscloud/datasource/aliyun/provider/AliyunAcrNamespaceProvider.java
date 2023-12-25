package com.baiyi.opscloud.datasource.aliyun.provider;

import com.aliyuncs.exceptions.ClientException;
import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.core.comparer.AssetComparer;
import com.baiyi.opscloud.core.exception.DatasourceProviderException;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.annotation.ChildProvider;
import com.baiyi.opscloud.core.provider.asset.AbstractAssetChildProvider;
import com.baiyi.opscloud.datasource.aliyun.acr.delegate.AliyunAcrInstanceDelegate;
import com.baiyi.opscloud.datasource.aliyun.acr.entity.AliyunAcr;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_ALIYUN_ACR_NAMESPACE;

/**
 * @Author baiyi
 * @Date 2022/8/15 18:30
 * @Version 1.0
 */
@Slf4j
@Component
@ChildProvider(parentType = DsAssetTypeConstants.ACR_INSTANCE)
public class AliyunAcrNamespaceProvider extends AbstractAssetChildProvider<AliyunAcr.Namespace> {

    @Resource
    private AliyunAcrInstanceDelegate aliyunAcrInstanceDelegate;

    @Resource
    private AliyunAcrNamespaceProvider aliyunAcrNamespaceProvider;

    @Override
    @SingleTask(name = PULL_ALIYUN_ACR_NAMESPACE, lockTime = "1m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    private AliyunConfig.Aliyun buildConfig(DatasourceConfig dsConfig) {
        return dsConfigManager.build(dsConfig, AliyunConfig.class).getAliyun();
    }

    @Override
    protected AssetComparer getAssetComparer() {
        return AssetComparer.COMPARE_DESCRIPTION;
    }

    @Override
    protected List<AliyunAcr.Namespace> listEntities(DsInstanceContext dsInstanceContext) {
        AliyunConfig.Aliyun aliyun = buildConfig(dsInstanceContext.getDsConfig());
        Set<String> regionIds = aliyun.getRegionIds();
        try {
            List<AliyunAcr.Namespace> entities = Lists.newArrayList();
            for (String regionId : regionIds) {
                List<AliyunAcr.Instance> instances = aliyunAcrInstanceDelegate.listInstance(regionId, aliyun);
                if (!CollectionUtils.isEmpty(instances)) {
                    for (AliyunAcr.Instance instance : instances) {
                        entities.addAll(aliyunAcrInstanceDelegate.listNamespace(regionId, aliyun, instance.getInstanceId()));
                    }
                }
            }
            return entities;
        } catch (ClientException e) {
            log.error(e.getMessage());
            throw new DatasourceProviderException(e.getMessage());
        }
    }

    @Override
    protected List<AliyunAcr.Namespace> listEntities(DsInstanceContext dsInstanceContext, DatasourceInstanceAsset parentAsset) {
        AliyunConfig.Aliyun aliyun = buildConfig(dsInstanceContext.getDsConfig());
        try {
            return aliyunAcrInstanceDelegate.listNamespace(parentAsset.getRegionId(), aliyun, parentAsset.getAssetId());
        } catch (ClientException e) {
            log.error(e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ALIYUN.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.ACR_NAMESPACE.name();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(aliyunAcrNamespaceProvider);
    }

}