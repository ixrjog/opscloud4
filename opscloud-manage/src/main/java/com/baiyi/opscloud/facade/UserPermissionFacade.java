package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.generator.opscloud.OcUserPermission;
import com.baiyi.opscloud.domain.vo.user.UserVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/25 7:11 下午
 * @Version 1.0
 */
public interface UserPermissionFacade {

    /**
     * 查询所有的授权关系
     * @param businessType
     * @param businessId
     * @return
     */
    List<OcUserPermission> queryPermissions(int businessType, int businessId);

    void syncUserBusinessPermission(List<UserVO.User> userList, int businessType, int businessId);

    void syncUserBusinessPermission(int userId, int businessType, List<Integer> businessIds);

    BusinessWrapper<Boolean> addOcUserPermission(OcUserPermission ocUserPermission);

    BusinessWrapper<Boolean> delOcUserPermission(OcUserPermission ocUserPermission);

    boolean tryUserBusinessPermission(int userId, int businessType, int businessId);

    /**
     * 查询用户的访问级别
     * @param ocUser
     * @return
     */
    int getUserAccessLevel(OcUser ocUser);

    BusinessWrapper<Boolean> checkAccessLevel(OcUser ocUser,int accessLevel);

    BusinessWrapper<Boolean> checkAccessLevelIsHigherDev(String username);
}
