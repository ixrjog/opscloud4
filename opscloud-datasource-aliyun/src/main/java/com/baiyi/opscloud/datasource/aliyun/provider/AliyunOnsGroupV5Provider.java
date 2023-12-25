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
import com.baiyi.opscloud.datasource.aliyun.ons.driver.AliyunOnsGroupV5Driver;
import com.baiyi.opscloud.datasource.aliyun.ons.driver.AliyunOnsInstanceV5Driver;
import com.baiyi.opscloud.datasource.aliyun.ons.entity.OnsGroupV5;
import com.baiyi.opscloud.datasource.aliyun.ons.entity.OnsInstanceV5;
import com.baiyi.opscloud.datasource.aliyun.util.AliyunRegionIdUtil;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_ALIYUN_ONS5_GROUP;

/**
 * @Author 修远
 * @Date 2023/9/12 12:49 AM
 * @Since 1.0
 */

@Component
@ChildProvider(parentType = DsAssetTypeConstants.ONS5_INSTANCE)
public class AliyunOnsGroupV5Provider extends AbstractAssetChildProvider<OnsGroupV5.Group> {

    @Resource
    private AliyunOnsInstanceV5Driver aliyunOnsInstanceV5Driver;

    @Resource
    private AliyunOnsGroupV5Driver aliyunOnsGroupV5Driver;

    @Resource
    private AliyunOnsGroupV5Provider aliyunOnsGroupV5Provider;

    @Override
    @SingleTask(name = PULL_ALIYUN_ONS5_GROUP, lockTime = "5m")
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
                .compareOfDescription()
                .build();
    }

    @Override
    protected List<OnsGroupV5.Group> listEntities(DsInstanceContext dsInstanceContext) {
        AliyunConfig.Aliyun aliyun = buildConfig(dsInstanceContext.getDsConfig());
        Set<String> regionIds = AliyunRegionIdUtil.toOnsRegionIds(aliyun);
        List<OnsGroupV5.Group> entities = Lists.newArrayList();
        regionIds.forEach(regionId -> {
            try {
                List<OnsInstanceV5.InstanceInfo> instances = aliyunOnsInstanceV5Driver.listInstance(regionId, aliyun);
                if (!CollectionUtils.isEmpty(instances)) {
                    instances.forEach(instance -> {
                        try {
                            entities.addAll(aliyunOnsGroupV5Driver.listGroup(regionId, aliyun, instance.getInstanceId()));
                        } catch (Exception ignored) {
                        }
                    });
                }
            } catch (Exception ignored) {
            }
        });
        return entities;
    }

    @Override
    protected List<OnsGroupV5.Group> listEntities(DsInstanceContext dsInstanceContext, DatasourceInstanceAsset asset) {
        AliyunConfig.Aliyun aliyun = buildConfig(dsInstanceContext.getDsConfig());
        try {
            return aliyunOnsGroupV5Driver.listGroup(asset.getRegionId(), aliyun, asset.getAssetId());
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ALIYUN.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.ONS5_GROUP.name();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(aliyunOnsGroupV5Provider);
    }

}