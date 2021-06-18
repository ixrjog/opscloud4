package com.baiyi.caesar.datasource.aliyun.provider;

import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.baiyi.caesar.common.datasource.AliyunDsInstanceConfig;
import com.baiyi.caesar.common.datasource.config.AliyunDsConfig;
import com.baiyi.caesar.common.type.DatasourceTypeEnum;
import com.baiyi.caesar.datasource.aliyun.convert.ComputeAssetConvert;
import com.baiyi.caesar.datasource.aliyun.ecs.handler.AliyunEcsHandler;
import com.baiyi.caesar.datasource.compute.AbstractComputeProvider;
import com.baiyi.caesar.datasource.factory.DsConfigFactory;
import com.baiyi.caesar.datasource.factory.ElasticComputeProviderFactory;
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
public class AliyunEcsProvider extends AbstractComputeProvider<DescribeInstancesResponse.Instance> {

    @Resource
    private DsConfigFactory dsFactory;

    @Resource
    private AliyunEcsHandler aliyunEcsHandler;

    @Resource
    private AliyunEcsProvider aliyunEcsProvider;

    private AliyunDsConfig.aliyun buildConfig(DatasourceConfig dsConfig) {
        return dsFactory.build(dsConfig, AliyunDsInstanceConfig.class).getAliyun();
    }

    @Override
    protected DatasourceInstanceAsset toAsset(DatasourceInstance dsInstance, DescribeInstancesResponse.Instance compute) {
        return ComputeAssetConvert.toAsset(dsInstance, compute);
    }

    @Override
    protected boolean equals(DatasourceInstanceAsset asset, DatasourceInstanceAsset preAsset) {
        preAsset.setIsActive(asset.getIsActive());
        if (!AssetUtil.equals(preAsset.getKind(), asset.getKind()))
            return false;
        if (!AssetUtil.equals(preAsset.getDescription(), asset.getDescription()))
            return false;
        if (!AssetUtil.equals(preAsset.getExpiredTime(), asset.getExpiredTime()))
            return false;
        return true;
    }

    @Override
    protected List<DescribeInstancesResponse.Instance> listInstance(DatasourceConfig dsConfig) {
        AliyunDsConfig.aliyun aliyun = buildConfig(dsConfig);
        List<DescribeInstancesResponse.Instance> instanceList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(aliyun.getRegionIds()))
            return instanceList;
        aliyun.getRegionIds().forEach(regionId -> instanceList.addAll(aliyunEcsHandler.listInstance(regionId, aliyun)));
        return instanceList;
    }

    @Override
    public String getInstanceType() {
        return DatasourceTypeEnum.ALIYUN.name();
    }

    @Override
    public String getKey() {
        return "ECS";
    }

    @Override
    public void afterPropertiesSet() {
        ElasticComputeProviderFactory.register(aliyunEcsProvider);
    }

}
