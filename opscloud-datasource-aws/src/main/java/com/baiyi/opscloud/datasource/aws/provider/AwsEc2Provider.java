package com.baiyi.opscloud.datasource.aws.provider;

import com.amazonaws.services.ec2.model.Instance;
import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.AbstractAssetBusinessRelationProvider;
import com.baiyi.opscloud.core.util.AssetUtil;
import com.baiyi.opscloud.datasource.aws.ec2.drive.AmazonEc2Drive;
import com.baiyi.opscloud.datasource.aws.ec2.entity.Ec2Instance;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_AWS_EC2;

/**
 * @Author baiyi
 * @Date 2022/1/24 6:13 PM
 * @Version 1.0
 */
@Component
public class AwsEc2Provider extends AbstractAssetBusinessRelationProvider<Ec2Instance.Instance> {

    @Resource
    private AmazonEc2Drive amazonEc2Drive;

    @Resource
    private AwsEc2Provider awsEc2Provider;

    @Override
    @SingleTask(name = PULL_AWS_EC2, lockTime = "5m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    private AwsConfig.Aws buildConfig(DatasourceConfig dsConfig) {
        return dsConfigHelper.build(dsConfig, AwsConfig.class).getAws();
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, Ec2Instance.Instance entity) {
        return entity.toAssetContainer(dsInstance);
    }

    @Override
    protected boolean equals(DatasourceInstanceAsset asset, DatasourceInstanceAsset preAsset) {
        if (!AssetUtil.equals(preAsset.getName(), asset.getName()))
            return false;
        if (!AssetUtil.equals(preAsset.getAssetKey2(), asset.getAssetKey2()))
            return false;
        if (!AssetUtil.equals(preAsset.getKind(), asset.getKind()))
            return false;
//        if (!AssetUtil.equals(preAsset.getDescription(), asset.getDescription()))
//            return false;
        if (!AssetUtil.equals(preAsset.getExpiredTime(), asset.getExpiredTime()))
            return false;
        return true;
    }

    @Override
    protected List<Ec2Instance.Instance> listEntities(DsInstanceContext dsInstanceContext) {
        AwsConfig.Aws aws = buildConfig(dsInstanceContext.getDsConfig());
        if (CollectionUtils.isEmpty(aws.getRegionIds()))
            return Collections.emptyList();
        List<Ec2Instance.Instance> instanceList = Lists.newArrayList();
        aws.getRegionIds().forEach(regionId ->
                instanceList.addAll(amazonEc2Drive.listInstances(regionId, aws))
        );
        return instanceList;
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.AWS.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.EC2.name();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(awsEc2Provider);
    }

}
