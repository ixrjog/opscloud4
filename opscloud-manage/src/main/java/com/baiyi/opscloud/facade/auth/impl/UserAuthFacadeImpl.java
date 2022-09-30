package com.baiyi.opscloud.facade.auth.impl;

import com.baiyi.opscloud.common.exception.auth.AuthCommonException;
import com.baiyi.opscloud.common.util.SessionUtil;
import com.baiyi.opscloud.datasource.manager.DsAuthManager;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.annotation.PermitEmptyPasswords;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.param.auth.LoginParam;
import com.baiyi.opscloud.domain.vo.auth.AuthRoleResourceVO;
import com.baiyi.opscloud.domain.vo.auth.LogVO;
import com.baiyi.opscloud.facade.auth.AuthFacade;
import com.baiyi.opscloud.facade.auth.PlatformAuthHelper;
import com.baiyi.opscloud.facade.auth.UserAuthFacade;
import com.baiyi.opscloud.facade.auth.UserTokenFacade;
import com.baiyi.opscloud.facade.auth.mfa.MfaAuthHelper;
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

    private final MfaAuthHelper mfaAuthHelper;

    private final AuthPlatformLogService authPlatformLogService;

    private final PlatformAuthHelper platformAuthHelper;

    @Override
    public void verifyUserHasResourcePermissionWithToken(String token, String resourceName) {
        AuthResource authResource = authResourceService.queryByName(resourceName);
        if (authResource == null)
            throw new AuthCommonException(ErrorEnum.AUTHENTICATION_RESOURCE_NOT_EXIST);

        if (!authResource.getNeedAuth())
            return; // 此接口不需要鉴权

        if (StringUtils.isEmpty(token))
            throw new AuthCommonException(ErrorEnum.AUTHENTICATION_REQUEST_NO_TOKEN);

        UserToken userToken = userTokenService.getByVaildToken(token);
        if (userToken == null)
            throw new AuthCommonException(ErrorEnum.AUTHENTICATION_TOKEN_INVALID);
        // 设置会话用户
        SessionUtil.setUserToken(userToken);
        // 校验用户是否可以访问资源路径
        if (userTokenService.checkUserHasResourceAuthorize(token, resourceName) == 0) {
            if (userTokenService.checkUserHasRole(token, SUPER_ADMIN) == 0) {
                throw new AuthCommonException(ErrorEnum.AUTHENTICATION_FAILURE);
            } else {
                grantRoleResource(authResource);
            }
        }
    }

    @Override
    public void verifyUserHasResourcePermissionWithAccessToken(String accessToken, String resourceName) {
        AuthResource authResource = authResourceService.queryByName(resourceName);
        if (authResource == null)
            throw new AuthCommonException(ErrorEnum.AUTHENTICATION_RESOURCE_NOT_EXIST);
        if (!authResource.getNeedAuth())
            return; // 此接口不需要鉴权
        if (StringUtils.isEmpty(accessToken))
            throw new AuthCommonException(ErrorEnum.AUTHENTICATION_REQUEST_NO_TOKEN);
        AccessToken token = accessTokenService.getByToken(accessToken);
        if (token == null)
            throw new AuthCommonException(ErrorEnum.AUTHENTICATION_TOKEN_INVALID);
        // 设置会话用户
        SessionUtil.setUsername(token.getUsername());
        // 校验用户是否可以访问资源路径
        if (accessTokenService.checkUserHasResourceAuthorize(accessToken, resourceName) == 0) {
            throw new AuthCommonException(ErrorEnum.AUTHENTICATION_FAILURE);
        }
    }

    // 授权资源到SA角色
    private void grantRoleResource(AuthResource authResource) {
        AuthRole authRole = authRoleService.getByRoleName(SUPER_ADMIN);
        if (authRole == null) return;

        AuthRoleResourceVO.RoleResource roleResource = AuthRoleResourceVO.RoleResource.builder()
                .resourceId(authResource.getId())
                .roleId(authRole.getId())
                .build();
        authFacade.addRoleResource(roleResource);
    }

    @Override
    @PermitEmptyPasswords // 允许空密码登录
    public LogVO.Login login(LoginParam.Login loginParam) {
        User user = userService.getByUsername(loginParam.getUsername());
        // 尝试使用authProvider 认证
        if (dsAuthManager.tryLogin(user, loginParam)) {
            boolean bindMfa = false;
            if (user.getMfa()) {
                mfaAuthHelper.verify(user, loginParam);
            } else {
                bindMfa = mfaAuthHelper.tryBind(user, loginParam);
            }
            // 更新用户登录信息
            user.setPassword(stringEncryptor.encrypt(loginParam.getPassword()));
            // 更新用户登录时间
            user.setLastLogin(new Date());
            if (bindMfa) {
                user.setMfa(true);
            }
            userService.updateLogin(user);
            return userTokenFacade.userLogin(user);
        } else {
            throw new AuthCommonException(ErrorEnum.AUTH_USER_LOGIN_FAILURE);
        }
    }

    @Override
    public LogVO.Login loginWithPlatform(LoginParam.PlatformLogin loginParam) {
        AuthPlatform authPlatform = platformAuthHelper.verify(loginParam);
        User user = userService.getByUsername(loginParam.getUsername());
        // 尝试使用authProvider 认证
        if (dsAuthManager.tryLogin(user, loginParam)) {
            if (user.getMfa()) {
                try {
                    mfaAuthHelper.verify(user, loginParam);
                } catch (AuthCommonException e) {
                    recordLog(authPlatform, loginParam, ErrorEnum.AUTH_USER_LOGIN_FAILURE);
                    throw new AuthCommonException(e.getMessage());
                }
            }
            recordLog(authPlatform, loginParam, ErrorEnum.OK);
            return LogVO.Login.builder()
                    .name(loginParam.getUsername())
                    .build();
        } else {
            recordLog(authPlatform, loginParam, ErrorEnum.AUTH_USER_LOGIN_FAILURE);
            throw new AuthCommonException(ErrorEnum.AUTH_USER_LOGIN_FAILURE);
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
        log.info("用户登出: username = {}", SessionUtil.getUsername());
        userTokenFacade.revokeUserToken(SessionUtil.getUsername());
    }

}
