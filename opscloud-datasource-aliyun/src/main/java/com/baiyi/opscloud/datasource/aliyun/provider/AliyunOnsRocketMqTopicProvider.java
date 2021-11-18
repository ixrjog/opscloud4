package com.baiyi.opscloud.datasource.aliyun.provider;

import com.aliyuncs.ons.model.v20190214.OnsInstanceInServiceListResponse;
import com.aliyuncs.ons.model.v20190214.OnsTopicListResponse;
import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.common.constant.enums.DsTypeEnum;
import com.baiyi.opscloud.datasource.aliyun.convert.OnsRocketMqConvert;
import com.baiyi.opscloud.datasource.aliyun.ons.rocketmq.AliyunOnsRocketMqInstanceDatasource;
import com.baiyi.opscloud.datasource.aliyun.ons.rocketmq.AliyunOnsRocketMqTopicDatasource;
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

import static com.baiyi.opscloud.common.constant.SingleTaskConstants.PULL_ALIYUN_ONS_ROCKETMQ_TOPIC;

/**
 * @Author baiyi
 * @Date 2021/9/30 3:09 下午
 * @Version 1.0
 */
@Component
public class AliyunOnsRocketMqTopicProvider extends AbstractAssetRelationProvider<OnsTopicListResponse.PublishInfoDo, OnsInstanceInServiceListResponse.InstanceVO> {

    @Resource
    private AliyunOnsRocketMqInstanceDatasource aliyunOnsRocketMqInstanceDatasource;

    @Resource
    private AliyunOnsRocketMqTopicDatasource aliyunOnsRocketMqTopicDatasource;

    @Resource
    private AliyunOnsRocketMqTopicProvider aliyunOnsRocketMqTopicProvider;

    @Override
    @EnablePullChild(type = DsAssetTypeEnum.ONS_ROCKETMQ_TOPIC)
    @SingleTask(name = PULL_ALIYUN_ONS_ROCKETMQ_TOPIC, lockTime = "5m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    private AliyunConfig.Aliyun buildConfig(DatasourceConfig dsConfig) {
        return dsConfigHelper.build(dsConfig, AliyunConfig.class).getAliyun();
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, OnsTopicListResponse.PublishInfoDo entity) {
        return OnsRocketMqConvert.toAssetContainer(dsInstance, entity);
    }

    @Override
    protected boolean equals(DatasourceInstanceAsset asset, DatasourceInstanceAsset preAsset) {
        if (!AssetUtil.equals(preAsset.getName(), asset.getName()))
            return false;
        if (!AssetUtil.equals(preAsset.getAssetKey2(), asset.getAssetKey2()))
            return false;
        if (!AssetUtil.equals(preAsset.getDescription(), asset.getDescription()))
            return false;
        return true;
    }

    @Override
    protected List<OnsTopicListResponse.PublishInfoDo> listEntities(DsInstanceContext dsInstanceContext) {
        AliyunConfig.Aliyun aliyun = buildConfig(dsInstanceContext.getDsConfig());
        if (CollectionUtils.isEmpty(aliyun.getRegionIds()))
            return Collections.emptyList();
        List<OnsTopicListResponse.PublishInfoDo> entities = Lists.newArrayList();
        aliyun.getRegionIds().forEach(regionId -> {
            List<OnsInstanceInServiceListResponse.InstanceVO> instances = aliyunOnsRocketMqInstanceDatasource.listInstance(regionId, aliyun);
            if (!CollectionUtils.isEmpty(instances)) {
                instances.forEach(instance ->
                        entities.addAll(aliyunOnsRocketMqTopicDatasource.listTopic(regionId, aliyun, instance.getInstanceId())));
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
        return DsAssetTypeEnum.ONS_ROCKETMQ_TOPIC.name();
    }

    @Override
    public String getTargetAssetKey() {
        return DsAssetTypeEnum.ONS_ROCKETMQ_INSTANCE.name();
    }

    @Override
    protected List<OnsTopicListResponse.PublishInfoDo> listEntities(DsInstanceContext dsInstanceContext, OnsInstanceInServiceListResponse.InstanceVO target) {
        AliyunConfig.Aliyun aliyun = buildConfig(dsInstanceContext.getDsConfig());
        return aliyunOnsRocketMqTopicDatasource.listTopic(aliyun.getRegionId(), aliyun, target.getInstanceId());
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(aliyunOnsRocketMqTopicProvider);
    }

}

