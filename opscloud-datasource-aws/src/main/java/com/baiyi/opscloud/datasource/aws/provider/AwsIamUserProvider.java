package com.baiyi.opscloud.datasource.aws.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.core.comparer.AssetComparer;
import com.baiyi.opscloud.core.comparer.AssetComparerBuilder;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.annotation.EnablePullChild;
import com.baiyi.opscloud.core.provider.asset.AbstractAssetRelationProvider;
import com.baiyi.opscloud.datasource.aws.iam.driver.AmazonIdentityManagementUserDriver;
import com.baiyi.opscloud.datasource.aws.iam.entity.IamPolicy;
import com.baiyi.opscloud.datasource.aws.iam.entity.IamUser;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_AWS_IAM_USER;

/**
 * @Author baiyi
 * @Date 2022/1/21 4:42 PM
 * @Version 1.0
 */
@Component
public class AwsIamUserProvider extends AbstractAssetRelationProvider<IamUser.User, IamPolicy.Policy> {

    @Resource
    private AmazonIdentityManagementUserDriver amazonIMUserDriver;

    @Resource
    private AwsIamUserProvider awsIamUserProvider;

    @Override
    @EnablePullChild(type = DsAssetTypeConstants.IAM_USER)
    @SingleTask(name = PULL_AWS_IAM_USER, lockTime = "2m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    private AwsConfig.Aws buildConfig(DatasourceConfig dsConfig) {
        return dsConfigManager.build(dsConfig, AwsConfig.class).getAws();
    }

    @Override
    protected AssetComparer getAssetComparer() {
        return AssetComparerBuilder.newBuilder()
                .compareOfAssetId()
                .compareOfKey()
                .compareOfKey2()
                .build();
    }

    @Override
    protected List<IamUser.User> listEntities(DsInstanceContext dsInstanceContext) {
        AwsConfig.Aws config = buildConfig(dsInstanceContext.getDsConfig());
        return amazonIMUserDriver.listUsers(config);
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.AWS.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.IAM_USER.name();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(awsIamUserProvider);
    }

    @Override
    protected List<IamUser.User> listEntities(DsInstanceContext dsInstanceContext, IamPolicy.Policy target) {
        AwsConfig.Aws config = buildConfig(dsInstanceContext.getDsConfig());
        return amazonIMUserDriver.listUsersForPolicy(config, target.getArn());
    }

    @Override
    public String getTargetAssetKey() {
        return DsAssetTypeConstants.IAM_POLICY.name();
    }
}

