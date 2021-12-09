package com.baiyi.opscloud.datasource.aliyun.provider;

import com.aliyuncs.ecs.model.v20140526.DescribeVSwitchesResponse;
import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.datasource.aliyun.convert.VpcAssetConvert;
import com.baiyi.opscloud.datasource.aliyun.ecs.drive.AliyunVpcDrive;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.annotation.ChildProvider;
import com.baiyi.opscloud.core.provider.asset.AbstractAssetChildProvider;
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

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_ALIYUN_VSWITCH;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/23 2:00 下午
 * @Since 1.0
 */

@Component
@ChildProvider(parentType = DsAssetTypeEnum.VPC)
public class AliyunVSwitchProvider extends AbstractAssetChildProvider<DescribeVSwitchesResponse.VSwitch> {

    @Resource
    private AliyunVpcDrive aliyunVpcDrive;

    @Resource
    private AliyunVSwitchProvider aliyunVSwitchProvider;

    @Override
    @SingleTask(name = PULL_ALIYUN_VSWITCH)
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    private AliyunConfig.Aliyun buildConfig(DatasourceConfig dsConfig) {
        return dsConfigHelper.build(dsConfig, AliyunConfig.class).getAliyun();
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, DescribeVSwitchesResponse.VSwitch entity) {
        return VpcAssetConvert.toAssetContainer(dsInstance, entity);
    }

    @Override
    protected boolean equals(DatasourceInstanceAsset asset, DatasourceInstanceAsset preAsset) {
        if (!AssetUtil.equals(preAsset.getDescription(), asset.getDescription()))
            return false;
        return true;
    }

    @Override
    protected List<DescribeVSwitchesResponse.VSwitch> listEntities(DsInstanceContext dsInstanceContext, DatasourceInstanceAsset asset) {
        AliyunConfig.Aliyun aliyun = buildConfig(dsInstanceContext.getDsConfig());
        if (CollectionUtils.isEmpty(aliyun.getRegionIds()))
            return Collections.emptyList();
        List<DescribeVSwitchesResponse.VSwitch> vSwitchList = Lists.newArrayList();
        aliyun.getRegionIds().forEach(regionId -> vSwitchList.addAll(aliyunVpcDrive.listVSwitches(regionId, aliyun, asset)));
        return vSwitchList;
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ALIYUN.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeEnum.V_SWITCH.name();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(aliyunVSwitchProvider);
    }

}