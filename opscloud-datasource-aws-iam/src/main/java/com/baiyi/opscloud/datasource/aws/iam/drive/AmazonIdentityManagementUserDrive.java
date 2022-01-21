package com.baiyi.opscloud.datasource.aws.iam.drive;

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
public class AmazonIdentityManagementUserDrive {

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
        request.setPolicyUsageFilter("User");
        request.setPolicyArn(policyArn);
        request.setPolicyUsageFilter("PermissionsPolicy");
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

}
