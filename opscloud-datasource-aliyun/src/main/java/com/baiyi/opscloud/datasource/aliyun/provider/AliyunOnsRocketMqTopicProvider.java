package com.baiyi.opscloud.datasource.aliyun.provider;

import com.aliyuncs.ons.model.v20190214.OnsInstanceInServiceListResponse;
import com.aliyuncs.ons.model.v20190214.OnsTopicListResponse;
import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.datasource.AliyunDsInstanceConfig;
import com.baiyi.opscloud.common.datasource.config.DsAliyunConfig;
import com.baiyi.opscloud.common.type.DsTypeEnum;
import com.baiyi.opscloud.datasource.aliyun.convert.OnsRocketMqConvert;
import com.baiyi.opscloud.datasource.aliyun.ons.rocketmq.handler.AliyunOnsRocketMqInstanceHandler;
import com.baiyi.opscloud.datasource.aliyun.ons.rocketmq.handler.AliyunOnsRocketMqTopicHandler;
import com.baiyi.opscloud.datasource.factory.AssetProviderFactory;
import com.baiyi.opscloud.datasource.model.DsInstanceContext;
import com.baiyi.opscloud.datasource.provider.annotation.EnablePullChild;
import com.baiyi.opscloud.datasource.provider.asset.AbstractAssetRelationProvider;
import com.baiyi.opscloud.datasource.util.AssetUtil;
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

/**
 * @Author baiyi
 * @Date 2021/9/30 3:09 下午
 * @Version 1.0
 */
@Component
public class AliyunOnsRocketMqTopicProvider extends AbstractAssetRelationProvider<OnsTopicListResponse.PublishInfoDo, OnsInstanceInServiceListResponse.InstanceVO> {

    @Resource
    private AliyunOnsRocketMqInstanceHandler aliyunOnsRocketMqInstanceHandler;

    @Resource
    private AliyunOnsRocketMqTopicHandler aliyunOnsRocketMqTopicHandler;

    @Resource
    private AliyunOnsRocketMqTopicProvider aliyunOnsRocketMqTopicProvider;

    @Override
    @EnablePullChild(type = DsAssetTypeEnum.ONS_ROCKETMQ_TOPIC)
    @SingleTask(name = "pull_aliyun_ons_rocketmq_topic", lockTime = "5m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    private DsAliyunConfig.Aliyun buildConfig(DatasourceConfig dsConfig) {
        return dsConfigFactory.build(dsConfig, AliyunDsInstanceConfig.class).getAliyun();
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, OnsTopicListResponse.PublishInfoDo entry) {
        return OnsRocketMqConvert.toAssetContainer(dsInstance, entry);
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
    protected List<OnsTopicListResponse.PublishInfoDo> listEntries(DsInstanceContext dsInstanceContext) {
        DsAliyunConfig.Aliyun aliyun = buildConfig(dsInstanceContext.getDsConfig());
        if (CollectionUtils.isEmpty(aliyun.getRegionIds()))
            return Collections.emptyList();
        List<OnsTopicListResponse.PublishInfoDo> entries = Lists.newArrayList();
        aliyun.getRegionIds().forEach(regionId -> {
            List<OnsInstanceInServiceListResponse.InstanceVO> instances = aliyunOnsRocketMqInstanceHandler.listInstance(regionId, aliyun);
            if (!CollectionUtils.isEmpty(instances)) {
                instances.forEach(instance ->
                        entries.addAll(aliyunOnsRocketMqTopicHandler.listTopic(regionId, aliyun, instance.getInstanceId())));
            }
        });
        return entries;
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
    protected List<OnsTopicListResponse.PublishInfoDo> listEntries(DsInstanceContext dsInstanceContext, OnsInstanceInServiceListResponse.InstanceVO target) {
        DsAliyunConfig.Aliyun aliyun = buildConfig(dsInstanceContext.getDsConfig());
        return aliyunOnsRocketMqTopicHandler.listTopic(aliyun.getRegionId(), aliyun, target.getInstanceId());
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(aliyunOnsRocketMqTopicProvider);
    }

}

