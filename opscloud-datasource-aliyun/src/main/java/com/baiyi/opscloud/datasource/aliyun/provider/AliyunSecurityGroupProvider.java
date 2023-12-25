package com.baiyi.opscloud.datasource.aliyun.provider;

import com.aliyuncs.ecs.model.v20140526.DescribeSecurityGroupsResponse;
import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.core.comparer.AssetComparer;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.annotation.ChildProvider;
import com.baiyi.opscloud.core.provider.asset.AbstractAssetChildProvider;
import com.baiyi.opscloud.datasource.aliyun.converter.VpcAssetConverter;
import com.baiyi.opscloud.datasource.aliyun.ecs.driver.AliyunEcsDriver;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_ALIYUN_SECURITY_GROUP;

/**
 * @Author 修远
 * @Date 2021/6/23 4:39 下午
 * @Since 1.0
 */

@Component
@ChildProvider(parentType = DsAssetTypeConstants.VPC)
public class AliyunSecurityGroupProvider extends AbstractAssetChildProvider<DescribeSecurityGroupsResponse.SecurityGroup> {

    @Resource
    private AliyunEcsDriver aliyunEcsDriver;

    @Resource
    private AliyunSecurityGroupProvider aliyunSecurityGroupProvider;

    @Override
    @SingleTask(name = PULL_ALIYUN_SECURITY_GROUP)
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    private AliyunConfig.Aliyun buildConfig(DatasourceConfig dsConfig) {
        return dsConfigManager.build(dsConfig, AliyunConfig.class).getAliyun();
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, DescribeSecurityGroupsResponse.SecurityGroup entity) {
        return VpcAssetConverter.toAssetContainer(dsInstance, entity);
    }

    @Override
    protected AssetComparer getAssetComparer() {
        return AssetComparer.COMPARE_DESCRIPTION;
    }

    @Override
    protected List<DescribeSecurityGroupsResponse.SecurityGroup> listEntities(DsInstanceContext dsInstanceContext, DatasourceInstanceAsset asset) {
        AliyunConfig.Aliyun aliyun = buildConfig(dsInstanceContext.getDsConfig());
        return aliyunEcsDriver.listSecurityGroups(asset.getRegionId(), aliyun,asset);
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ALIYUN.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.ECS_SG.name();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(aliyunSecurityGroupProvider);
    }

}