package com.baiyi.opscloud.datasource.aliyun.provider;

import com.aliyuncs.ecs.model.v20140526.DescribeImagesResponse;
import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.core.comparer.AssetComparer;
import com.baiyi.opscloud.core.comparer.AssetComparerBuilder;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.BaseAssetProvider;
import com.baiyi.opscloud.datasource.aliyun.converter.ComputeAssetConverter;
import com.baiyi.opscloud.datasource.aliyun.ecs.driver.AliyunEcsDriver;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_ALIYUN_ECS_IMAGE;

/**
 * @Author 修远
 * @Date 2021/6/23 3:42 下午
 * @Since 1.0
 */

@Component
public class AliyunEcsImageProvider extends BaseAssetProvider<DescribeImagesResponse.Image> {

    @Resource
    private AliyunEcsDriver aliyunEcsDriver;

    @Resource
    private AliyunEcsImageProvider aliyunEcsImageProvider;

    @Override
    @SingleTask(name = PULL_ALIYUN_ECS_IMAGE, lockTime = "5m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    private AliyunConfig.Aliyun buildConfig(DatasourceConfig dsConfig) {
        return dsConfigManager.build(dsConfig, AliyunConfig.class).getAliyun();
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, DescribeImagesResponse.Image entity) {
        return ComputeAssetConverter.toAssetContainer(dsInstance, entity);
    }

    @Override
    protected AssetComparer getAssetComparer() {
        return AssetComparerBuilder.newBuilder()
                .compareOfKind()
                .compareOfDescription()
                .compareOfExpiredTime()
                .build();
    }

    @Override
    protected List<DescribeImagesResponse.Image> listEntities(DsInstanceContext dsInstanceContext) {
        AliyunConfig.Aliyun aliyun = buildConfig(dsInstanceContext.getDsConfig());
        if (CollectionUtils.isEmpty(aliyun.getRegionIds())) {
            return Collections.emptyList();
        }
        List<DescribeImagesResponse.Image> imageList = Lists.newArrayList();
        aliyun.getRegionIds().forEach(regionId -> imageList.addAll(aliyunEcsDriver.listImages(regionId, aliyun)));
        return imageList;
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ALIYUN.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.ECS_IMAGE.name();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(aliyunEcsImageProvider);
    }

}