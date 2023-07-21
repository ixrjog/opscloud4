package com.baiyi.opscloud.facade.auth;

import com.baiyi.opscloud.domain.generator.opscloud.AuthPlatform;
import com.baiyi.opscloud.domain.param.auth.IAuthPlatform;

/**
 * @Author baiyi
 * @Date 2022/9/8 11:58
 * @Version 1.0
 */
public interface PlatformAuthValidator {

    AuthPlatform verify(IAuthPlatform iAuthPlatform);

}
