package com.baiyi.opscloud.datasource.aws;

import com.baiyi.opscloud.datasource.aws.base.BaseAwsTest;
import com.baiyi.opscloud.datasource.aws.iam.drive.AmazonIdentityManagementPolicyDrive;
import com.baiyi.opscloud.datasource.aws.iam.drive.AmazonIdentityManagementUserDrive;
import com.baiyi.opscloud.datasource.aws.iam.entity.IamPolicy;
import com.baiyi.opscloud.datasource.aws.iam.entity.IamUser;
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
    private AmazonIdentityManagementPolicyDrive amazonIMDrive;

    @Resource
    private AmazonIdentityManagementUserDrive amazonIMUserDrive;

    @Test
    void listPoliciesTest() {
        List<IamPolicy.Policy> policies = amazonIMDrive.listPolicies(getConfig().getAws());
        print("size = " + policies.size());
    }

    @Test
    void listUserPoliciesTest() {
        List<IamPolicy.Policy> userPolicies = amazonIMDrive.listUserPolicies(getConfig().getAws(), "baiyi");
        print(userPolicies);
    }

    @Test
    void listUserPolicies() {
        List<IamUser.User> users = amazonIMUserDrive.listUsersForPolicy(getConfig().getAws(), "arn:aws:iam::aws:policy/AdministratorAccess");
        print(users);
    }
}
