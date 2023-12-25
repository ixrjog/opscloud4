package com.baiyi.opscloud.datasource.aliyun.provider;

import com.aliyuncs.exceptions.ClientException;
import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.core.comparer.AssetComparer;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.annotation.ChildProvider;
import com.baiyi.opscloud.core.provider.asset.AbstractAssetChildProvider;
import com.baiyi.opscloud.datasource.aliyun.rds.driver.AliyunRdsDatabaseDriver;
import com.baiyi.opscloud.datasource.aliyun.rds.entity.AliyunRds;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_ALIYUN_RDS_DATABASE;

/**
 * @Author baiyi
 * @Date 2021/9/30 9:31 上午
 * @Version 1.0
 */
@ChildProvider(parentType = DsAssetTypeConstants.RDS_INSTANCE)
@Component
public class AliyunRdsDatabaseProvider extends AbstractAssetChildProvider<AliyunRds.Database> {

    @Resource
    private AliyunRdsDatabaseDriver aliyunRdsDatabaseDriver;

    @Resource
    private AliyunRdsDatabaseProvider aliyunRdsDatabaseProvider;

    @Override
    @SingleTask(name = PULL_ALIYUN_RDS_DATABASE, lockTime = "5m")
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
    protected List<AliyunRds.Database> listEntities(DsInstanceContext dsInstanceContext, DatasourceInstanceAsset asset) {
        AliyunConfig.Aliyun aliyun = buildConfig(dsInstanceContext.getDsConfig());
        try {
            return aliyunRdsDatabaseDriver.listDatabase(asset.getRegionId(), aliyun, asset.getAssetId());
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
        return DsAssetTypeConstants.RDS_DATABASE.name();
    }


    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(aliyunRdsDatabaseProvider);
    }

}