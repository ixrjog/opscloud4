package com.baiyi.opscloud.datasource.facade.am;

import com.amazonaws.services.identitymanagement.model.VirtualMFADevice;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.constants.enums.UserCredentialTypeEnum;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.common.exception.common.OCException;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.StringFormatter;
import com.baiyi.opscloud.datasource.aliyun.ram.entity.RamPolicy;
import com.baiyi.opscloud.datasource.aws.iam.driver.AmazonIdentityManagementMFADriver;
import com.baiyi.opscloud.datasource.aws.iam.driver.AmazonIdentityManagementPolicyDriver;
import com.baiyi.opscloud.datasource.aws.iam.driver.AmazonIdentityManagementUserDriver;
import com.baiyi.opscloud.datasource.aws.iam.entity.IamPolicy;
import com.baiyi.opscloud.datasource.aws.iam.entity.IamUser;
import com.baiyi.opscloud.datasource.manager.base.NoticeManager;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.notice.message.CreateIamUserMessage;
import com.baiyi.opscloud.domain.notice.message.UpdateIamLoginProfileMessage;
import com.baiyi.opscloud.domain.param.user.UserAmParam;
import com.baiyi.opscloud.domain.param.user.UserCredentialParam;
import com.baiyi.opscloud.facade.user.UserCredentialFacade;
import com.google.common.base.Joiner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.baiyi.opscloud.datasource.aliyun.ram.driver.AliyunRamUserDriver.NO_PASSWORD_RESET_REQUIRED;

/**
 * AWS IAM
 *
 * @Author baiyi
 * @Date 2022/2/10 8:02 PM
 * @Version 1.0
 */
@SuppressWarnings("ALL")
@Slf4j
@Component
@RequiredArgsConstructor
public class IdentityAndAccessManagementProcessor extends AbstractAccessManagementProcessor {

    private final AmazonIdentityManagementUserDriver amazonIMUserDriver;

    private final AmazonIdentityManagementPolicyDriver amazonIMPolicyDriver;

    private final AmazonIdentityManagementMFADriver amazonIMMFADriver;

    private final MFADelegate mfaDelegate;

    private final UserCredentialFacade userCredentialFacade;

    private static final String DEF_SERIAL_NUMBER = "arn:aws:iam::{}:mfa/{}";

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
            IamUser.User iamUser = amazonIMUserDriver.getUser(config, grantPolicy.getUsername());
            if (iamUser == null) {
                iamUser = createUser(config, grantPolicy.getInstanceUuid(), user);
            }
            IamPolicy.Policy policy = IamPolicy.Policy.builder()
                    .arn(grantPolicy.getPolicy().getPolicyArn())
                    .build();
            amazonIMPolicyDriver.attachUserPolicy(config, iamUser.getUserName(), policy);
            // 同步资产 IAM_USER
            dsInstanceFacade.pullAsset(grantPolicy.getInstanceUuid(), DsAssetTypeConstants.IAM_USER.name(), iamUser);
        } catch (Exception e) {
            throw new OCException("AWS接口查询错误: {}", e.getMessage());
        }
    }

    @Override
    public void revokePolicy(UserAmParam.RevokePolicy revokePolicy) {
        AwsConfig.Aws config = buildConfig(revokePolicy.getInstanceUuid());
        try {
            IamUser.User iamUser = amazonIMUserDriver.getUser(config, revokePolicy.getUsername());
            if (iamUser != null) {
                RamPolicy.Policy ramPolicy = BeanCopierUtil.copyProperties(revokePolicy.getPolicy(), RamPolicy.Policy.class);
                IamPolicy.Policy policy = IamPolicy.Policy.builder()
                        .arn(revokePolicy.getPolicy().getPolicyArn())
                        .build();
                amazonIMPolicyDriver.detachUserPolicy(config, iamUser.getUserName(), policy);
                // 同步资产 IAM_USER
                dsInstanceFacade.pullAsset(revokePolicy.getInstanceUuid(), DsAssetTypeConstants.IAM_USER.name(), iamUser);
            }
        } catch (Exception e) {
            throw new OCException("AWS接口查询错误: {}", e.getMessage());
        }
    }

    public IamUser.User createUser(AwsConfig.Aws config, String instanceUuid, User user) {
        IamUser.User iamUser;
        try {
            iamUser = amazonIMUserDriver.getUser(config, user.getUsername());
            if (iamUser != null) {
                return iamUser;
            }
        } catch (Exception ignore) {
        }
        iamUser = amazonIMUserDriver.createUser(config, user, CREATE_LOGIN_PROFILE);
        if (enableMFA(instanceUuid)) {
            // 启用虚拟MFA设备
            enableMFADevice(config, instanceUuid, user);
        }
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

    private void enableMFADevice(AwsConfig.Aws config, String instanceUuid, User user) {
        final String serialNumber = StringFormatter.arrayFormat(DEF_SERIAL_NUMBER, config.getAccount().getId(), user.getUsername());
        try {
            amazonIMMFADriver.deleteVirtualMFADevice(config, serialNumber);
            log.debug("删除IAM虚拟MFA设备: serialNumber={}", serialNumber);
        } catch (Exception e) {
            log.debug("删除IAM虚拟MFA设备错误: serialNumber={}, {}", serialNumber, e.getMessage());
        }
        try {
            log.info("创建用户 {} 的IAM虚拟MFA", user.getUsername());
            VirtualMFADevice vMFADevice = amazonIMMFADriver.createVirtualMFADevice(config, user);
            mfaDelegate.enableMFADevice(config, user, vMFADevice);
            // 录入MFA密钥
            UserCredentialParam.Credential credential = UserCredentialParam.Credential.builder()
                    .instanceUuid(instanceUuid)
                    .title(vMFADevice.getSerialNumber())
                    .userId(user.getId())
                    .valid(true)
                    .credentialType(UserCredentialTypeEnum.IAM_OTP_SK.getType())
                    .credential(new String(vMFADevice.getBase32StringSeed().array()))
                    .comment(Joiner.on("@").join(user.getUsername(), config.getAccount().getName()))
                    .build();
            log.info("录入用户 {} 的IAM虚拟MFA凭据", user.getUsername());
            userCredentialFacade.saveCredential(credential, user);
        } catch (Exception e) {
            log.error("启用IAM虚拟MFA失败: {}", e.getMessage());
        }
    }

    @Override
    public void updateLoginProfile(UserAmParam.UpdateLoginProfile updateLoginProfile) {
        AwsConfig.Aws config = buildConfig(updateLoginProfile.getInstanceUuid());
        User user = userService.getByUsername(updateLoginProfile.getUsername());
        amazonIMUserDriver.updateLoginProfile(config, user, updateLoginProfile.getPassword(), NO_PASSWORD_RESET_REQUIRED);

        UpdateIamLoginProfileMessage message = UpdateIamLoginProfileMessage.builder()
                .awsName(config.getAccount().getName())
                .loginUrl(config.getAccount().getLoginUrl())
                .accountId(config.getAccount().getId())
                .username(updateLoginProfile.getUsername())
                .password(updateLoginProfile.getPassword())
                .build();
        noticeManager.sendMessage(user, NoticeManager.MsgKeys.AWS_IAM_UPDATE_LOGIN_PROFILE, message);
    }

    private AwsConfig.Aws buildConfig(String instanceUuid) {
        DatasourceConfig config = dsConfigManager.getConfigByInstanceUuid(instanceUuid);
        return dsConfigManager.build(config, AwsConfig.class).getAws();
    }

}