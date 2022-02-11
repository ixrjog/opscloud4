package com.baiyi.opscloud.datasource.aws;

import com.baiyi.opscloud.common.util.PasswordUtil;
import com.baiyi.opscloud.datasource.aws.base.BaseAwsTest;
import com.baiyi.opscloud.datasource.aws.iam.drive.AmazonIdentityManagementPolicyDrive;
import com.baiyi.opscloud.datasource.aws.iam.drive.AmazonIdentityManagementUserDrive;
import com.baiyi.opscloud.datasource.aws.iam.entity.IamPolicy;
import com.baiyi.opscloud.datasource.aws.iam.entity.IamUser;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/1/21 2:26 PM
 * @Version 1.0
 */
public class IamTest extends BaseAwsTest {

    @Resource
    private AmazonIdentityManagementPolicyDrive amazonIMPolicyDrive;

    @Resource
    private AmazonIdentityManagementUserDrive amazonIMUserDrive;

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
                .password(PasswordUtil.getPW(20))
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
}
