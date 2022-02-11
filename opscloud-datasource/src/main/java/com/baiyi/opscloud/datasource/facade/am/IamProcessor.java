package com.baiyi.opscloud.datasource.facade.am;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.common.exception.common.CommonRuntimeException;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.datasource.aliyun.ram.entity.RamPolicy;
import com.baiyi.opscloud.datasource.aws.iam.drive.AmazonIdentityManagementPolicyDrive;
import com.baiyi.opscloud.datasource.aws.iam.drive.AmazonIdentityManagementUserDrive;
import com.baiyi.opscloud.datasource.aws.iam.entity.IamPolicy;
import com.baiyi.opscloud.datasource.aws.iam.entity.IamUser;
import com.baiyi.opscloud.datasource.manager.base.NoticeManager;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.notice.message.CreateIamUserMessage;
import com.baiyi.opscloud.domain.param.user.UserAmParam;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2022/2/10 8:02 PM
 * @Version 1.0
 */
@Component
public class IamProcessor extends AbstractAmProcessor {

    @Resource
    private AmazonIdentityManagementUserDrive amazonIMUserDrive;

    @Resource
    private AmazonIdentityManagementPolicyDrive amazonIMPolicyDrive;

    @Override
    public String getDsType() {
        return DsTypeEnum.AWS.name();
    }

    @Override
    protected void createUser(String instanceUuid, User user) {
        AwsConfig.Aws config = buildConfig(instanceUuid);
        createUser(config, instanceUuid, user);
    }

    @Override
    public void grantPolicy(UserAmParam.GrantPolicy grantPolicy) {
        User user = userService.getByUsername(grantPolicy.getUsername());
        AwsConfig.Aws config = buildConfig(grantPolicy.getInstanceUuid());
        try {
            IamUser.User iamUser = amazonIMUserDrive.getUser(config, grantPolicy.getUsername());
            if (iamUser == null)
                iamUser = createUser(config, grantPolicy.getInstanceUuid(), user);
            IamPolicy.Policy policy = IamPolicy.Policy.builder()
                    .arn(grantPolicy.getPolicy().getPolicyArn())
                    .build();
            amazonIMPolicyDrive.attachUserPolicy(config, iamUser.getUserName(), policy);
            // 同步资产 IAM_USER
            dsInstanceFacade.pullAsset(grantPolicy.getInstanceUuid(), DsAssetTypeConstants.IAM_USER.name(), iamUser);
        } catch (Exception e) {
            throw new CommonRuntimeException("AWS接口查询错误！");
        }
    }

    @Override
    public void revokePolicy(UserAmParam.RevokePolicy revokePolicy) {
        User user = userService.getByUsername(revokePolicy.getUsername());
        AwsConfig.Aws config = buildConfig(revokePolicy.getInstanceUuid());
        try {
            IamUser.User iamUser = amazonIMUserDrive.getUser(config, revokePolicy.getUsername());
            if (iamUser != null) {
                RamPolicy.Policy ramPolicy = BeanCopierUtil.copyProperties(revokePolicy.getPolicy(), RamPolicy.Policy.class);
                IamPolicy.Policy policy = IamPolicy.Policy.builder()
                        .arn(revokePolicy.getPolicy().getPolicyArn())
                        .build();
                amazonIMPolicyDrive.detachUserPolicy(config, iamUser.getUserName(), policy);
                // 同步资产 IAM_USER
                dsInstanceFacade.pullAsset(revokePolicy.getInstanceUuid(), DsAssetTypeConstants.IAM_USER.name(), iamUser);
            }
        } catch (Exception e) {
            throw new CommonRuntimeException("AWS接口查询错误！");
        }
    }

    public IamUser.User createUser(AwsConfig.Aws config, String instanceUuid, User user) {
        IamUser.User iamUser;
        try {
            iamUser = amazonIMUserDrive.getUser(config, user.getUsername());
            if (iamUser != null)
                return iamUser;
        } catch (Exception ignore) {
        }
        iamUser = amazonIMUserDrive.createUser(config, user, CREATE_LOGIN_PROFILE);
        // 同步资产 IAM_USER
        dsInstanceFacade.pullAsset(instanceUuid, DsAssetTypeConstants.RAM_USER.name(), iamUser);
        CreateIamUserMessage message = CreateIamUserMessage.builder()
                .awsName(config.getAccount().getName())
                .loginUrl(config.getAccount().getLoginUrl())
                .accountId(config.getAccount().getId())
                .username(iamUser.getUserName())
                .password(user.getPassword())
                .build();
        noticeManager.sendMessage(user, NoticeManager.MsgKeys.CREATE_IAM_USER, message);
        return iamUser;
    }

    private AwsConfig.Aws buildConfig(String instanceUuid) {
        DatasourceConfig config = dsConfigHelper.getConfigByInstanceUuid(instanceUuid);
        return dsConfigHelper.build(config, AwsConfig.class).getAws();
    }

}
