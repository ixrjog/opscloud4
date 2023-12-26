package com.baiyi.opscloud.facade.user;

import com.baiyi.opscloud.domain.param.user.UserBusinessPermissionParam;

/**
 * @Author baiyi
 * @Date 2021/5/27 2:38 下午
 * @Version 1.0
 */
public interface UserPermissionFacade {

    void revokeByUserId(int userId);

    void revokeUserBusinessPermission(UserBusinessPermissionParam.UserBusinessPermission userBusinessPermission);

    void grantUserBusinessPermission(UserBusinessPermissionParam.UserBusinessPermission userBusinessPermission);

    void setUserBusinessPermission(int id);

    int getUserAccessLevel(String username);

    void revokeUserPermissionByBusiness(int businessType, int businessId);

}