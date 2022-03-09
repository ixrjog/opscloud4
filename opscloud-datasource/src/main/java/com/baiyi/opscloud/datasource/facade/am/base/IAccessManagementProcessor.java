package com.baiyi.opscloud.datasource.facade.am.base;

import com.baiyi.opscloud.domain.param.user.UserAmParam;

/**
 * @Author baiyi
 * @Date 2022/2/10 6:32 PM
 * @Version 1.0
 */
public interface IAccessManagementProcessor {

    void createUser(UserAmParam.CreateUser createUser);

    void grantPolicy(UserAmParam.GrantPolicy grantPolicy);

    void revokePolicy(UserAmParam.RevokePolicy revokePolicy);

    String getDsType();

}
