package com.baiyi.opscloud.datasource.aliyun.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.core.comparer.AssetComparer;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.annotation.EnablePullChild;
import com.baiyi.opscloud.core.provider.asset.BaseAssetProvider;
import com.baiyi.opscloud.datasource.aliyun.ons.driver.AliyunOnsInstanceV5Driver;
import com.baiyi.opscloud.datasource.aliyun.ons.entity.OnsInstanceV5;
import com.baiyi.opscloud.datasource.aliyun.util.AliyunRegionIdUtil;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_ALIYUN_ONS5_INSTANCE;

/**
 * @Author 修远
 * @Date 2023/9/12 12:48 AM
 * @Since 1.0
 */
@Component
public class AliyunOnsInstanceV5Provider extends BaseAssetProvider<OnsInstanceV5.InstanceInfo> {

    @Resource
    private AliyunOnsInstanceV5Driver aliyunOnsInstanceV5Driver;

    @Resource
    private AliyunOnsInstanceV5Provider aliyunOnsInstanceV5Provider;

    @Override
    @EnablePullChild(type = DsAssetTypeConstants.ONS5_INSTANCE)
    @SingleTask(name = PULL_ALIYUN_ONS5_INSTANCE, lockTime = "2m")
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
    protected List<OnsInstanceV5.InstanceInfo> listEntities(DsInstanceContext dsInstanceContext) {
        AliyunConfig.Aliyun aliyun = buildConfig(dsInstanceContext.getDsConfig());
        Set<String> regionIds = AliyunRegionIdUtil.toOnsRegionIds(aliyun);
        List<OnsInstanceV5.InstanceInfo> entities = Lists.newArrayList();
        regionIds.forEach(regionId -> {
            try {
                entities.addAll(aliyunOnsInstanceV5Driver.listInstance(regionId, aliyun));
            } catch (Exception ignored) {
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
        return DsAssetTypeConstants.ONS5_INSTANCE.name();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(aliyunOnsInstanceV5Provider);
    }

}