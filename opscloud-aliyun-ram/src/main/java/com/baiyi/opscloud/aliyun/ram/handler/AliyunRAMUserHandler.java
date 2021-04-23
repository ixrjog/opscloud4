package com.baiyi.opscloud.aliyun.ram.handler;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.ram.model.v20150501.*;
import com.baiyi.opscloud.aliyun.core.config.AliyunCoreConfig;
import com.baiyi.opscloud.aliyun.ram.base.BaseAliyunRAM;
import com.baiyi.opscloud.common.util.RegexUtils;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/6/9 11:11 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class AliyunRAMUserHandler extends BaseAliyunRAM {


    public static final boolean NO_PASSWORD_RESET_REQUIRED = false;

    /**
     * 创建RAM账户
     *
     * @param aliyunAccount
     * @param ocUser
     * @param createLoginProfile
     * @return
     */
    public BusinessWrapper<CreateUserResponse.User> createRamUser(AliyunCoreConfig.AliyunAccount aliyunAccount, OcUser ocUser, boolean createLoginProfile) {
        IAcsClient client = acqAcsClient(aliyunAccount);
        BusinessWrapper<CreateUserResponse.User> wrapper = createUser(client, ocUser);
        if (!wrapper.isSuccess())
            return wrapper;
        if (createLoginProfile) {
            createLoginProfile(client, ocUser, NO_PASSWORD_RESET_REQUIRED);
        }
        return wrapper;
    }

    private BusinessWrapper<CreateUserResponse.User> createUser(IAcsClient client, OcUser ocUser) {
        CreateUserRequest request = new CreateUserRequest();
        request.setUserName(ocUser.getUsername());
        request.setDisplayName(ocUser.getDisplayName());
        if (RegexUtils.isPhone(ocUser.getPhone()))
            request.setMobilePhone("86-" + ocUser.getPhone());
        if (!StringUtils.isEmpty(ocUser.getEmail()))
            request.setEmail(ocUser.getEmail());
        request.setComments("Created by opsCloud");
        return createUserResponse(client, request);
    }

    private BusinessWrapper<CreateUserResponse.User> createUserResponse(IAcsClient client, CreateUserRequest request) {
        try {
            return new BusinessWrapper(client.getAcsResponse(request).getUser());
        } catch (ClientException e) {
            return new BusinessWrapper(10000, e.getMessage());
        }
    }

    /**
     * 删除账户
     *
     * @param aliyunAccount
     * @param username
     * @return
     */
    public boolean deleteRamUser(AliyunCoreConfig.AliyunAccount aliyunAccount, String username) {
        IAcsClient client = acqAcsClient(aliyunAccount);
        List<ListPoliciesForUserResponse.Policy> policyList = listPoliciesForUser(client, username);
        if (!CollectionUtils.isEmpty(policyList)) {
            policyList.forEach(policy -> detachPolicyFromUser(client, username, policy));
        }
        List<ListGroupsForUserResponse.Group> groupList = listGroupsForUser(client, username);
        if (!CollectionUtils.isEmpty(groupList)) {
            groupList.forEach(group -> removeUserFromGroup(client, username, group));
        }
        DeleteUserResponse response = deleteUser(client, username);
        return response != null && !StringUtils.isEmpty(response.getRequestId());
    }

    private DeleteUserResponse deleteUser(IAcsClient client, String username) {
        DeleteUserRequest request = new DeleteUserRequest();
        request.setUserName(username);
        try {
            return client.getAcsResponse(request);
        } catch (ClientException e) {
            log.error("删除RAM用户失败，用户名：{}", username, e);
            return null;
        }
    }

    private List<ListPoliciesForUserResponse.Policy> listPoliciesForUser(IAcsClient client, String username) {
        ListPoliciesForUserRequest request = new ListPoliciesForUserRequest();
        request.setUserName(username);
        try {
            ListPoliciesForUserResponse response = client.getAcsResponse(request);
            return response == null ? Collections.emptyList() : response.getPolicies();
        } catch (ClientException e) {
            log.error("获取RAM用户权限列表失败，用户名为：{}", username, e);
            return Collections.emptyList();
        }
    }

    private void detachPolicyFromUser(IAcsClient client, String username, ListPoliciesForUserResponse.Policy policy) {
        DetachPolicyFromUserRequest request = new DetachPolicyFromUserRequest();
        request.setUserName(username);
        request.setPolicyName(policy.getPolicyName());
        request.setPolicyType(policy.getPolicyType());
        try {
            client.getAcsResponse(request);
        } catch (ClientException e) {
            log.error("移除RAM用户权限失败，用户名为：{}", username, e);
        }
    }

    private List<ListGroupsForUserResponse.Group> listGroupsForUser(IAcsClient client, String username) {
        ListGroupsForUserRequest request = new ListGroupsForUserRequest();
        request.setUserName(username);
        try {
            ListGroupsForUserResponse response = client.getAcsResponse(request);
            return response == null ? Collections.emptyList() : response.getGroups();
        } catch (ClientException e) {
            log.error("获取RAM用户用户组失败，用户名为：{}", username, e);
            return Collections.emptyList();
        }
    }

    private void removeUserFromGroup(IAcsClient client, String username, ListGroupsForUserResponse.Group group) {
        RemoveUserFromGroupRequest request = new RemoveUserFromGroupRequest();
        request.setUserName(username);
        request.setGroupName(group.getGroupName());
        try {
            client.getAcsResponse(request);
        } catch (ClientException e) {
            log.error("移除RAM用户用户组失败，用户名为：{}", username, e);
        }
    }

    /**
     * 开通控制台登录
     *
     * @param ocUser
     * @param passwordResetRequired 需要密码重置
     * @return
     */
    private BusinessWrapper<CreateLoginProfileResponse.LoginProfile> createLoginProfile(IAcsClient client, OcUser ocUser, boolean passwordResetRequired) {
        CreateLoginProfileRequest request = new CreateLoginProfileRequest();
        request.setUserName(ocUser.getUsername());
        request.setPassword(ocUser.getPassword());
        request.setPasswordResetRequired(passwordResetRequired);
        return createLoginProfileResponse(client, request);
    }

    private BusinessWrapper<CreateLoginProfileResponse.LoginProfile> createLoginProfileResponse(IAcsClient client, CreateLoginProfileRequest request) {
        try {
            return new BusinessWrapper(client.getAcsResponse(request).getLoginProfile());
        } catch (ClientException e) {
            return new BusinessWrapper(10000, e.getMessage());
        }
    }


    /**
     * 查询账户下所有用户
     *
     * @param aliyunAccount
     * @return
     */
    public List<ListUsersResponse.User> getUsers(AliyunCoreConfig.AliyunAccount aliyunAccount) {
        IAcsClient client = acqAcsClient(aliyunAccount);
        List<ListUsersResponse.User> users = Lists.newArrayList();
        String marker = "";
        while (true) {
            ListUsersResponse responseMarker = listUsers(client, marker);
            if (responseMarker.getUsers() == null)
                return users;
            users.addAll(responseMarker.getUsers());
            if (!responseMarker.getIsTruncated()) {
                return users;
            } else {
                marker = responseMarker.getMarker();
            }
        }
    }

    private ListUsersResponse listUsers(IAcsClient client, String marker) {
        ListUsersRequest request = new ListUsersRequest();
        request.setMaxItems(MAX_ITEMS);
        if (!StringUtils.isEmpty(marker))
            request.setMarker(marker);
        try {
            return client.getAcsResponse(request);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 查询用户激活的AccessKey
     *
     * @param username
     * @return
     */
    public List<ListAccessKeysResponse.AccessKey> getUserAccessKeys(AliyunCoreConfig.AliyunAccount aliyunAccount, String username) {
        log.error("查询RAM用户 {} AK", username);
        ListAccessKeysRequest request = new ListAccessKeysRequest();
        request.setUserName(username);
        IAcsClient client = acqAcsClient(aliyunAccount);
        try {
            return client.getAcsResponse(request)
                    .getAccessKeys().stream().filter(e -> e.getStatus().equalsIgnoreCase("Active")).collect(Collectors.toList());
        } catch (ClientException e) {
            log.error("查询RAM用户AK错误: {}", e.getMessage());
        }
        return Collections.EMPTY_LIST;
    }


    /**
     * 更新账户基本信息
     *
     * @param aliyunAccount
     * @param ocUser
     * @return
     */
    public BusinessWrapper<UpdateUserResponse.User> updateRamUser(AliyunCoreConfig.AliyunAccount aliyunAccount, OcUser ocUser) {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setUserName(ocUser.getUsername());
        request.setNewUserName(ocUser.getUsername());
        request.setNewDisplayName(ocUser.getDisplayName());
        if (RegexUtils.isPhone(ocUser.getPhone()))
            request.setNewMobilePhone("86-" + ocUser.getPhone());
        if (!StringUtils.isEmpty(ocUser.getEmail()))
            request.setNewEmail(ocUser.getEmail());
        IAcsClient client = acqAcsClient(aliyunAccount);
        try {
            return new BusinessWrapper(client.getAcsResponse(request).getUser());
        } catch (ClientException e) {
            return new BusinessWrapper(10000, e.getMessage());
        }
    }

    /**
     * 查询RAM子账户
     *
     * @param aliyunAccount
     * @param ocUser
     * @return
     */
    public BusinessWrapper<GetUserResponse.User> getRamUser(AliyunCoreConfig.AliyunAccount aliyunAccount, OcUser ocUser) {
        GetUserRequest request = new GetUserRequest();
        request.setUserName(ocUser.getUsername());
        IAcsClient client = acqAcsClient(aliyunAccount);
        try {
            return new BusinessWrapper(client.getAcsResponse(request).getUser());
        } catch (ClientException e) {
            return new BusinessWrapper(10000, e.getMessage());
        }
    }

}
