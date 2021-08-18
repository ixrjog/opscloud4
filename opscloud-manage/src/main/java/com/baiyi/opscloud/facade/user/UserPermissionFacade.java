package com.baiyi.opscloud.facade.user;

import com.baiyi.opscloud.domain.vo.user.UserPermissionVO;

/**
 * @Author baiyi
 * @Date 2021/5/27 2:38 下午
 * @Version 1.0
 */
public interface UserPermissionFacade {

    void revokeUserBusinessPermission(UserPermissionVO.UserBusinessPermission userBusinessPermission);

    void grantUserBusinessPermission(UserPermissionVO.UserBusinessPermission userBusinessPermission);

    void settUserBusinessPermission(int id);

    int getUserAccessLevel(String username);

    void revokeUserPermissionByBusiness(int businessType, int businessId);
}
