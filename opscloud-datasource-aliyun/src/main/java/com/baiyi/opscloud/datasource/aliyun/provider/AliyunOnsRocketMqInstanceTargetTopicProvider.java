package com.baiyi.opscloud.datasource.aliyun.provider;

import com.aliyuncs.ons.model.v20190214.OnsInstanceInServiceListResponse;
import com.aliyuncs.ons.model.v20190214.OnsTopicListResponse;
import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.common.constant.enums.DsTypeEnum;
import com.baiyi.opscloud.datasource.aliyun.convert.OnsRocketMqConvert;
import com.baiyi.opscloud.datasource.aliyun.ons.rocketmq.AliyunOnsRocketMqInstanceDatasource;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
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
import java.util.stream.Collectors;

import static com.baiyi.opscloud.common.constant.SingleTaskConstants.PULL_ALIYUN_ONS_ROCKETMQ_INSTANCE_TARGET_TOPIC;

/**
 * @Author baiyi
 * @Date 2021/9/30 2:30 下午
 * @Version 1.0
 */
@Component
public class AliyunOnsRocketMqInstanceTargetTopicProvider extends AbstractAssetRelationProvider<OnsInstanceInServiceListResponse.InstanceVO, OnsTopicListResponse.PublishInfoDo> {

    @Resource
    private AliyunOnsRocketMqInstanceDatasource aliyunOnsRocketMqInstanceDatasource;

    @Resource
    private AliyunOnsRocketMqInstanceTargetTopicProvider aliyunOnsRocketMqInstanceTargetTopicProvider;

    @Override
    @SingleTask(name = PULL_ALIYUN_ONS_ROCKETMQ_INSTANCE_TARGET_TOPIC, lockTime = "2m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    private AliyunConfig.Aliyun buildConfig(DatasourceConfig dsConfig) {
        return dsConfigHelper.build(dsConfig, AliyunConfig.class).getAliyun();
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, OnsInstanceInServiceListResponse.InstanceVO entry) {
        return OnsRocketMqConvert.toAssetContainer(dsInstance, entry);
    }

    @Override
    protected boolean equals(DatasourceInstanceAsset asset, DatasourceInstanceAsset preAsset) {
        if (!AssetUtil.equals(preAsset.getName(), asset.getName()))
            return false;
        if (!AssetUtil.equals(preAsset.getKind(), asset.getKind()))
            return false;
        if (!AssetUtil.equals(preAsset.getDescription(), asset.getDescription()))
            return false;
        if (!AssetUtil.equals(preAsset.getExpiredTime(), asset.getExpiredTime()))
            return false;
        return true;
    }

    @Override
    protected List<OnsInstanceInServiceListResponse.InstanceVO> listEntries(DsInstanceContext dsInstanceContext) {
        AliyunConfig.Aliyun aliyun = buildConfig(dsInstanceContext.getDsConfig());
        if (CollectionUtils.isEmpty(aliyun.getRegionIds()))
            return Collections.emptyList();
        List<OnsInstanceInServiceListResponse.InstanceVO> entries = Lists.newArrayList();
        aliyun.getRegionIds().forEach(regionId ->
                entries.addAll(aliyunOnsRocketMqInstanceDatasource.listInstance(regionId, aliyun))
        );
        return entries;
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ALIYUN.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeEnum.ONS_ROCKETMQ_INSTANCE.name();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(aliyunOnsRocketMqInstanceTargetTopicProvider);
    }

    @Override
    protected List<OnsInstanceInServiceListResponse.InstanceVO> listEntries(DsInstanceContext dsInstanceContext, OnsTopicListResponse.PublishInfoDo target) {
        AliyunConfig.Aliyun aliyun = buildConfig(dsInstanceContext.getDsConfig());
        return aliyunOnsRocketMqInstanceDatasource.listInstance(aliyun.getRegionId(), aliyun).stream().filter(e ->
                e.getInstanceId().equals(target.getInstanceId())
        ).collect(Collectors.toList());
    }

    @Override
    public String getTargetAssetKey() {
        return DsAssetTypeEnum.ONS_ROCKETMQ_TOPIC.name();
    }
}

