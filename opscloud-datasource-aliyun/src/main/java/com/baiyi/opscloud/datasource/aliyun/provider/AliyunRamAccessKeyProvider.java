package com.baiyi.opscloud.datasource.aliyun.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.core.comparer.AssetComparer;
import com.baiyi.opscloud.core.comparer.AssetComparerBuilder;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.annotation.ChildProvider;
import com.baiyi.opscloud.core.provider.asset.AbstractAssetChildProvider;
import com.baiyi.opscloud.datasource.aliyun.ram.driver.AliyunRamAccessKeyDriver;
import com.baiyi.opscloud.datasource.aliyun.ram.entity.AccessKey;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import jakarta.annotation.Resource;
import java.util.Collections;
import java.util.List;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_ALIYUN_RAM_ACCESS_KEY;

/**
 * @Author 修远
 * @Date 2021/7/8 2:46 下午
 * @Since 1.0
 */
@Component
@ChildProvider(parentType = DsAssetTypeConstants.RAM_USER)
public class AliyunRamAccessKeyProvider extends AbstractAssetChildProvider<AccessKey.Key> {

    @Resource
    private AliyunRamAccessKeyDriver aliyunRamAccessKeyDriver;

    @Resource
    private AliyunRamAccessKeyProvider aliyunRamAccessKeyProvider;

    @Override
    @SingleTask(name = PULL_ALIYUN_RAM_ACCESS_KEY)
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    private AliyunConfig.Aliyun buildConfig(DatasourceConfig dsConfig) {
        return dsConfigManager.build(dsConfig, AliyunConfig.class).getAliyun();
    }

    @Override
    protected AssetComparer getAssetComparer() {
        return AssetComparerBuilder.newBuilder()
                .compareOfActive()
                .build();
    }

    @Override
    protected List<AccessKey.Key> listEntities(DsInstanceContext dsInstanceContext, DatasourceInstanceAsset asset) {
        AliyunConfig.Aliyun aliyun = buildConfig(dsInstanceContext.getDsConfig());
        if (CollectionUtils.isEmpty(aliyun.getRegionIds())) {
            return Collections.emptyList();
        }
        List<AccessKey.Key> entities = Lists.newArrayList();
        aliyun.getRegionIds().forEach(regionId -> entities.addAll(aliyunRamAccessKeyDriver.listAccessKeys(regionId, aliyun, asset.getAssetKey())));
        return entities;
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ALIYUN.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.RAM_ACCESS_KEY.name();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(aliyunRamAccessKeyProvider);
    }

}