package com.baiyi.opscloud.datasource.aliyun.provider;

import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.core.comparer.AssetComparer;
import com.baiyi.opscloud.core.comparer.AssetComparerBuilder;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.annotation.EnablePullChild;
import com.baiyi.opscloud.core.provider.asset.AbstractAssetRelationProvider;
import com.baiyi.opscloud.datasource.aliyun.converter.ComputeAssetConverter;
import com.baiyi.opscloud.datasource.aliyun.ram.driver.AliyunRamUserDriver;
import com.baiyi.opscloud.datasource.aliyun.ram.entity.RamPolicy;
import com.baiyi.opscloud.datasource.aliyun.ram.entity.RamUser;
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

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_ALIYUN_RAM_USER;

/**
 * @Author 修远
 * @Date 2021/7/2 7:46 下午
 * @Since 1.0
 */
@Component
public class AliyunRamUserProvider extends AbstractAssetRelationProvider<RamUser.User, RamPolicy.Policy> {

    @Resource
    private AliyunRamUserDriver aliyunRamUserDriver;

    @Resource
    private AliyunRamUserProvider aliyunRamUserProvider;

    @Override
    @EnablePullChild(type = DsAssetTypeConstants.RAM_USER)
    @SingleTask(name = PULL_ALIYUN_RAM_USER, lockTime = "5m")
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
                .compareOfDescription()
                .build();
    }

    @Override
    protected List<RamUser.User> listEntities(DsInstanceContext dsInstanceContext) {
        AliyunConfig.Aliyun aliyun = buildConfig(dsInstanceContext.getDsConfig());
        if (CollectionUtils.isEmpty(aliyun.getRegionIds())) {
            return Collections.emptyList();
        }
        List<RamUser.User> entities = Lists.newArrayList();
        aliyun.getRegionIds().forEach(regionId -> entities.addAll(aliyunRamUserDriver.listUsers(regionId, aliyun)));
        return entities;
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ALIYUN.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.RAM_USER.name();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(aliyunRamUserProvider);
    }

    @Override
    protected List<RamUser.User> listEntities(DsInstanceContext dsInstanceContext, RamPolicy.Policy target) {
        AliyunConfig.Aliyun aliyun = buildConfig(dsInstanceContext.getDsConfig());
        return aliyunRamUserDriver.listUsersForPolicy(aliyun.getRegionId(), aliyun, target.getPolicyType(), target.getPolicyName());
    }

    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, DescribeInstancesResponse.Instance entity) {
        return ComputeAssetConverter.toAssetContainer(dsInstance, entity);
    }

    @Override
    public String getTargetAssetKey() {
        return DsAssetTypeConstants.RAM_POLICY.name();
    }

}