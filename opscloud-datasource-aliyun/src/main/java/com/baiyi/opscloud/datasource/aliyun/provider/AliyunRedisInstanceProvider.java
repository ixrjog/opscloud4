package com.baiyi.opscloud.datasource.aliyun.provider;

import com.aliyuncs.exceptions.ClientException;
import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.core.comparer.AssetComparer;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.BaseAssetProvider;
import com.baiyi.opscloud.datasource.aliyun.redis.driver.AliyunRedisInstanceDriver;
import com.baiyi.opscloud.datasource.aliyun.redis.entity.AliyunRedis;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_ALIYUN_REDIS_INSTANCE;

/**
 * @Author baiyi
 * @Date 2021/12/16 10:09 AM
 * @Version 1.0
 */
@Component
public class AliyunRedisInstanceProvider extends BaseAssetProvider<AliyunRedis.KVStoreInstance> {

    @Resource
    private AliyunRedisInstanceDriver aliyunRedisInstanceDriver;

    @Resource
    private AliyunRedisInstanceProvider aliyunRedisInstanceProvider;

    @Override
    @SingleTask(name = PULL_ALIYUN_REDIS_INSTANCE, lockTime = "2m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    private AliyunConfig.Aliyun buildConfig(DatasourceConfig dsConfig) {
        return dsConfigManager.build(dsConfig, AliyunConfig.class).getAliyun();
    }

    @Override
    protected AssetComparer getAssetComparer() {
        return AssetComparer.COMPARE_NAME;
    }

    @Override
    protected List<AliyunRedis.KVStoreInstance> listEntities(DsInstanceContext dsInstanceContext) {
        AliyunConfig.Aliyun aliyun = buildConfig(dsInstanceContext.getDsConfig());
        if (CollectionUtils.isEmpty(aliyun.getRegionIds())) {
            return Collections.emptyList();
        }
        List<AliyunRedis.KVStoreInstance> entities = Lists.newArrayList();
        aliyun.getRegionIds().forEach(regionId -> {
            try {
                entities.addAll(aliyunRedisInstanceDriver.listInstance(regionId, aliyun));
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
        return DsAssetTypeConstants.REDIS_INSTANCE.name();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(aliyunRedisInstanceProvider);
    }

}