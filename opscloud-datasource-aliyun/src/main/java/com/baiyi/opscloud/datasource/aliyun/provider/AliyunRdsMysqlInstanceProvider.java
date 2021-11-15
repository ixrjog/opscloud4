package com.baiyi.opscloud.datasource.aliyun.provider;

import com.aliyuncs.rds.model.v20140815.DescribeDBInstancesResponse;
import com.aliyuncs.rds.model.v20140815.DescribeDatabasesResponse;
import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.datasource.AliyunDsInstanceConfig;
import com.baiyi.opscloud.common.constant.enums.DsTypeEnum;
import com.baiyi.opscloud.datasource.aliyun.convert.RdsMysqlAssetConvert;
import com.baiyi.opscloud.datasource.aliyun.rds.mysql.handler.AliyunRdsMysqlHandler;
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

/**
 * @Author baiyi
 * @Date 2021/9/29 6:09 下午
 * @Version 1.0
 */
@Component
public class AliyunRdsMysqlInstanceProvider extends AbstractAssetRelationProvider<DescribeDBInstancesResponse.DBInstance, DescribeDatabasesResponse.Database> {

    @Resource
    private AliyunRdsMysqlHandler aliyunRdsMysqlHandler;

    @Resource
    private AliyunRdsMysqlInstanceProvider aliyunRdsMysqlInstanceProvider;

    @Override
    @EnablePullChild(type = DsAssetTypeEnum.RDS_MYSQL_INSTANCE)
    @SingleTask(name = "pull_aliyun_rds_mysql_instance", lockTime = "5m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    private AliyunDsInstanceConfig.Aliyun buildConfig(DatasourceConfig dsConfig) {
        return dsConfigHelper.build(dsConfig, AliyunDsInstanceConfig.class).getAliyun();
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, DescribeDBInstancesResponse.DBInstance entry) {
        return RdsMysqlAssetConvert.toAssetContainer(dsInstance, entry);
    }

    @Override
    protected boolean equals(DatasourceInstanceAsset asset, DatasourceInstanceAsset preAsset) {
        if (!AssetUtil.equals(preAsset.getName(), asset.getName()))
            return false;
        return true;
    }

    @Override
    protected List<DescribeDBInstancesResponse.DBInstance> listEntries(DsInstanceContext dsInstanceContext) {
        AliyunDsInstanceConfig.Aliyun aliyun = buildConfig(dsInstanceContext.getDsConfig());
        if (CollectionUtils.isEmpty(aliyun.getRegionIds()))
            return Collections.emptyList();
        List<DescribeDBInstancesResponse.DBInstance> entries = Lists.newArrayList();
        aliyun.getRegionIds().forEach(regionId -> entries.addAll(aliyunRdsMysqlHandler.listDbInstance(regionId, aliyun)));
        return entries;
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ALIYUN.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeEnum.RDS_MYSQL_INSTANCE.name();
    }

    @Override
    public String getTargetAssetKey() {
        return DsAssetTypeEnum.RDS_MYSQL_DATABASE.name();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(aliyunRdsMysqlInstanceProvider);
    }

    @Override
    protected List<DescribeDBInstancesResponse.DBInstance> listEntries(DsInstanceContext dsInstanceContext, DescribeDatabasesResponse.Database target) {
        AliyunDsInstanceConfig.Aliyun aliyun = buildConfig(dsInstanceContext.getDsConfig());
        return aliyunRdsMysqlHandler.listDbInstance(aliyun.getRegionId(), aliyun, target.getDBInstanceId());
    }

}
