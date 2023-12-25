package com.baiyi.opscloud.datasource.aliyun.ram.driver;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.ram.model.v20150501.*;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.common.exception.common.OCException;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.ValidationUtil;
import com.baiyi.opscloud.datasource.aliyun.core.AliyunClient;
import com.baiyi.opscloud.datasource.aliyun.ram.entity.RamPolicy;
import com.baiyi.opscloud.datasource.aliyun.ram.entity.RamUser;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

import static com.baiyi.opscloud.common.base.Global.CREATED_BY;
import static com.baiyi.opscloud.datasource.aliyun.core.SimpleAliyunClient.Query.PAGE_SIZE;

/**
 * @Author 修远
 * @Date 2021/7/2 7:40 下午
 * @Since 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AliyunRamUserDriver {

    public static final boolean NO_PASSWORD_RESET_REQUIRED = false;

    private final AliyunClient aliyunClient;

    /**
     * 创建RAM账户
     *
     * @param regionId
     * @param aliyun
     * @param user
     * @param createLoginProfile
     * @return RamUser.User
     */
    public RamUser.User createUser(String regionId, AliyunConfig.Aliyun aliyun, User user, boolean createLoginProfile, boolean enableMFA) {
        try {
            CreateUserResponse.User createUser = createUser(regionId, aliyun, user);
            if (createLoginProfile) {
                createLoginProfile(regionId, aliyun, user, NO_PASSWORD_RESET_REQUIRED, enableMFA);
            }
            return BeanCopierUtil.copyProperties(createUser, RamUser.User.class);
        } catch (ClientException e) {
            throw new OCException("创建RAM用户错误: {}", e.getMessage());
        }
    }

    /**
     * 开通控制台登录
     *
     * @param regionId
     * @param aliyun
     * @param user
     * @param passwordResetRequired
     * @return
     * @throws ClientException
     */
    private CreateLoginProfileResponse.LoginProfile createLoginProfile(String regionId, AliyunConfig.Aliyun aliyun, User user, boolean passwordResetRequired, boolean mFABindRequired)
            throws ClientException {
        CreateLoginProfileRequest request = new CreateLoginProfileRequest();
        request.setUserName(user.getUsername());
        request.setPassword(user.getPassword());
        request.setPasswordResetRequired(passwordResetRequired);
        request.setMFABindRequired(mFABindRequired);
        return aliyunClient.getAcsResponse(regionId, aliyun, request).getLoginProfile();
    }

    private CreateUserResponse.User createUser(String regionId, AliyunConfig.Aliyun aliyun, User user)
            throws ClientException {
        CreateUserRequest request = new CreateUserRequest();
        request.setUserName(user.getUsername());
        request.setDisplayName(user.getDisplayName());
        if (ValidationUtil.isPhone(user.getPhone())) {
            request.setMobilePhone("86-" + user.getPhone());
        }
        if (!StringUtils.isEmpty(user.getEmail())) {
            request.setEmail(user.getEmail());
        }
        request.setComments(CREATED_BY);
        return aliyunClient.getAcsResponse(regionId, aliyun, request).getUser();
    }

    public List<RamUser.User> listUsers(String regionId, AliyunConfig.Aliyun aliyun) {
        List<ListUsersResponse.User> userList = Lists.newArrayList();
        String marker;
        try {
            ListUsersRequest request = new ListUsersRequest();
            request.setMaxItems(PAGE_SIZE);
            do {
                ListUsersResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
                userList.addAll(response.getUsers());
                marker = response.getMarker();
                request.setMarker(marker);
            } while (StringUtils.isNotBlank(marker));
        } catch (ClientException e) {
            log.error(e.getMessage());
        }
        List<RamUser.User> result = Lists.newArrayList();
        // 获取用户详情
        userList.forEach(e -> {
            try {
                result.add(getUser(regionId, aliyun, e.getUserName()));
            } catch (ClientException ex) {
                log.error(ex.getMessage());
            }

        });
        return result;
    }

    /**
     * 查询 RAM 账户
     *
     * @param regionId
     * @param aliyun
     * @param ramUsername
     * @return
     * @throws ClientException
     */
    public RamUser.User getUser(String regionId, AliyunConfig.Aliyun aliyun, String ramUsername) throws ClientException {
        GetUserRequest request = new GetUserRequest();
        request.setUserName(ramUsername);
        GetUserResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
        if (response == null || response.getUser() == null) {
            return null;
        }
        return BeanCopierUtil.copyProperties(response.getUser(), RamUser.User.class);
    }

    /**
     * 查询 策略授权的所有用户
     *
     * @param regionId
     * @param aliyun
     * @param policyType
     * @param policyName
     * @return ListEntitiesForPolicyResponse.User
     */
    public List<RamUser.User> listUsersForPolicy(String regionId, AliyunConfig.Aliyun aliyun, String policyType, String policyName) {
        ListEntitiesForPolicyRequest request = new ListEntitiesForPolicyRequest();
        request.setPolicyType(policyType);
        request.setPolicyName(policyName);
        try {
            ListEntitiesForPolicyResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
            return response == null ? Collections.emptyList() : BeanCopierUtil.copyListProperties(response.getUsers(), RamUser.User.class);
        } catch (ClientException e) {
            log.error(e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * 查询RAM User 所有的策略
     *
     * @param regionId
     * @param aliyun
     * @param username
     * @return
     */
    public List<RamPolicy.Policy> listPoliciesForUser(String regionId, AliyunConfig.Aliyun aliyun, String username) {
        ListPoliciesForUserRequest request = new ListPoliciesForUserRequest();
        request.setUserName(username);
        try {
            ListPoliciesForUserResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
            return response == null ? Collections.emptyList() :
                    BeanCopierUtil.copyListProperties(response.getPolicies(), RamPolicy.Policy.class);
        } catch (ClientException e) {
            log.error(e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * 删除RAM账户
     *
     * @param regionId
     * @param aliyun
     * @param username
     * @return
     */
    public boolean deleteUser(String regionId, AliyunConfig.Aliyun aliyun, String username) {
        DeleteUserRequest request = new DeleteUserRequest();
        request.setUserName(username);
        try {
            DeleteUserResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
            if (!StringUtils.isEmpty(response.getRequestId())) {
                return true;
            }
        } catch (ClientException e) {
            log.error(e.getMessage());
        }
        return false;
    }

    /**
     * 关闭指定 RAM 用户登录 Web 控制台的功能
     *
     * @param regionId
     * @param aliyun
     * @param username
     * @return
     */
    public boolean deleteLoginProfile(String regionId, AliyunConfig.Aliyun aliyun, String username) {
        DeleteLoginProfileRequest request = new DeleteLoginProfileRequest();
        request.setUserName(username);
        try {
            DeleteLoginProfileResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
            if (!StringUtils.isEmpty(response.getRequestId())) {
                return true;
            }
        } catch (ClientException e) {
            log.error(e.getMessage());
        }
        return false;
    }

    /**
     * 更新 RAM 用户的基本信息
     *
     * @param regionId
     * @param aliyun
     * @param ramUser
     */
    public void updateUser(String regionId, AliyunConfig.Aliyun aliyun, RamUser.User ramUser) {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setUserName(ramUser.getUserName());
        // 修改Email
        if (StringUtils.isNotBlank(ramUser.getEmail())) {
            request.setNewEmail(ramUser.getEmail());
        }
        // 修改显示名
        if (StringUtils.isNotBlank(ramUser.getDisplayName())) {
            request.setNewDisplayName(ramUser.getDisplayName());
        }
        try {
            UpdateUserResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
        } catch (ClientException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 更新RAM用户登录配置
     *
     * @param aliyun
     * @param user
     * @param password
     * @param passwordResetRequired
     * @return
     * @throws ClientException
     */
    public String updateLoginProfile(AliyunConfig.Aliyun aliyun,
                                     com.baiyi.opscloud.domain.generator.opscloud.User user,
                                     String password,
                                     boolean passwordResetRequired) throws ClientException {
        UpdateLoginProfileRequest request = new UpdateLoginProfileRequest();
        request.setUserName(user.getUsername());
        request.setPassword(password);
        request.setPasswordResetRequired(passwordResetRequired);
        UpdateLoginProfileResponse response = aliyunClient.getAcsResponse(aliyun.getRegionId(), aliyun, request);
        return response.getRequestId();
    }

}