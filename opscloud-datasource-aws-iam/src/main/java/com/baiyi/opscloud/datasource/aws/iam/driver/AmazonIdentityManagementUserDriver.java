package com.baiyi.opscloud.datasource.aws.iam.driver;

import com.amazonaws.services.identitymanagement.AmazonIdentityManagement;
import com.amazonaws.services.identitymanagement.model.*;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.datasource.aws.iam.entity.IamUser;
import com.baiyi.opscloud.datasource.aws.iam.service.AmazonIdentityManagementService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2022/1/21 3:20 PM
 * @Version 1.0
 */
@Component
public class AmazonIdentityManagementUserDriver {

    public static final boolean NO_PASSWORD_RESET_REQUIRED = false;

    // https://docs.aws.amazon.com/zh_cn/IAM/latest/APIReference/API_ListEntitiesForPolicy.html
    private interface EntityFilter {
        String USER = "User";
        String ROLE = "Role";
        String GROUP = "Group";
        String LOCAL_MANAGED = "LocalManagedPolicy";
        String AWS_MANAGED = "AWSManagedPolicy";
    }

    private interface PolicyUsageFilter {
        String POLICY = "PermissionsPolicy";
        String BOUNDARY = "PermissionsBoundary";
    }

    /**
     * https://docs.aws.amazon.com/zh_cn/IAM/latest/APIReference/API_CreateUser.html
     *
     * @param config
     */
    public IamUser.User createUser(AwsConfig.Aws config, com.baiyi.opscloud.domain.generator.opscloud.User user, boolean createLoginProfile) {
        CreateUserRequest request = new CreateUserRequest();
        request.setUserName(user.getUsername());
        CreateUserResult result = buildAmazonIdentityManagement(config).createUser(request);
        if (createLoginProfile) {
            this.createLoginProfile(config, user, NO_PASSWORD_RESET_REQUIRED);
        }
        return BeanCopierUtil.copyProperties(result.getUser(), IamUser.User.class);
    }

    /**
     * https://docs.aws.amazon.com/zh_cn/IAM/latest/APIReference/API_CreateLoginProfile.html
     *
     * @param config
     * @param user
     * @param passwordResetRequired
     * @return
     */
    private LoginProfile createLoginProfile(AwsConfig.Aws config, com.baiyi.opscloud.domain.generator.opscloud.User user, boolean passwordResetRequired) {
        CreateLoginProfileRequest request = new CreateLoginProfileRequest();
        request.setUserName(user.getUsername());
        request.setPassword(user.getPassword());
        request.setPasswordResetRequired(passwordResetRequired);
        CreateLoginProfileResult result = buildAmazonIdentityManagement(config).createLoginProfile(request);
        return result.getLoginProfile();
    }

    public List<IamUser.User> listUsers(AwsConfig.Aws config) {
        ListUsersRequest request = new ListUsersRequest();
        List<User> users = Lists.newArrayList();
        while (true) {
            ListUsersResult result = buildAmazonIdentityManagement(config).listUsers(request);
            users.addAll(result.getUsers());
            if (result.getIsTruncated()) {
                request.setMarker(result.getMarker());
            } else {
                break;
            }
        }
        return BeanCopierUtil.copyListProperties(users, IamUser.User.class);
    }

    public List<IamUser.User> listUsersForPolicy(AwsConfig.Aws config, String policyArn) {
        ListEntitiesForPolicyRequest request = new ListEntitiesForPolicyRequest();
        // 用于筛选结果的类型
        request.setEntityFilter(EntityFilter.USER);
        request.setPolicyArn(policyArn);
        /**
         * 用于过滤结果的策略使用方法
         * 只列出权限策略，设置为PermissionsPolicy
         * 只列出用于设置权限边界的策略，设置为PermissionsBoundary
         */
        request.setPolicyUsageFilter(PolicyUsageFilter.POLICY);
        List<PolicyUser> policyUsers = Lists.newArrayList();
        while (true) {
            ListEntitiesForPolicyResult result = buildAmazonIdentityManagement(config).listEntitiesForPolicy(request);
            policyUsers.addAll(result.getPolicyUsers());
            if (result.getIsTruncated()) {
                request.setMarker(result.getMarker());
            } else {
                break;
            }
        }
        return policyUsers.stream().map(e -> getUser(config, e.getUserName())).collect(Collectors.toList());
    }

    public IamUser.User getUser(AwsConfig.Aws config, String userName) {
        GetUserRequest request = new GetUserRequest();
        request.setUserName(userName);
        GetUserResult result = buildAmazonIdentityManagement(config).getUser(request);
        return BeanCopierUtil.copyProperties(result.getUser(), IamUser.User.class);
    }

    private AmazonIdentityManagement buildAmazonIdentityManagement(AwsConfig.Aws aws) {
        return AmazonIdentityManagementService.buildAmazonIdentityManagement(aws);
    }

    /**
     * 删除登录配置文件
     * https://docs.aws.amazon.com/IAM/latest/APIReference/API_DeleteLoginProfile.html
     *
     * @param config
     * @param userName
     * @return
     */
    public void deleteLoginProfile(AwsConfig.Aws config, String userName) {
        DeleteLoginProfileRequest request = new DeleteLoginProfileRequest();
        request.setUserName(userName);
        DeleteLoginProfileResult result = buildAmazonIdentityManagement(config).deleteLoginProfile(request);
    }

}
