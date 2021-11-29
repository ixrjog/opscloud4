package com.baiyi.opscloud.datasource.aliyun.provider;

import com.aliyuncs.ons.model.v20190214.OnsGroupListResponse;
import com.aliyuncs.ons.model.v20190214.OnsInstanceInServiceListResponse;
import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.common.constant.enums.DsTypeEnum;
import com.baiyi.opscloud.datasource.aliyun.convert.OnsRocketMqConvert;
import com.baiyi.opscloud.datasource.aliyun.ons.rocketmq.drive.AliyunOnsRocketMqGroupDrive;
import com.baiyi.opscloud.datasource.aliyun.ons.rocketmq.drive.AliyunOnsRocketMqInstanceDrive;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.annotation.EnablePullChild;
import com.baiyi.opscloud.core.provider.asset.AbstractAssetRelationProvider;
import com.baiyi.opscloud.core.util.AssetUtil;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

import static com.baiyi.opscloud.common.constant.SingleTaskConstants.PULL_ALIYUN_ONS_ROCKETMQ_GROUP;

/**
 * @Author baiyi
 * @Date 2021/9/30 4:34 下午
 * @Version 1.0
 */
@Component
public class AliyunOnsRocketMqGroupProvider extends AbstractAssetRelationProvider<OnsGroupListResponse.SubscribeInfoDo, OnsInstanceInServiceListResponse.InstanceVO> {

    @Resource
    private AliyunOnsRocketMqInstanceDrive aliyunOnsRocketMqInstanceDrive;

    @Resource
    private AliyunOnsRocketMqGroupDrive aliyunOnsRocketMqGroupDrive;

    @Resource
    private AliyunOnsRocketMqGroupProvider aliyunOnsRocketMqGroupProvider;

    @Override
    @EnablePullChild(type = DsAssetTypeEnum.ONS_ROCKETMQ_GROUP)
    @SingleTask(name = PULL_ALIYUN_ONS_ROCKETMQ_GROUP, lockTime = "5m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    private AliyunConfig.Aliyun buildConfig(DatasourceConfig dsConfig) {
        return dsConfigHelper.build(dsConfig, AliyunConfig.class).getAliyun();
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, OnsGroupListResponse.SubscribeInfoDo entity) {
        return OnsRocketMqConvert.toAssetContainer(dsInstance, entity);
    }

    @Override
    protected boolean equals(DatasourceInstanceAsset asset, DatasourceInstanceAsset preAsset) {
        if (!AssetUtil.equals(preAsset.getName(), asset.getName()))
            return false;
        if (!AssetUtil.equals(preAsset.getDescription(), asset.getDescription()))
            return false;
        return true;
    }

    @Override
    protected List<OnsGroupListResponse.SubscribeInfoDo> listEntities(DsInstanceContext dsInstanceContext) {
        AliyunConfig.Aliyun aliyun = buildConfig(dsInstanceContext.getDsConfig());
        if (CollectionUtils.isEmpty(aliyun.getRegionIds()))
            return Collections.emptyList();
        List<OnsGroupListResponse.SubscribeInfoDo> entities = Lists.newArrayList();
        aliyun.getRegionIds().forEach(regionId -> {
            List<OnsInstanceInServiceListResponse.InstanceVO> instances = aliyunOnsRocketMqInstanceDrive.listInstance(regionId, aliyun);
            if (!CollectionUtils.isEmpty(instances)) {
                instances.forEach(instance ->
                        entities.addAll(aliyunOnsRocketMqGroupDrive.listGroup(regionId, aliyun, instance.getInstanceId())));
            }
        });
        return entities;
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ALIYUN.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeEnum.ONS_ROCKETMQ_GROUP.name();
    }

    @Override
    public String getTargetAssetKey() {
        return DsAssetTypeEnum.ONS_ROCKETMQ_INSTANCE.name();
    }

    @Override
    protected List<OnsGroupListResponse.SubscribeInfoDo> listEntities(DsInstanceContext dsInstanceContext, OnsInstanceInServiceListResponse.InstanceVO target) {
        AliyunConfig.Aliyun aliyun = buildConfig(dsInstanceContext.getDsConfig());
        return aliyunOnsRocketMqGroupDrive.listGroup(aliyun.getRegionId(), aliyun, target.getInstanceId());
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(aliyunOnsRocketMqGroupProvider);
    }

}


