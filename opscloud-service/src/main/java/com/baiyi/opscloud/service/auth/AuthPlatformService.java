package com.baiyi.opscloud.service.auth;

import com.baiyi.opscloud.domain.generator.opscloud.AuthPlatform;

/**
 * @Author baiyi
 * @Date 2022/7/26 11:21
 * @Version 1.0
 */
public interface AuthPlatformService {

    AuthPlatform getByName(String name);

}
