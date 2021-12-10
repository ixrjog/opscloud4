package com.baiyi.opscloud.datasource.aliyun.ram.drive;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.ram.model.v20150501.*;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.common.exception.common.CommonRuntimeException;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.RegexUtil;
import com.baiyi.opscloud.datasource.aliyun.core.AliyunClient;
import com.baiyi.opscloud.datasource.aliyun.ram.entity.RamPolicy;
import com.baiyi.opscloud.datasource.aliyun.ram.entity.RamUser;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
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

@Component
@RequiredArgsConstructor
public class AliyunRamUserDrive {

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
    public RamUser.User createUser(String regionId, AliyunConfig.Aliyun aliyun, User user, boolean createLoginProfile) {
        try {
            CreateUserResponse.User createUser = createUser(regionId, aliyun, user);
            if (createLoginProfile)
                createLoginProfile(regionId, aliyun, user, NO_PASSWORD_RESET_REQUIRED);
            return BeanCopierUtil.copyProperties(createUser, RamUser.User.class);
        } catch (ClientException e) {
            throw new CommonRuntimeException("创建RAM用户错误: " + e.getMessage());
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
    private CreateLoginProfileResponse.LoginProfile createLoginProfile(String regionId, AliyunConfig.Aliyun aliyun, User user, boolean passwordResetRequired)
            throws ClientException {
        CreateLoginProfileRequest request = new CreateLoginProfileRequest();
        request.setUserName(user.getUsername());
        request.setPassword(user.getPassword());
        request.setPasswordResetRequired(passwordResetRequired);
        return aliyunClient.getAcsResponse(regionId, aliyun, request).getLoginProfile();
    }

    private CreateUserResponse.User createUser(String regionId, AliyunConfig.Aliyun aliyun, User user)
            throws ClientException {
        CreateUserRequest request = new CreateUserRequest();
        request.setUserName(user.getUsername());
        request.setDisplayName(user.getDisplayName());
        if (RegexUtil.isPhone(user.getPhone()))
            request.setMobilePhone("86-" + user.getPhone());
        if (!StringUtils.isEmpty(user.getEmail()))
            request.setEmail(user.getEmail());
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
            } while (Strings.isNotBlank(marker));
        } catch (ClientException e) {
            e.printStackTrace();
        }

        return BeanCopierUtil.copyListProperties(userList, RamUser.User.class);
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
                  BeanCopierUtil.copyListProperties(response.getPolicies(), RamPolicy.Policy.class)  ;
        } catch (ClientException e) {
            return Collections.emptyList();
        }
    }

}
