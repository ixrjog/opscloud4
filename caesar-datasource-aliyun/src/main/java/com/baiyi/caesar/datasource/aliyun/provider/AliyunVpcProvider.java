package com.baiyi.caesar.datasource.aliyun.provider;

import com.aliyuncs.ecs.model.v20140526.DescribeVpcsResponse;
import com.baiyi.caesar.common.annotation.SingleTask;
import com.baiyi.caesar.common.datasource.AliyunDsInstanceConfig;
import com.baiyi.caesar.common.datasource.config.DsAliyunConfig;
import com.baiyi.caesar.common.type.DsAssetTypeEnum;
import com.baiyi.caesar.common.type.DsTypeEnum;
import com.baiyi.caesar.datasource.aliyun.convert.VpcAssetConvert;
import com.baiyi.caesar.datasource.aliyun.ecs.handler.AliyunVpcHandler;
import com.baiyi.caesar.datasource.asset.BaseAssetProvider;
import com.baiyi.caesar.datasource.builder.AssetContainer;
import com.baiyi.caesar.datasource.factory.AssetProviderFactory;
import com.baiyi.caesar.datasource.util.AssetUtil;
import com.baiyi.caesar.domain.generator.caesar.DatasourceConfig;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstance;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstanceAsset;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/23 1:21 下午
 * @Since 1.0
 */
@Component
public class AliyunVpcProvider extends BaseAssetProvider<DescribeVpcsResponse.Vpc> {

    @Resource
    private AliyunVpcHandler aliyunVpcHandler;

    @Resource
    private AliyunVpcProvider aliyunVpcProvider;

    @Override
    @SingleTask(name = "PullAliyunVpc", lockTime = 300)
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    private DsAliyunConfig.Aliyun buildConfig(DatasourceConfig dsConfig) {
        return dsFactory.build(dsConfig, AliyunDsInstanceConfig.class).getAliyun();
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, DescribeVpcsResponse.Vpc entry) {
        return VpcAssetConvert.toAssetContainer(dsInstance, entry);
    }

    @Override
    protected boolean equals(DatasourceInstanceAsset asset, DatasourceInstanceAsset preAsset) {
        if (!AssetUtil.equals(preAsset.getDescription(), asset.getDescription()))
            return false;
        return true;
    }

    @Override
    protected List<DescribeVpcsResponse.Vpc> listEntries(DatasourceConfig dsConfig) {
        DsAliyunConfig.Aliyun aliyun = buildConfig(dsConfig);
        if (CollectionUtils.isEmpty(aliyun.getRegionIds()))
            return Collections.emptyList();
        List<DescribeVpcsResponse.Vpc> vpcList = Lists.newArrayList();
        aliyun.getRegionIds().forEach(regionId -> vpcList.addAll(aliyunVpcHandler.listVpcs(regionId, aliyun)));
        return vpcList;
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ALIYUN.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeEnum.VPC.getType();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(aliyunVpcProvider);
    }

}