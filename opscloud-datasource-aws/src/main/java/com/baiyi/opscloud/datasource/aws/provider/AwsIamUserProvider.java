package com.baiyi.opscloud.datasource.aws.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.annotation.EnablePullChild;
import com.baiyi.opscloud.core.provider.asset.AbstractAssetRelationProvider;
import com.baiyi.opscloud.core.util.AssetUtil;
import com.baiyi.opscloud.datasource.aws.iam.drive.AmazonIdentityManagementPolicyDrive;
import com.baiyi.opscloud.datasource.aws.iam.drive.AmazonIdentityManagementUserDrive;
import com.baiyi.opscloud.datasource.aws.iam.entity.IamPolicy;
import com.baiyi.opscloud.datasource.aws.iam.entity.IamUser;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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
    private AmazonIdentityManagementPolicyDrive amazonIMPolicyDrive;

    @Resource
    private AmazonIdentityManagementUserDrive amazonIMUserDrive;

    @Resource
    private AwsIamUserProvider awsIamUserProvider;

    @Override
    @EnablePullChild(type = DsAssetTypeConstants.IAM_USER)
    @SingleTask(name = PULL_AWS_IAM_USER, lockTime = "2m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    private AwsConfig.Aws buildConfig(DatasourceConfig dsConfig) {
        return dsConfigHelper.build(dsConfig, AwsConfig.class).getAws();
    }

    @Override
    protected boolean equals(DatasourceInstanceAsset asset, DatasourceInstanceAsset preAsset) {
        if (!AssetUtil.equals(preAsset.getAssetId(), asset.getAssetId()))
            return false;
        if (!AssetUtil.equals(preAsset.getAssetKey(), asset.getAssetKey()))
            return false;
        if (!AssetUtil.equals(preAsset.getAssetKey2(), asset.getAssetKey2()))
            return false;
        return true;
    }

    @Override
    protected List<IamUser.User> listEntities(DsInstanceContext dsInstanceContext) {
        AwsConfig.Aws config = buildConfig(dsInstanceContext.getDsConfig());
        return amazonIMUserDrive.listUsers(config);
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
        return amazonIMUserDrive.listUsersForPolicy(config, target.getArn());
    }

    @Override
    public String getTargetAssetKey() {
        return DsAssetTypeConstants.IAM_POLICY.name();
    }
}

