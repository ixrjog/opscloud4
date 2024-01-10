package com.baiyi.opscloud.datasource.aliyun.provider;

import com.aliyuncs.exceptions.ClientException;
import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.core.comparer.AssetComparer;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.annotation.EnablePullChild;
import com.baiyi.opscloud.core.provider.asset.BaseAssetProvider;
import com.baiyi.opscloud.datasource.aliyun.ons.driver.AliyunOnsRocketMqInstanceDriver;
import com.baiyi.opscloud.datasource.aliyun.ons.entity.OnsInstance;
import com.baiyi.opscloud.datasource.aliyun.util.AliyunRegionIdUtil;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_ALIYUN_ONS_ROCKETMQ_INSTANCE;

/**
 * @Author baiyi
 * @Date 2021/12/14 5:26 PM
 * @Version 1.0
 */
@Component
public class AliyunOnsRocketMqInstanceProvider extends BaseAssetProvider<OnsInstance.InstanceBaseInfo> {

    @Resource
    private AliyunOnsRocketMqInstanceDriver aliyunOnsRocketMqInstanceDriver;

    @Resource
    private AliyunOnsRocketMqInstanceProvider aliyunOnsRocketMqInstanceProvider;

    @Override
    @EnablePullChild(type = DsAssetTypeConstants.ONS_ROCKETMQ_INSTANCE)
    @SingleTask(name = PULL_ALIYUN_ONS_ROCKETMQ_INSTANCE, lockTime = "2m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    private AliyunConfig.Aliyun buildConfig(DatasourceConfig dsConfig) {
        return dsConfigManager.build(dsConfig, AliyunConfig.class).getAliyun();
    }

    @Override
    protected AssetComparer getAssetComparer() {
        return AssetComparer.COMPARE_DESCRIPTION;
    }

    @Override
    protected List<OnsInstance.InstanceBaseInfo> listEntities(DsInstanceContext dsInstanceContext) {
        AliyunConfig.Aliyun aliyun = buildConfig(dsInstanceContext.getDsConfig());
        Set<String> regionIds = AliyunRegionIdUtil.toOnsRegionIds(aliyun);
        List<OnsInstance.InstanceBaseInfo> entities = Lists.newArrayList();
        regionIds.forEach(regionId -> {
            try {
                entities.addAll(aliyunOnsRocketMqInstanceDriver.listInstance(regionId, aliyun));
            } catch (ClientException ignored) {
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
        return DsAssetTypeConstants.ONS_ROCKETMQ_INSTANCE.name();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(aliyunOnsRocketMqInstanceProvider);
    }

}