package com.baiyi.opscloud.facade.auth.impl;

import com.baiyi.opscloud.common.exception.auth.AuthenticationException;
import com.baiyi.opscloud.common.holder.SessionHolder;
import com.baiyi.opscloud.datasource.manager.DsAuthManager;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.annotation.PermitEmptyPasswords;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.param.auth.AuthRoleResourceParam;
import com.baiyi.opscloud.domain.param.auth.LoginParam;
import com.baiyi.opscloud.domain.vo.auth.LogVO;
import com.baiyi.opscloud.facade.auth.AuthFacade;
import com.baiyi.opscloud.facade.auth.PlatformAuthValidator;
import com.baiyi.opscloud.facade.auth.UserAuthFacade;
import com.baiyi.opscloud.facade.auth.UserTokenFacade;
import com.baiyi.opscloud.facade.auth.mfa.MfaValidator;
import com.baiyi.opscloud.service.auth.AuthPlatformLogService;
import com.baiyi.opscloud.service.auth.AuthResourceService;
import com.baiyi.opscloud.service.auth.AuthRoleService;
import com.baiyi.opscloud.service.user.AccessTokenService;
import com.baiyi.opscloud.service.user.UserService;
import com.baiyi.opscloud.service.user.UserTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.baiyi.opscloud.common.base.Global.SUPER_ADMIN;

/**
 * @Author baiyi
 * @Date 2021/5/14 4:07 下午
 * @Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserAuthFacadeImpl implements UserAuthFacade {

    private final UserTokenService userTokenService;

    private final AccessTokenService accessTokenService;

    private final AuthResourceService authResourceService;

    private final AuthFacade authFacade;

    private final AuthRoleService authRoleService;

    private final UserService userService;

    private final UserTokenFacade userTokenFacade;

    private final StringEncryptor stringEncryptor;

    private final DsAuthManager dsAuthManager;

    private final MfaValidator mfaValidator;

    private final AuthPlatformLogService authPlatformLogService;

    private final PlatformAuthValidator platformAuthValidator;

    @Override
    public void verifyUserHasResourcePermissionWithToken(String token, String resourceName) {
        AuthResource authResource = authResourceService.queryByName(resourceName);
        if (authResource == null) {
            throw new AuthenticationException(ErrorEnum.AUTHENTICATION_RESOURCE_NOT_EXIST);
        }

        if (!authResource.getNeedAuth()) {
            return; // 此接口不需要鉴权
        }

        if (StringUtils.isEmpty(token)) {
            throw new AuthenticationException(ErrorEnum.AUTHENTICATION_REQUEST_NO_TOKEN);
        }

        UserToken userToken = userTokenService.getByValidToken(token);
        if (userToken == null) {
            throw new AuthenticationException(ErrorEnum.AUTHENTICATION_TOKEN_INVALID);
        }
        // 设置会话用户
        SessionHolder.setUserToken(userToken);
        // 校验用户是否可以访问资源路径
        if (userTokenService.checkUserHasResourceAuthorize(token, resourceName) == 0) {
            if (userTokenService.checkUserHasRole(token, SUPER_ADMIN) == 0) {
                throw new AuthenticationException(ErrorEnum.AUTHENTICATION_FAILURE);
            } else {
                grantRoleResource(authResource);
            }
        }
    }

    @Override
    public void verifyUserHasResourcePermissionWithAccessToken(String accessToken, String resourceName) {
        AuthResource authResource = authResourceService.queryByName(resourceName);
        if (authResource == null) {
            throw new AuthenticationException(ErrorEnum.AUTHENTICATION_RESOURCE_NOT_EXIST);
        }
        if (!authResource.getNeedAuth()) {
            return; // 此接口不需要鉴权
        }
        if (StringUtils.isEmpty(accessToken)) {
            throw new AuthenticationException(ErrorEnum.AUTHENTICATION_REQUEST_NO_TOKEN);
        }
        AccessToken token = accessTokenService.getByToken(accessToken);
        if (token == null) {
            throw new AuthenticationException(ErrorEnum.AUTHENTICATION_TOKEN_INVALID);
        }
        // 设置会话用户
        SessionHolder.setUsername(token.getUsername());
        // 校验用户是否可以访问资源路径
        if (accessTokenService.checkUserHasResourceAuthorize(accessToken, resourceName) == 0) {
            throw new AuthenticationException(ErrorEnum.AUTHENTICATION_FAILURE);
        }
    }

    // 授权资源到SA角色
    private void grantRoleResource(AuthResource authResource) {
        AuthRole authRole = authRoleService.getByRoleName(SUPER_ADMIN);
        if (authRole == null) {
            return;
        }

        AuthRoleResourceParam.RoleResource roleResource = AuthRoleResourceParam.RoleResource.builder()
                .resourceId(authResource.getId())
                .roleId(authRole.getId())
                .build();
        authFacade.addRoleResource(roleResource);
    }

    @Override
    @PermitEmptyPasswords
    public LogVO.Login login(LoginParam.Login loginParam) {
        User user = userService.getByUsername(loginParam.getUsername());
        // 尝试使用Provider认证
        if (!dsAuthManager.tryLogin(user, loginParam)) {
            throw new AuthenticationException(ErrorEnum.AUTH_USER_LOGIN_FAILURE);
        }
        boolean lockMfa = false;
        if (user.getMfa()) {
            mfaValidator.verify(user, loginParam);
        } else {
            lockMfa = mfaValidator.tryBind(user, loginParam);
        }
        // 更新用户登录信息
        User saveUser = User.builder()
                .id(user.getId())
                .password(stringEncryptor.encrypt(loginParam.getPassword()))
                .lastLogin(new Date())
                .mfa(lockMfa ? true : null)
                .build();
        userService.updateLogin(saveUser);
        return userTokenFacade.userLogin(user);
    }

    @Override
    public LogVO.Login platformLogin(LoginParam.PlatformLogin loginParam) {
        AuthPlatform authPlatform = platformAuthValidator.verify(loginParam);
        User user = userService.getByUsername(loginParam.getUsername());
        // 尝试使用authProvider 认证
        if (dsAuthManager.tryLogin(user, loginParam)) {
            if (user.getMfa()) {
                try {
                    mfaValidator.verify(user, loginParam);
                } catch (AuthenticationException e) {
                    recordLog(authPlatform, loginParam, ErrorEnum.AUTH_USER_LOGIN_FAILURE);
                    throw new AuthenticationException(e.getMessage());
                }
            }
            recordLog(authPlatform, loginParam, ErrorEnum.OK);
            return LogVO.Login.builder()
                    .name(loginParam.getUsername())
                    .build();
        } else {
            recordLog(authPlatform, loginParam, ErrorEnum.AUTH_USER_LOGIN_FAILURE);
            throw new AuthenticationException(ErrorEnum.AUTH_USER_LOGIN_FAILURE);
        }
    }

    private void recordLog(AuthPlatform authPlatform, LoginParam.PlatformLogin loginParam, ErrorEnum errorEnum) {
        AuthPlatformLog authPlatformLog = AuthPlatformLog.builder()
                .platformId(authPlatform.getId())
                .platformName(authPlatform.getName())
                .username(loginParam.getUsername())
                .otp(StringUtils.isNotBlank(loginParam.getOtp()))
                .result(errorEnum == ErrorEnum.OK)
                .resultMsg(errorEnum.getMessage())
                .build();
        try {
            authPlatformLogService.add(authPlatformLog);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void logout() {
        log.info("User logout: {}", SessionHolder.getUsername());
        userTokenFacade.revokeUserToken(SessionHolder.getUsername());
    }

}
