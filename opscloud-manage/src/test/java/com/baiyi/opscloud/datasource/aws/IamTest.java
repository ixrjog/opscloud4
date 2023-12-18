package com.baiyi.opscloud.datasource.aws;

import com.amazonaws.services.identitymanagement.model.EnableMFADeviceResult;
import com.amazonaws.services.identitymanagement.model.VirtualMFADevice;
import com.baiyi.opscloud.common.constants.enums.UserCredentialTypeEnum;
import com.baiyi.opscloud.common.util.PasswordUtil;
import com.baiyi.opscloud.datasource.aws.base.BaseAwsTest;
import com.baiyi.opscloud.datasource.aws.iam.driver.AmazonIdentityManagementMFADriver;
import com.baiyi.opscloud.datasource.aws.iam.driver.AmazonIdentityManagementPolicyDriver;
import com.baiyi.opscloud.datasource.aws.iam.driver.AmazonIdentityManagementUserDriver;
import com.baiyi.opscloud.datasource.aws.iam.entity.IamPolicy;
import com.baiyi.opscloud.datasource.aws.iam.entity.IamUser;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.param.user.UserCredentialParam;
import com.baiyi.opscloud.facade.user.UserCredentialFacade;
import com.baiyi.opscloud.otp.OtpUtil;
import com.baiyi.opscloud.otp.model.OTPAccessCode;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/1/21 2:26 PM
 * @Version 1.0
 */
@Slf4j
public class IamTest extends BaseAwsTest {

    @Resource
    private AmazonIdentityManagementPolicyDriver amazonIMPolicyDrive;

    @Resource
    private AmazonIdentityManagementUserDriver amazonIMUserDrive;

    @Resource
    private UserCredentialFacade userCredentialFacade;

    @Test
    void listPoliciesTest() {
        List<IamPolicy.Policy> policies = amazonIMPolicyDrive.listPolicies(getConfig().getAws());
        print("size = " + policies.size());
    }

    @Test
    void listUserPoliciesTest1() {
        List<IamPolicy.Policy> userPolicies = amazonIMPolicyDrive.listUserPolicies(getConfig().getAws(), "baiyi");
        print(userPolicies);
    }

    @Test
    void listUserPoliciesTest() {
        List<IamUser.User> users = amazonIMUserDrive.listUsersForPolicy(getConfig().getAws(), "arn:aws:iam::aws:policy/AdministratorAccess");
        print(users);
    }

    @Test
    void createUserTest() {
        User user = User.builder()
                .username("aaa-test")
                .password(PasswordUtil.generatorPassword(20, true))
                .build();
        IamUser.User createUser = amazonIMUserDrive.createUser(getConfig().getAws(), user, true);
        print(createUser);
    }

    @Test
    void attachPolicyToUserTest() {
        IamPolicy.Policy policy = IamPolicy.Policy.builder()
                .arn("arn:aws:iam::aws:policy/AmazonRoute53RecoveryClusterReadOnlyAccess")
                .build();
        amazonIMPolicyDrive.attachUserPolicy(getConfig().getAws(), "aaa-test", policy);
    }

    @Resource
    private AmazonIdentityManagementMFADriver amazonIMMFADriver;

    @Test
    void iamDeleteMfaTest() {
        try {
            log.info("第1次删除");
            amazonIMMFADriver.deleteVirtualMFADevice(getConfig().getAws(), "arn:aws:iam::506262517929:mfa/baiyitest");
            log.info("第2次删除");
            amazonIMMFADriver.deleteVirtualMFADevice(getConfig().getAws(), "arn:aws:iam::506262517929:mfa/baiyitest");
        } catch (Exception e) {
            print("删除错误！");
        }
    }

    @Test
    void iamMfaTest() {
        try {
            amazonIMMFADriver.deleteVirtualMFADevice(getConfig().getAws(), "arn:aws:iam::506262517929:mfa/baiyitest");
        } catch (Exception e) {
            print("删除错误！");
        }
        User user = User.builder()
                .username("baiyitest")
                .build();
        VirtualMFADevice virtualMFADevice = amazonIMMFADriver.createVirtualMFADevice(getConfig().getAws(), user);
        String sk = new String(virtualMFADevice.getBase32StringSeed().array());
        print(sk);
        print(virtualMFADevice.getSerialNumber());
    }


    /**
     * HFDS7SESXSB3APVVS33V5TFYBUW7NP42F5XWCAN7BWEMYFRZIX5VRVROVIELVFRD
     * arn:aws:iam::506262517929:mfa/baiyitest
     */
    @Test
    void iamMfaTest2() throws Exception {
        final String serialNumber = "arn:aws:iam::506262517929:mfa/baiyitest";
        User user = User.builder()
                .username("baiyitest")
                .build();
        SecretKey key = OtpUtil.toKey("HFDS7SESXSB3APVVS33V5TFYBUW7NP42F5XWCAN7BWEMYFRZIX5VRVROVIELVFRD");
        OTPAccessCode.AccessCode ac = OtpUtil.generateOtpAccessCode(key);
        EnableMFADeviceResult r = amazonIMMFADriver.enableMFADevice(getConfig().getAws(), user, serialNumber, ac.getCurrentPassword(), ac.getFuturePassword());
        print(r);
    }

    @Test
    void saveCredentialTest() {
        User user = User.builder()
                .id(1)
                .username("baiyi")
                .build();
        UserCredentialParam.Credential credential = UserCredentialParam.Credential.builder()
                .instanceUuid("121213")
                .title("xdpisjdg")
                .userId(99999)
                .valid(true)
                .credentialType(UserCredentialTypeEnum.IAM_OTP_SK.getType())
                .credential("ABCDEE")
                .build();
        userCredentialFacade.saveCredential(credential, user);
    }
}
