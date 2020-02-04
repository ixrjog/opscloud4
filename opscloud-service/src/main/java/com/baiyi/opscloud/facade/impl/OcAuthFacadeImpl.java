package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.facade.OcAuthFacade;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2020/1/16 10:12 上午
 * @Version 1.0
 */
@Component
public class OcAuthFacadeImpl implements OcAuthFacade {

    @Override
    public BusinessWrapper<Boolean> checkUserHasResourceAuthorize(String token, String resourceName) {
//        ResourceDO resourceDO = authDao.getResourceByResourceName(resourceName);
//        if (resourceDO == null) {
//            return new BusinessWrapper<>(ErrorCode.resourceNotExist);
//        }
//
//        if (resourceDO.getNeedAuth() == 1) {
//            return new BusinessWrapper<>(true);
//        }
//
//        if (StringUtils.isEmpty(token)) {
//            return new BusinessWrapper<>(ErrorCode.requestNoToken);
//        }
//
//        //检查token是否失效
//        if (authDao.checkTokenHasInvalid(token) == 0) {
//            return new BusinessWrapper<>(ErrorCode.tokenInvalid);
//        }
//
//        int nums = authDao.checkUserHasResourceAuthorize(token, resourceName);
//        if (nums == 0) {
//            return new BusinessWrapper<>(ErrorCode.authenticationFailure);
//        } else {
//            return new BusinessWrapper<>(true);
//        }
        return null;
    }

    @Override
    public String getUserByToken(String token) {
        return token;
    }
}
