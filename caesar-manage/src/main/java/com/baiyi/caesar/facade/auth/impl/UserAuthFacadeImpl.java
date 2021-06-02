package com.baiyi.caesar.facade.auth.impl;

import com.baiyi.caesar.common.exception.auth.AuthRuntimeException;
import com.baiyi.caesar.common.util.SessionUtil;
import com.baiyi.caesar.domain.ErrorEnum;
import com.baiyi.caesar.domain.generator.caesar.AuthResource;
import com.baiyi.caesar.domain.generator.caesar.AuthRole;
import com.baiyi.caesar.domain.generator.caesar.User;
import com.baiyi.caesar.domain.generator.caesar.UserToken;
import com.baiyi.caesar.domain.param.auth.LoginParam;
import com.baiyi.caesar.facade.auth.AuthFacade;
import com.baiyi.caesar.facade.auth.UserAuthFacade;
import com.baiyi.caesar.facade.auth.UserTokenFacade;
import com.baiyi.caesar.service.auth.AuthResourceService;
import com.baiyi.caesar.service.auth.AuthRoleService;
import com.baiyi.caesar.service.user.UserService;
import com.baiyi.caesar.service.user.UserTokenService;
import com.baiyi.caesar.domain.vo.auth.AuthRoleResourceVO;
import com.baiyi.caesar.domain.vo.auth.LogVO;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

import static com.baiyi.caesar.common.base.Global.SUPER_ADMIN;

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
        if (user == null || !user.getIsActive())
            throw new AuthRuntimeException(ErrorEnum.AUTH_USER_LOGIN_FAILUER);
        if (loginParam.isEmptyPassword()) {
            if (StringUtils.isEmpty(user.getPassword()))
                return userTokenFacade.userLogin(user);     // 空密码登录成功
            throw new AuthRuntimeException(ErrorEnum.AUTH_USER_LOGIN_FAILUER);
        }
        // 校验密码
        if (verifyPassword(loginParam.getPassword(), user.getPassword()))
            return userTokenFacade.userLogin(user);
        throw new AuthRuntimeException(ErrorEnum.AUTH_USER_LOGIN_FAILUER); // 登录失败
    }

    /**
     * 校验密码
     *
     * @param password         密码
     * @param encryptdPassword 加密的密码
     * @return
     */
    private boolean verifyPassword(String password, String encryptdPassword) {
        return password.equals(stringEncryptor.decrypt(encryptdPassword));
    }


}
