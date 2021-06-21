package com.baiyi.caesar.datasource.aliyun.provider;

import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.baiyi.caesar.common.annotation.SingleTask;
import com.baiyi.caesar.common.datasource.AliyunDsInstanceConfig;
import com.baiyi.caesar.common.datasource.config.AliyunDsConfig;
import com.baiyi.caesar.common.type.DsTypeEnum;
import com.baiyi.caesar.datasource.aliyun.convert.ComputeAssetConvert;
import com.baiyi.caesar.datasource.aliyun.ecs.handler.AliyunEcsHandler;
import com.baiyi.caesar.datasource.builder.AssetContainer;
import com.baiyi.caesar.datasource.factory.AssetProviderFactory;
import com.baiyi.caesar.datasource.asset.BaseAssetProvider;
import com.baiyi.caesar.datasource.util.AssetUtil;
import com.baiyi.caesar.domain.generator.caesar.DatasourceConfig;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstance;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstanceAsset;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/17 4:47 下午
 * @Version 1.0
 */
@Component
public class AliyunEcsProvider extends BaseAssetProvider<DescribeInstancesResponse.Instance> {

    @Resource
    private AliyunEcsHandler aliyunEcsHandler;

    @Resource
    private AliyunEcsProvider aliyunEcsProvider;

    @Override
    @SingleTask(name = "PullAliyunEcs", lockTime = 300)
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    private AliyunDsConfig.Aliyun buildConfig(DatasourceConfig dsConfig) {
        return dsFactory.build(dsConfig, AliyunDsInstanceConfig.class).getAliyun();
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, DescribeInstancesResponse.Instance entry) {
        return ComputeAssetConvert.toAssetContainer(dsInstance, entry);
    }

    @Override
    protected boolean equals(DatasourceInstanceAsset asset, DatasourceInstanceAsset preAsset) {
        if (!AssetUtil.equals(preAsset.getKind(), asset.getKind()))
            return false;
        if (!AssetUtil.equals(preAsset.getDescription(), asset.getDescription()))
            return false;
        if (!AssetUtil.equals(preAsset.getExpiredTime(), asset.getExpiredTime()))
            return false;
        return true;
    }

    @Override
    protected List<DescribeInstancesResponse.Instance> listEntries(DatasourceConfig dsConfig) {
        AliyunDsConfig.Aliyun aliyun = buildConfig(dsConfig);
        List<DescribeInstancesResponse.Instance> instanceList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(aliyun.getRegionIds()))
            return instanceList;
        aliyun.getRegionIds().forEach(regionId -> instanceList.addAll(aliyunEcsHandler.listInstance(regionId, aliyun)));
        return instanceList;
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ALIYUN.name();
    }

    @Override
    public String getAssetType() {
        return "ECS";
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(aliyunEcsProvider);
    }

}
