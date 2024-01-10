package com.baiyi.opscloud.datasource.aliyun.provider;

import com.aliyuncs.exceptions.ClientException;
import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.core.comparer.AssetComparer;
import com.baiyi.opscloud.core.comparer.AssetComparerBuilder;
import com.baiyi.opscloud.core.exception.DatasourceProviderException;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.annotation.EnablePullChild;
import com.baiyi.opscloud.core.provider.asset.BaseAssetProvider;
import com.baiyi.opscloud.datasource.aliyun.acr.delegate.AliyunAcrInstanceDelegate;
import com.baiyi.opscloud.datasource.aliyun.acr.entity.AliyunAcr;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_ALIYUN_ACR_INSTANCE;

/**
 * @Author baiyi
 * @Date 2022/8/15 17:59
 * @Version 1.0
 */
@Slf4j
@Component
public class AliyunAcrInstanceProvider extends BaseAssetProvider<AliyunAcr.Instance> {

    @Resource
    private AliyunAcrInstanceDelegate aliyunAcrInstanceDelegate;

    @Resource
    private AliyunAcrInstanceProvider aliyunAcrInstanceProvider;

    @Override
    @EnablePullChild(type = DsAssetTypeConstants.ACR_INSTANCE)
    @SingleTask(name = PULL_ALIYUN_ACR_INSTANCE, lockTime = "1m")
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
    protected List<AliyunAcr.Instance> listEntities(DsInstanceContext dsInstanceContext) {
        AliyunConfig.Aliyun aliyun = buildConfig(dsInstanceContext.getDsConfig());
        Set<String> regionIds = aliyun.getRegionIds();
        try {
            List<AliyunAcr.Instance> entities = Lists.newArrayList();
            for (String regionId : regionIds) {
                List<AliyunAcr.Instance> instances = aliyunAcrInstanceDelegate.listInstance(regionId, aliyun);
                entities.addAll(instances);
            }
            return entities;
        } catch (ClientException e) {
            log.error(e.getMessage());
            throw new DatasourceProviderException(e.getMessage());
        }
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ALIYUN.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.ACR_INSTANCE.name();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(aliyunAcrInstanceProvider);
    }

}