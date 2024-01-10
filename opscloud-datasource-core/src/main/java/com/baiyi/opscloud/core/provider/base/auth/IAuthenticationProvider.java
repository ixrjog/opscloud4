package com.baiyi.opscloud.core.provider.base.auth;

import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.model.Authorization;

/**
 * @Author baiyi
 * @Date 2021/6/23 9:28 上午
 * @Version 1.0
 */
public interface IAuthenticationProvider {

    /**
     * 登录
     * @param instance
     * @param credential
     * @return
     */
    boolean login(DatasourceInstance instance, Authorization.Credential credential);

}