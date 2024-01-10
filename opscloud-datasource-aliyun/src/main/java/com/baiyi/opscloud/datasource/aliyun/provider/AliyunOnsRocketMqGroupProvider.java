package com.baiyi.opscloud.datasource.aliyun.provider;

import com.aliyuncs.exceptions.ClientException;
import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.core.comparer.AssetComparer;
import com.baiyi.opscloud.core.comparer.AssetComparerBuilder;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.annotation.ChildProvider;
import com.baiyi.opscloud.core.provider.asset.AbstractAssetChildProvider;
import com.baiyi.opscloud.datasource.aliyun.ons.driver.AliyunOnsRocketMqGroupDriver;
import com.baiyi.opscloud.datasource.aliyun.ons.driver.AliyunOnsRocketMqInstanceDriver;
import com.baiyi.opscloud.datasource.aliyun.ons.entity.OnsInstance;
import com.baiyi.opscloud.datasource.aliyun.ons.entity.OnsRocketMqGroup;
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

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_ALIYUN_ONS_ROCKETMQ_GROUP;

/**
 * @Author baiyi
 * @Date 2021/9/30 4:34 下午
 * @Version 1.0
 */
@Component
@ChildProvider(parentType = DsAssetTypeConstants.ONS_ROCKETMQ_INSTANCE)
public class AliyunOnsRocketMqGroupProvider extends AbstractAssetChildProvider<OnsRocketMqGroup.Group> {

    @Resource
    private AliyunOnsRocketMqInstanceDriver aliyunOnsRocketMqInstanceDriver;

    @Resource
    private AliyunOnsRocketMqGroupDriver aliyunOnsRocketMqGroupDriver;

    @Resource
    private AliyunOnsRocketMqGroupProvider aliyunOnsRocketMqGroupProvider;

    @Override
    @SingleTask(name = PULL_ALIYUN_ONS_ROCKETMQ_GROUP, lockTime = "5m")
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
    protected List<OnsRocketMqGroup.Group> listEntities(DsInstanceContext dsInstanceContext) {
        AliyunConfig.Aliyun aliyun = buildConfig(dsInstanceContext.getDsConfig());
        Set<String> regionIds = AliyunRegionIdUtil.toOnsRegionIds(aliyun);
        List<OnsRocketMqGroup.Group> entities = Lists.newArrayList();
        regionIds.forEach(regionId -> {
            try {
                List<OnsInstance.InstanceBaseInfo> instances = aliyunOnsRocketMqInstanceDriver.listInstance(regionId, aliyun);
                if (!CollectionUtils.isEmpty(instances)) {
                    instances.forEach(instance -> {
                        try {
                            entities.addAll(aliyunOnsRocketMqGroupDriver.listGroup(regionId, aliyun, instance.getInstanceId()));
                        } catch (ClientException ignored) {
                        }
                    });
                }
            } catch (ClientException ignored) {
            }
        });
        return entities;
    }

    @Override
    protected List<OnsRocketMqGroup.Group> listEntities(DsInstanceContext dsInstanceContext, DatasourceInstanceAsset asset) {
        AliyunConfig.Aliyun aliyun = buildConfig(dsInstanceContext.getDsConfig());
        try {
            return aliyunOnsRocketMqGroupDriver.listGroup(asset.getRegionId(), aliyun, asset.getAssetId());
        } catch (ClientException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ALIYUN.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.ONS_ROCKETMQ_GROUP.name();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(aliyunOnsRocketMqGroupProvider);
    }

}