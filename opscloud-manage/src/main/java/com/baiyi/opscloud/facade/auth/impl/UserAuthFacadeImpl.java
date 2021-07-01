package com.baiyi.opscloud.facade.auth.impl;

import com.baiyi.opscloud.common.exception.auth.AuthRuntimeException;
import com.baiyi.opscloud.common.util.SessionUtil;
import com.baiyi.opscloud.datasource.manager.AuthProviderManager;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.AuthResource;
import com.baiyi.opscloud.domain.generator.opscloud.AuthRole;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.generator.opscloud.UserToken;
import com.baiyi.opscloud.domain.param.auth.LoginParam;
import com.baiyi.opscloud.domain.vo.auth.AuthRoleResourceVO;
import com.baiyi.opscloud.domain.vo.auth.LogVO;
import com.baiyi.opscloud.facade.auth.AuthFacade;
import com.baiyi.opscloud.facade.auth.UserAuthFacade;
import com.baiyi.opscloud.facade.auth.UserTokenFacade;
import com.baiyi.opscloud.service.auth.AuthResourceService;
import com.baiyi.opscloud.service.auth.AuthRoleService;
import com.baiyi.opscloud.service.user.UserService;
import com.baiyi.opscloud.service.user.UserTokenService;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

import java.util.Date;

import static com.baiyi.opscloud.common.base.Global.SUPER_ADMIN;

/**
 * @Author baiyi
 * @Date 2021/5/14 4:07 下午
 * @Version 1.0
 */
@Service
public class UserAuthFacadeImpl implements UserAuthFacade {

    @Resource
    private UserTokenService userTokenService;

    @Resource
    private AuthResourceService authResourceService;

    @Resource
    private AuthFacade authFacade;

    @Resource
    private AuthRoleService authRoleService;

    @Resource
    private UserService userService;

    @Resource
    private UserTokenFacade userTokenFacade;

    @Resource
    private StringEncryptor stringEncryptor;

    @Resource
    private AuthProviderManager authProviderManager;

    @Override
    public void tryUserHasResourceAuthorize(String token, String resourceName) {
        AuthResource authResource = authResourceService.queryByName(resourceName);
        if (authResource == null)
            throw new AuthRuntimeException(ErrorEnum.AUTHENTICATION_RESOURCE_NOT_EXIST); // 资源不存在

        if (!authResource.getNeedAuth())
            return; // 此接口不需要鉴权

        if (StringUtils.isEmpty(token))
            throw new AuthRuntimeException(ErrorEnum.AUTHENTICATION_REQUEST_NO_TOKEN); // request请求中没有Token

        UserToken userToken = userTokenService.getByVaildToken(token);
        if (userToken == null)
            throw new AuthRuntimeException(ErrorEnum.AUTHENTICATION_TOKEN_INVALID); // Token无效

        SessionUtil.setUserToken(userToken); // 设置会话用户

        // 校验用户是否可以访问资源路径
        if (userTokenService.checkUserHasResourceAuthorize(token, resourceName) == 0) {
            if (userTokenService.checkUserHasRole(token, SUPER_ADMIN) == 0) {
                throw new AuthRuntimeException(ErrorEnum.AUTHENTICATION_FAILUER);
            } else {
                grantRoleResource(authResource);
            }
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
    public LogVO.Login login(LoginParam.Login loginParam) {
        User user = userService.getByUsername(loginParam.getUsername());
        // 判断用户是否有效
        if (user == null || !user.getIsActive())
            throw new AuthRuntimeException(ErrorEnum.AUTH_USER_LOGIN_FAILUER);
        // user.password 为空则允许空密码登录
        if (loginParam.isEmptyPassword()) {
            if (StringUtils.isEmpty(user.getPassword()))
                return userTokenFacade.userLogin(user);     // 空密码登录成功
            throw new AuthRuntimeException(ErrorEnum.AUTH_USER_LOGIN_FAILUER);
        }
        // 尝试使用authProvider 认证
        if (authProviderManager.tryLogin(user, loginParam)) {
            // 更新用户登录信息
            user.setPassword(stringEncryptor.encrypt(loginParam.getPassword()));
            user.setLastLogin(new Date());
            userService.update(user);
            return userTokenFacade.userLogin(user);
        } else {
            throw new AuthRuntimeException(ErrorEnum.AUTH_USER_LOGIN_FAILUER); // 登录失败
        }
    }

}
