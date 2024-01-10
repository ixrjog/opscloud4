package com.baiyi.opscloud.datasource.aliyun.provider;

import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.core.comparer.AssetComparer;
import com.baiyi.opscloud.core.comparer.AssetComparerBuilder;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.AbstractAssetBusinessRelationProvider;
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

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_ALIYUN_ECS;

/**
 * @Author baiyi
 * @Date 2021/6/17 4:47 下午
 * @Version 1.0
 */
@Component
public class AliyunEcsProvider extends AbstractAssetBusinessRelationProvider<DescribeInstancesResponse.Instance> {

    @Resource
    private AliyunEcsDriver aliyunEcsDriver;

    @Resource
    private AliyunEcsProvider aliyunEcsProvider;

    @Override
    @SingleTask(name = PULL_ALIYUN_ECS, lockTime = "5m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    private AliyunConfig.Aliyun buildConfig(DatasourceConfig dsConfig) {
        return dsConfigManager.build(dsConfig, AliyunConfig.class).getAliyun();
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, DescribeInstancesResponse.Instance entity) {
        return ComputeAssetConverter.toAssetContainer(dsInstance, entity);
    }

    @Override
    protected AssetComparer getAssetComparer() {
        return AssetComparerBuilder.newBuilder()
                .compareOfName()
                .compareOfKey2()
                .compareOfKind()
                .compareOfDescription()
                .compareOfExpiredTime()
                .build();
    }

    @Override
    protected List<DescribeInstancesResponse.Instance> listEntities(DsInstanceContext dsInstanceContext) {
        AliyunConfig.Aliyun aliyun = buildConfig(dsInstanceContext.getDsConfig());
        if (CollectionUtils.isEmpty(aliyun.getRegionIds())) {
            return Collections.emptyList();
        }
        List<DescribeInstancesResponse.Instance> instanceList = Lists.newArrayList();
        aliyun.getRegionIds().forEach(regionId ->
                instanceList.addAll(aliyunEcsDriver.listInstances(regionId, aliyun))
        );
        return instanceList;
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ALIYUN.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.ECS.name();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(aliyunEcsProvider);
    }

}