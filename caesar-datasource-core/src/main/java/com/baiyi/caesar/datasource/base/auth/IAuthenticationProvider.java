package com.baiyi.caesar.datasource.base.auth;

import com.baiyi.caesar.domain.generator.caesar.DatasourceInstance;
import com.baiyi.caesar.domain.model.Authorization;

/**
 * @Author baiyi
 * @Date 2021/6/23 9:28 上午
 * @Version 1.0
 */
public interface IAuthenticationProvider {

    boolean login(DatasourceInstance instance,Authorization.Credential credential);

}
