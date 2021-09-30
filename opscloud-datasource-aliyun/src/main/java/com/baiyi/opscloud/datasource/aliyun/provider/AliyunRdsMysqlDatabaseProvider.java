package com.baiyi.opscloud.datasource.aliyun.provider;

import com.aliyuncs.rds.model.v20140815.DescribeDBInstancesResponse;
import com.aliyuncs.rds.model.v20140815.DescribeDatabasesResponse;
import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.datasource.AliyunDsInstanceConfig;
import com.baiyi.opscloud.common.datasource.config.DsAliyunConfig;
import com.baiyi.opscloud.common.type.DsTypeEnum;
import com.baiyi.opscloud.datasource.aliyun.convert.RdsMysqlAssetConvert;
import com.baiyi.opscloud.datasource.aliyun.rds.mysql.handler.AliyunRdsMysqlHandler;
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
 * @Date 2021/9/30 9:31 上午
 * @Version 1.0
 */
@Component
public class AliyunRdsMysqlDatabaseProvider extends AbstractAssetRelationProvider<DescribeDatabasesResponse.Database, DescribeDBInstancesResponse.DBInstance> {

    @Resource
    private AliyunRdsMysqlHandler aliyunRdsMysqlHandler;

    @Resource
    private AliyunRdsMysqlDatabaseProvider aliyunRdsMysqlDatabaseProvider;

    @Override
    @EnablePullChild(type = DsAssetTypeEnum.RDS_MYSQL_DATABASE)
    @SingleTask(name = "pull_aliyun_rds_mysql_database", lockTime = "5m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    private DsAliyunConfig.Aliyun buildConfig(DatasourceConfig dsConfig) {
        return dsConfigFactory.build(dsConfig, AliyunDsInstanceConfig.class).getAliyun();
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, DescribeDatabasesResponse.Database entry) {
        return RdsMysqlAssetConvert.toAssetContainer(dsInstance, entry);
    }

    @Override
    protected boolean equals(DatasourceInstanceAsset asset, DatasourceInstanceAsset preAsset) {
        if (!AssetUtil.equals(preAsset.getName(), asset.getName()))
            return false;
        return true;
    }

    @Override
    protected List<DescribeDatabasesResponse.Database> listEntries(DsInstanceContext dsInstanceContext) {
        DsAliyunConfig.Aliyun aliyun = buildConfig(dsInstanceContext.getDsConfig());
        if (CollectionUtils.isEmpty(aliyun.getRegionIds()))
            return Collections.emptyList();
        List<DescribeDatabasesResponse.Database> entries = Lists.newArrayList();
        aliyun.getRegionIds().forEach(regionId -> {
            List<DescribeDBInstancesResponse.DBInstance> instances = aliyunRdsMysqlHandler.listDbInstance(regionId, aliyun);
            if (!CollectionUtils.isEmpty(instances)) {
                instances.forEach(instance -> {
                    entries.addAll(aliyunRdsMysqlHandler.listDatabase(regionId, aliyun, instance.getDBInstanceId()));
                });
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
        return DsAssetTypeEnum.RDS_MYSQL_DATABASE.name();
    }

    @Override
    public String getTargetAssetKey() {
        return DsAssetTypeEnum.RDS_MYSQL_INSTANCE.name();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(aliyunRdsMysqlDatabaseProvider);
    }

    @Override
    protected List<DescribeDatabasesResponse.Database> listEntries(DsInstanceContext dsInstanceContext, DescribeDBInstancesResponse.DBInstance target) {
        DsAliyunConfig.Aliyun aliyun = buildConfig(dsInstanceContext.getDsConfig());
        return aliyunRdsMysqlHandler.listDatabase(aliyun.getRegionId(), aliyun, target.getDBInstanceId());
    }

}
