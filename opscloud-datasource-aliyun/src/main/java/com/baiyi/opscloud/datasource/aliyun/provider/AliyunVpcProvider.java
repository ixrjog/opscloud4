package com.baiyi.opscloud.datasource.aliyun.provider;

import com.aliyuncs.ecs.model.v20140526.DescribeVpcsResponse;
import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.core.comparer.AssetComparer;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.annotation.EnablePullChild;
import com.baiyi.opscloud.core.provider.asset.BaseAssetProvider;
import com.baiyi.opscloud.datasource.aliyun.converter.VpcAssetConverter;
import com.baiyi.opscloud.datasource.aliyun.ecs.driver.AliyunVpcDriver;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_ALIYUN_VPC;

/**
 * @Author 修远
 * @Date 2021/6/23 1:21 下午
 * @Since 1.0
 */
@Component
public class AliyunVpcProvider extends BaseAssetProvider<DescribeVpcsResponse.Vpc> {

    @Resource
    private AliyunVpcDriver aliyunVpcDriver;

    @Resource
    private AliyunVpcProvider aliyunVpcProvider;

    @Override
    @EnablePullChild(type = DsAssetTypeConstants.VPC)
    @SingleTask(name = PULL_ALIYUN_VPC, lockTime = "5m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    private AliyunConfig.Aliyun buildConfig(DatasourceConfig dsConfig) {
        return dsConfigManager.build(dsConfig, AliyunConfig.class).getAliyun();
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, DescribeVpcsResponse.Vpc entity) {
        return VpcAssetConverter.toAssetContainer(dsInstance, entity);
    }

    @Override
    protected AssetComparer getAssetComparer() {
        return AssetComparer.COMPARE_DESCRIPTION;
    }

    @Override
    protected List<DescribeVpcsResponse.Vpc> listEntities(DsInstanceContext dsInstanceContext) {
        AliyunConfig.Aliyun aliyun = buildConfig(dsInstanceContext.getDsConfig());
        if (CollectionUtils.isEmpty(aliyun.getRegionIds())) {
            return Collections.emptyList();
        }
        List<DescribeVpcsResponse.Vpc> vpcList = Lists.newArrayList();
        aliyun.getRegionIds().forEach(regionId -> vpcList.addAll(aliyunVpcDriver.listVpcs(regionId, aliyun)));
        return vpcList;
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ALIYUN.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.VPC.name();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(aliyunVpcProvider);
    }

}