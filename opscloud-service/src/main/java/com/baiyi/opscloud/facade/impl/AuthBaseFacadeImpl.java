package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.common.util.SessionUtils;
import com.baiyi.opscloud.common.util.TimeUtils;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.facade.AuthBaseFacade;
import com.baiyi.opscloud.service.auth.OcAuthResourceService;
import com.baiyi.opscloud.service.auth.OcAuthRoleResourceService;
import com.baiyi.opscloud.service.auth.OcAuthRoleService;
import com.baiyi.opscloud.service.auth.OcAuthUserRoleService;
import com.baiyi.opscloud.service.user.OcUserApiTokenService;
import com.baiyi.opscloud.service.user.OcUserService;
import com.baiyi.opscloud.service.user.OcUserTokenService;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/16 10:12 上午
 * @Version 1.0
 */
@Component
public class AuthBaseFacadeImpl implements AuthBaseFacade {

    public static final String SUPER_ADMIN = "super_admin";

    @Resource
    private OcAuthResourceService ocAuthResourceService;

    @Resource
    private OcUserTokenService ocUserTokenService;

    @Resource
    private OcUserApiTokenService ocUserApiTokenService;

    @Resource
    private OcAuthRoleService ocAuthRoleService;

    @Resource
    private OcAuthRoleResourceService ocAuthRoleResourceService;

    @Resource
    private OcAuthUserRoleService ocAuthUserRoleService;

    @Resource
    private StringEncryptor stringEncryptor;

    @Resource
    private OcUserService ocUserService;

    @Override
    public BusinessWrapper<Boolean> checkUserHasResourceAuthorize(String token, String resourceName) {

        OcAuthResource ocAuthResource = ocAuthResourceService.queryOcAuthResourceByName(resourceName);
        if (ocAuthResource == null) // 资源不存在
            return new BusinessWrapper<>(ErrorEnum.AUTHENTICATION_RESOURCE_NOT_EXIST);

        if (ocAuthResource.getNeedAuth() == 0) // 此接口不需要鉴权
            return BusinessWrapper.SUCCESS;

        if (StringUtils.isEmpty(token))  // request请求中没有Token
            return new BusinessWrapper<>(ErrorEnum.AUTHENTICATION_REQUEST_NO_TOKEN);

        OcUserToken ocUserToken = ocUserTokenService.queryOcUserTokenByTokenAndValid(token);
        // 检查token是否失效
        if (ocUserToken == null) {
            // 从ApiToken校验
            return checkUserApiHasResourceAuthorize(token, resourceName, new BusinessWrapper<>(ErrorEnum.AUTHENTICATION_TOKEN_INVALID));
        }
        // 校验用户是否可以访问资源路径
        if (ocUserTokenService.checkUserHasResourceAuthorize(token, resourceName) == 0) {
            if (ocUserTokenService.checkUserHasRole(token, SUPER_ADMIN) == 0) {
                return new BusinessWrapper<>(ErrorEnum.AUTHENTICATION_FAILUER);
            } else {
                // 授权资源到角色
                grantSuperAdminResource(resourceName);
            }
        }
        return BusinessWrapper.SUCCESS;
    }

    private void grantSuperAdminResource(String resourceName) {
        OcAuthResource ocAuthResource = ocAuthResourceService.queryOcAuthResourceByName(resourceName);
        if (ocAuthResource == null) return;
        OcAuthRole ocAuthRole = ocAuthRoleService.queryOcAuthRoleByName(resourceName);
        if (ocAuthRole == null) return;

        OcAuthRoleResource ocAuthRoleResource = new OcAuthRoleResource();
        ocAuthRoleResource.setResourceId(ocAuthResource.getId());
        ocAuthRoleResource.setRoleId(ocAuthRole.getId());

        ocAuthRoleResourceService.addOcAuthRoleResource(ocAuthRoleResource);
    }

    private BusinessWrapper<Boolean> checkUserApiHasResourceAuthorize(String token, String resourceName, BusinessWrapper<Boolean> wrapper) {
        OcUserApiToken ocUserApiToken = ocUserApiTokenService.queryOcUserApiTokenByTokenAndValid(token);
        if (ocUserApiToken == null) return wrapper; // 校验失败返回上级错误
        if (TimeUtils.calculateDateExpired(ocUserApiToken.getExpiredTime())) {
            // apiToken过期 设置失效
            ocUserApiToken.setValid(false);
            ocUserApiTokenService.updateOcUserApiToken(ocUserApiToken);
            return new BusinessWrapper<>(ErrorEnum.AUTHENTICATION_API_TOKEN_INVALID);
        }
        if (ocUserApiTokenService.checkUserHasResourceAuthorize(token, resourceName) == 0) {
            return new BusinessWrapper<>(ErrorEnum.AUTHENTICATION_API_FAILUER);
        } else {
            SessionUtils.setUsername(ocUserApiToken.getUsername());
            return BusinessWrapper.SUCCESS;
        }
    }

    @Override
    public String getUserByToken(String token) {
        OcUserToken ocUserToken = ocUserTokenService.queryOcUserTokenByTokenAndValid(token);
        if (ocUserToken == null)
            return "";
        return ocUserToken.getUsername();
    }

    /**
     * 吊销Token
     *
     * @param username
     */
    @Override
    public void revokeUserToken(String username) {
        List<OcUserToken> tokens = ocUserTokenService.queryOcUserTokenByUsername(username);
        if (!CollectionUtils.isEmpty(tokens))
            tokens.forEach(e -> ocUserTokenService.updateOcUserTokenInvalid(e));
    }

    /**
     * 设置用户Token
     *
     * @param username
     * @param token
     */
    @Override
    public void setUserToken(String username, String token) {
        revokeUserToken(username); // 吊销Token
        OcUserToken ocUserToken = new OcUserToken();
        ocUserToken.setValid(true);
        ocUserToken.setUsername(username);
        ocUserToken.setToken(token);
        ocUserTokenService.addOcUserToken(ocUserToken);
    }

    @Override
    public void setOcUserPassword(OcUser ocUser, String password) {
        if (!StringUtils.isEmpty(ocUser.getPassword())) {
            String pw = stringEncryptor.decrypt(ocUser.getPassword());
            if (pw.equals(password))
                return;
        }
        ocUser.setPassword(stringEncryptor.encrypt(password));
        ocUserService.updateOcUser(ocUser);
    }

    @Override
    public void authorizedAdminAllRole(OcUser ocUser) {
        ocAuthRoleService.queryAllOcAuthRole().forEach(e -> {
            try{
                OcAuthUserRole ocAuthUserRole = new OcAuthUserRole();
                ocAuthUserRole.setRoleId(e.getId());
                ocAuthUserRole.setUsername(ocUser.getUsername());
                ocAuthUserRoleService.addOcAuthUserRole(ocAuthUserRole);
            }catch (Exception ignored){
            }
        });
    }

}
