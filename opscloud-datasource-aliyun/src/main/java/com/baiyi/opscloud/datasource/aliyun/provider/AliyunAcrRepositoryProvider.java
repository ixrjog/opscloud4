package com.baiyi.opscloud.datasource.aliyun.provider;

import com.aliyuncs.exceptions.ClientException;
import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.common.exception.asset.ListEntitiesException;
import com.baiyi.opscloud.core.comparer.AssetComparer;
import com.baiyi.opscloud.core.comparer.AssetComparerBuilder;
import com.baiyi.opscloud.core.exception.DatasourceProviderException;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.annotation.ChildProvider;
import com.baiyi.opscloud.core.provider.annotation.EnablePullChild;
import com.baiyi.opscloud.core.provider.asset.AbstractAssetChildProvider;
import com.baiyi.opscloud.datasource.aliyun.acr.delegate.AliyunAcrInstanceDelegate;
import com.baiyi.opscloud.datasource.aliyun.acr.delegate.AliyunAcrRepositoryDelegate;
import com.baiyi.opscloud.datasource.aliyun.acr.entity.AliyunAcr;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_ALIYUN_ACR_INSTANCE;

/**
 * @Author baiyi
 * @Date 2022/8/16 11:40
 * @Version 1.0
 */
@Slf4j
@Component
@ChildProvider(parentType = DsAssetTypeConstants.ACR_INSTANCE)
public class AliyunAcrRepositoryProvider extends AbstractAssetChildProvider<AliyunAcr.Repository> {

    @Resource
    private AliyunAcrInstanceDelegate aliyunAcrInstanceDelegate;

    @Resource
    private AliyunAcrRepositoryDelegate aliyunAcrRepositoryDelegate;

    @Resource
    private AliyunAcrRepositoryProvider aliyunAcrRepositoryProvider;

    @Override
    @EnablePullChild(type = DsAssetTypeConstants.ACR_REPOSITORY)
    @SingleTask(name = PULL_ALIYUN_ACR_INSTANCE, lockTime = "5m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    private AliyunConfig.Aliyun buildConfig(DatasourceConfig dsConfig) {
        return dsConfigManager.build(dsConfig, AliyunConfig.class).getAliyun();
    }

    @Override
    protected AssetComparer getAssetComparer() {
        return AssetComparerBuilder.newBuilder()
                .compareOfName()
                .compareOfKey2()
                .build();
    }

    @Override
    protected List<AliyunAcr.Repository> listEntities(DsInstanceContext dsInstanceContext) throws ListEntitiesException {
        AliyunConfig.Aliyun aliyun = buildConfig(dsInstanceContext.getDsConfig());
        Set<String> regionIds = aliyun.getRegionIds();
        List<AliyunAcr.Repository> entities = Lists.newArrayList();
        try {
            for (String regionId : regionIds) {
                List<AliyunAcr.Instance> instances = aliyunAcrInstanceDelegate.listInstance(regionId, aliyun);
                if (!CollectionUtils.isEmpty(instances)) {
                    for (AliyunAcr.Instance instance : instances) {
                        entities.addAll(aliyunAcrRepositoryDelegate.listRepository(regionId, aliyun, instance.getInstanceId()));
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
    protected List<AliyunAcr.Repository> listEntities(DsInstanceContext dsInstanceContext, DatasourceInstanceAsset parentAsset) throws ListEntitiesException {
        AliyunConfig.Aliyun aliyun = buildConfig(dsInstanceContext.getDsConfig());
        try {
            return aliyunAcrRepositoryDelegate.listRepository(parentAsset.getRegionId(), aliyun, parentAsset.getAssetId());
        } catch (ClientException e) {
            log.error(e.getMessage());
            throw new ListEntitiesException(e.getMessage());
        }
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ALIYUN.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.ACR_REPOSITORY.name();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(aliyunAcrRepositoryProvider);
    }

}