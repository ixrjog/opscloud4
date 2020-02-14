package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.OcAuthResource;
import com.baiyi.opscloud.domain.generator.OcUserToken;
import com.baiyi.opscloud.facade.OcAuthFacade;
import com.baiyi.opscloud.service.auth.OcAuthResourceService;
import com.baiyi.opscloud.service.user.OcUserTokenService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/16 10:12 上午
 * @Version 1.0
 */
@Component
public class OcAuthFacadeImpl implements OcAuthFacade {

    @Resource
    private OcAuthResourceService ocAuthResourceService;

    @Resource
    private OcUserTokenService ocUserTokenService;

    @Override
    public BusinessWrapper<Boolean> checkUserHasResourceAuthorize(String token, String resourceName) {

        OcAuthResource ocAuthResource = ocAuthResourceService.queryOcAuthResourceByName(resourceName);
        if (ocAuthResource == null) // 资源不存在
            return new BusinessWrapper<>(ErrorEnum.AUTHENTICATION_RESOURCE_NOT_EXIST);

        if (ocAuthResource.getNeedAuth() == 0) // 此接口不需要鉴权
            return new BusinessWrapper<>(true);

        if (StringUtils.isEmpty(token))  // request请求中没有Token
            return new BusinessWrapper<>(ErrorEnum.AUTHENTICATION_REQUEST_NO_TOKEN);

        OcUserToken ocUserToken = ocUserTokenService.queryOcUserTokenByTokenAndValid(token);
        if (ocUserToken == null) // 检查token是否失效
            return new BusinessWrapper<>(ErrorEnum.AUTHENTICATION_TOKEN_INVALID);

        // 校验用户是否可以访问资源路径
        int nums = ocUserTokenService.checkUserHasResourceAuthorize(token, resourceName);
        if (nums == 0) {
            return new BusinessWrapper<>(ErrorEnum.AUTHENTICATION_FAILUER);
        } else {
            return new BusinessWrapper<>(true);
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
     * 设置用户Token
     *
     * @param username
     * @param token
     */
    @Override
    public void setUserToken(String username, String token) {
        List<OcUserToken> ocUserTokenList = ocUserTokenService.queryOcUserTokenByUsername(username);
        // 吊销Token
        if (!ocUserTokenList.isEmpty())
            for (OcUserToken ocUserToken : ocUserTokenList) ocUserTokenService.updateOcUserTokenInvalid(ocUserToken);
        OcUserToken ocUserToken = new OcUserToken();
        ocUserToken.setValid(true);
        ocUserToken.setUsername(username);
        ocUserToken.setToken(token);
        ocUserTokenService.addOcUserToken(ocUserToken);
    }
}
