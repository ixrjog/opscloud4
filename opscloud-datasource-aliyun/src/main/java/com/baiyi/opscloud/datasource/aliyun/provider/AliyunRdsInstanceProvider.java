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
import com.baiyi.opscloud.datasource.aliyun.rds.driver.AliyunRdsInstanceDriver;
import com.baiyi.opscloud.datasource.aliyun.rds.entity.AliyunRds;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_ALIYUN_RDS_INSTANCE;

/**
 * @Author baiyi
 * @Date 2021/9/29 6:09 下午
 * @Version 1.0
 */
@Component
public class AliyunRdsInstanceProvider extends BaseAssetProvider<AliyunRds.DBInstanceAttribute> {

    @Resource
    private AliyunRdsInstanceDriver aliyunRdsInstanceDriver;

    @Resource
    private AliyunRdsInstanceProvider aliyunRdsInstanceProvider;

    @Override
    @EnablePullChild(type = DsAssetTypeConstants.RDS_INSTANCE)
    @SingleTask(name = PULL_ALIYUN_RDS_INSTANCE, lockTime = "5m")
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
    protected List<AliyunRds.DBInstanceAttribute> listEntities(DsInstanceContext dsInstanceContext) {
        AliyunConfig.Aliyun aliyun = buildConfig(dsInstanceContext.getDsConfig());
        if (CollectionUtils.isEmpty(aliyun.getRegionIds())) {
            return Collections.emptyList();
        }
        List<AliyunRds.DBInstanceAttribute> entities = Lists.newArrayList();
        aliyun.getRegionIds().forEach(regionId -> {
            try {
                entities.addAll(aliyunRdsInstanceDriver.listDbInstance(regionId, aliyun));
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
        return DsAssetTypeConstants.RDS_INSTANCE.name();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(aliyunRdsInstanceProvider);
    }

}