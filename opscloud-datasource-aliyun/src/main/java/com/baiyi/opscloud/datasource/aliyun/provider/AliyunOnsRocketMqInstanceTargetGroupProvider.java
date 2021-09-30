package com.baiyi.opscloud.datasource.aliyun.provider;

import com.aliyuncs.ons.model.v20190214.OnsGroupListResponse;
import com.aliyuncs.ons.model.v20190214.OnsInstanceInServiceListResponse;
import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.datasource.AliyunDsInstanceConfig;
import com.baiyi.opscloud.common.datasource.config.DsAliyunConfig;
import com.baiyi.opscloud.common.type.DsTypeEnum;
import com.baiyi.opscloud.datasource.aliyun.convert.OnsRocketMqConvert;
import com.baiyi.opscloud.datasource.aliyun.ons.rocketmq.handler.AliyunOnsRocketMqInstanceHandler;
import com.baiyi.opscloud.datasource.factory.AssetProviderFactory;
import com.baiyi.opscloud.datasource.model.DsInstanceContext;
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
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/9/30 4:45 下午
 * @Version 1.0
 */
@Component
public class AliyunOnsRocketMqInstanceTargetGroupProvider extends AbstractAssetRelationProvider<OnsInstanceInServiceListResponse.InstanceVO, OnsGroupListResponse.SubscribeInfoDo> {

    @Resource
    private AliyunOnsRocketMqInstanceHandler aliyunOnsRocketMqInstanceHandler;

    @Resource
    private AliyunOnsRocketMqInstanceTargetGroupProvider aliyunOnsRocketMqInstanceTargetGroupProvider;

    @Override
    @SingleTask(name = "pull_aliyun_ons_rocketmq_instance_target_group", lockTime = "2m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    private DsAliyunConfig.Aliyun buildConfig(DatasourceConfig dsConfig) {
        return dsConfigFactory.build(dsConfig, AliyunDsInstanceConfig.class).getAliyun();
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
        DsAliyunConfig.Aliyun aliyun = buildConfig(dsInstanceContext.getDsConfig());
        if (CollectionUtils.isEmpty(aliyun.getRegionIds()))
            return Collections.emptyList();
        List<OnsInstanceInServiceListResponse.InstanceVO> entries = Lists.newArrayList();
        aliyun.getRegionIds().forEach(regionId ->
                entries.addAll(aliyunOnsRocketMqInstanceHandler.listInstance(regionId, aliyun))
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
        AssetProviderFactory.register(aliyunOnsRocketMqInstanceTargetGroupProvider);
    }

    @Override
    protected List<OnsInstanceInServiceListResponse.InstanceVO> listEntries(DsInstanceContext dsInstanceContext, OnsGroupListResponse.SubscribeInfoDo target) {
        DsAliyunConfig.Aliyun aliyun = buildConfig(dsInstanceContext.getDsConfig());
        return aliyunOnsRocketMqInstanceHandler.listInstance(aliyun.getRegionId(), aliyun).stream().filter(e ->
                e.getInstanceId().equals(target.getInstanceId())
        ).collect(Collectors.toList());
    }

    @Override
    public String getTargetAssetKey() {
        return DsAssetTypeEnum.ONS_ROCKETMQ_GROUP.name();
    }
}


