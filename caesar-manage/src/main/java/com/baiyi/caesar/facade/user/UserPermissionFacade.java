package com.baiyi.caesar.facade.user;

import com.baiyi.caesar.vo.user.UserPermissionVO;

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
}
