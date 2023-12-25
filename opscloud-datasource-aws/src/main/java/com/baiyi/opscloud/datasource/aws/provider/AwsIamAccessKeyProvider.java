package com.baiyi.opscloud.datasource.aws.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.core.comparer.AssetComparer;
import com.baiyi.opscloud.core.comparer.AssetComparerBuilder;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.annotation.ChildProvider;
import com.baiyi.opscloud.core.provider.asset.AbstractAssetChildProvider;
import com.baiyi.opscloud.datasource.aws.iam.driver.AmazonIdentityManagementAccessKeyDriver;
import com.baiyi.opscloud.datasource.aws.iam.entity.IamAccessKey;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.List;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_AWS_IAM_ACCESS_KEY;

/**
 * @Author baiyi
 * @Date 2022/1/24 10:04 AM
 * @Version 1.0
 */
@Component
@ChildProvider(parentType = DsAssetTypeConstants.IAM_USER)
public class AwsIamAccessKeyProvider extends AbstractAssetChildProvider<IamAccessKey.AccessKey> {

    @Resource
    private AmazonIdentityManagementAccessKeyDriver amazonIMAccessKeyDriver;

    @Resource
    private AwsIamAccessKeyProvider awsIamAccessKeyProvider;

    @Override
    @SingleTask(name = PULL_AWS_IAM_ACCESS_KEY)
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    private AwsConfig.Aws buildConfig(DatasourceConfig dsConfig) {
        return dsConfigManager.build(dsConfig, AwsConfig.class).getAws();
    }

    @Override
    protected AssetComparer getAssetComparer() {
        return AssetComparerBuilder.newBuilder()
                .compareOfActive()
                .build();
    }

    @Override
    protected List<IamAccessKey.AccessKey> listEntities(DsInstanceContext dsInstanceContext, DatasourceInstanceAsset asset) {
        AwsConfig.Aws config = buildConfig(dsInstanceContext.getDsConfig());
        return  amazonIMAccessKeyDriver.listAccessKeys(config,asset.getAssetKey());
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.AWS.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.IAM_ACCESS_KEY.name();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(awsIamAccessKeyProvider);
    }

}