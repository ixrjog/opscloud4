package com.baiyi.opscloud.datasource.aliyun.provider;

import com.aliyuncs.ecs.model.v20140526.DescribeSecurityGroupsResponse;
import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.datasource.AliyunDsInstanceConfig;
import com.baiyi.opscloud.common.datasource.config.DsAliyunConfig;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.baiyi.opscloud.common.type.DsTypeEnum;
import com.baiyi.opscloud.datasource.aliyun.convert.VpcAssetConvert;
import com.baiyi.opscloud.datasource.aliyun.ecs.handler.AliyunEcsHandler;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.datasource.factory.AssetProviderFactory;
import com.baiyi.opscloud.datasource.model.DsInstanceContext;
import com.baiyi.opscloud.datasource.provider.annotation.ChildProvider;
import com.baiyi.opscloud.datasource.provider.asset.AbstractAssetChildProvider;
import com.baiyi.opscloud.datasource.util.AssetUtil;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/23 4:39 下午
 * @Since 1.0
 */

@Component
@ChildProvider(parentType = DsAssetTypeEnum.VPC)
public class AliyunSecurityGroupProvider extends AbstractAssetChildProvider<DescribeSecurityGroupsResponse.SecurityGroup> {

    @Resource
    private AliyunEcsHandler aliyunEcsHandler;

    @Resource
    private AliyunSecurityGroupProvider aliyunSecurityGroupProvider;

    @Override
    @SingleTask(name = "pull_aliyun_security_group")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    private DsAliyunConfig.Aliyun buildConfig(DatasourceConfig dsConfig) {
        return dsConfigFactory.build(dsConfig, AliyunDsInstanceConfig.class).getAliyun();
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, DescribeSecurityGroupsResponse.SecurityGroup entry) {
        return VpcAssetConvert.toAssetContainer(dsInstance, entry);
    }

    @Override
    protected boolean equals(DatasourceInstanceAsset asset, DatasourceInstanceAsset preAsset) {
        if (!AssetUtil.equals(preAsset.getDescription(), asset.getDescription()))
            return false;
        return true;
    }

    @Override
    protected List<DescribeSecurityGroupsResponse.SecurityGroup> listEntries(DsInstanceContext dsInstanceContext,DatasourceInstanceAsset asset) {
        DsAliyunConfig.Aliyun aliyun = buildConfig(dsInstanceContext.getDsConfig());
        if (CollectionUtils.isEmpty(aliyun.getRegionIds()))
            return Collections.emptyList();
        List<DescribeSecurityGroupsResponse.SecurityGroup> securityGroupList = Lists.newArrayList();
        aliyun.getRegionIds().forEach(regionId -> securityGroupList.addAll(aliyunEcsHandler.listSecurityGroups(regionId, aliyun,asset)));
        return securityGroupList;
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ALIYUN.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeEnum.ECS_SG.name();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(aliyunSecurityGroupProvider);
    }
}
