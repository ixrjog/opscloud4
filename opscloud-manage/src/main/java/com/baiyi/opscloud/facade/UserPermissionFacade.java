package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.generator.opscloud.OcUserPermission;
import com.baiyi.opscloud.domain.vo.user.OcUserVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/25 7:11 下午
 * @Version 1.0
 */
public interface UserPermissionFacade {

    void syncUserBusinessPermission(List<OcUserVO.User> userList, int businessType, int businessId);

    void syncUserBusinessPermission(int userId, int businessType, List<Integer> businessIds);

    BusinessWrapper<Boolean> addOcUserPermission(OcUserPermission ocUserPermission);

    BusinessWrapper<Boolean> delOcUserPermission(OcUserPermission ocUserPermission);

    /**
     * 查询用户的访问级别
     * @param ocUser
     * @return
     */
    int getUserAccessLevel(OcUser ocUser);
}
