package com.baiyi.opscloud.datasource.facade.am.base;

import com.baiyi.opscloud.common.exception.am.CreateUserException;
import com.baiyi.opscloud.domain.param.user.UserAmParam;

/**
 * @Author baiyi
 * @Date 2022/2/10 6:32 PM
 * @Version 1.0
 */
public interface IAccessManagementProcessor {

    /**
     * 创建账户
     * @param createUser
     * @throws CreateUserException
     */
    void createUser(UserAmParam.CreateUser createUser) throws CreateUserException;

    /**
     * 授权策略
     * @param grantPolicy
     */
    void grantPolicy(UserAmParam.GrantPolicy grantPolicy);

    /**
     * 撤销策略
     * @param revokePolicy
     */
    void revokePolicy(UserAmParam.RevokePolicy revokePolicy);

    /**
     * 更新用户配置文件
     * @param updateLoginProfile
     */
    void updateLoginProfile(UserAmParam.UpdateLoginProfile updateLoginProfile);

    /**
     * 获取数据源类型
     * @return
     */
    String getDsType();

}
