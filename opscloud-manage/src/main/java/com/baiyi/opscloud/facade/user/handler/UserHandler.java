package com.baiyi.opscloud.facade.user.handler;

import com.baiyi.opscloud.common.configuration.properties.OpscloudConfigurationProperties;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.generator.opscloud.UserGroup;
import com.baiyi.opscloud.domain.param.auth.AuthUserRoleParam;
import com.baiyi.opscloud.domain.param.user.UserBusinessPermissionParam;
import com.baiyi.opscloud.facade.auth.AuthFacade;
import com.baiyi.opscloud.facade.user.UserPermissionFacade;
import com.baiyi.opscloud.service.auth.AuthRoleService;
import com.baiyi.opscloud.service.user.UserGroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2022/4/13 13:30
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserHandler {

    private final OpscloudConfigurationProperties opscloudConfiguration;

    private final AuthFacade authFacade;

    private final AuthRoleService authRoleService;

    private final UserPermissionFacade permissionFacade;

    private final UserGroupService userGroupService;

    public void postCreateUserHandle(User newUser) {
        if (opscloudConfiguration.getCreateUser() == null) {
            return;
        }
        final OpscloudConfigurationProperties.CreateUser createUser = opscloudConfiguration.getCreateUser();
        if (!CollectionUtils.isEmpty(createUser.getRoles())) {
            initializeUserRoles(newUser.getUsername(), createUser.getRoles());
        }
        if (!CollectionUtils.isEmpty(createUser.getUserGroups())) {
            initializeUserGroups(newUser.getId(), createUser.getUserGroups());
        }
    }

    private void initializeUserRoles(String username, List<String> roles) {
        try {
            List<Integer> roleIds = roles.stream().map(r -> authRoleService.getByRoleName(r).getId()).collect(Collectors.toList());
            AuthUserRoleParam.UpdateUserRole updateUserRole = AuthUserRoleParam.UpdateUserRole.builder()
                    .username(username)
                    .roleIds(roleIds)
                    .build();
            authFacade.updateUserRole(updateUserRole);
        } catch (Exception e) {
            log.error("初始化用户角色错误: username={}, {}", username, e.getMessage());
        }
    }

    private void initializeUserGroups(Integer userId, List<String> groups) {
        UserBusinessPermissionParam.UserBusinessPermission userBusinessPermission = UserBusinessPermissionParam.UserBusinessPermission.builder()
                .businessType(BusinessTypeEnum.USERGROUP.getType())
                .userId(userId)
                .build();
        try {
            groups.forEach(g -> {
                UserGroup userGroup = userGroupService.getByName(g);
                if (userGroup == null) {
                    return;
                }
                userBusinessPermission.setBusinessId(userGroup.getId());
                permissionFacade.grantUserBusinessPermission(userBusinessPermission);
            });
        } catch (Exception e) {
            log.error("初始化用户的用户组错误: userId={}, {}", userId, e.getMessage());
        }
    }

}