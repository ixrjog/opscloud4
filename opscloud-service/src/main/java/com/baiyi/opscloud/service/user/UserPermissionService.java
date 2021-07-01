package com.baiyi.opscloud.service.user;

import com.baiyi.opscloud.domain.generator.opscloud.UserPermission;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/26 5:39 下午
 * @Version 1.0
 */
public interface UserPermissionService {

    UserPermission getById(Integer id);

    UserPermission getByUserPermission(UserPermission userPermission);

    void update(UserPermission userPermission);

    void add(UserPermission userPermission);

    void deleteByUserPermission(UserPermission userPermission);

    /**
     * 只查询数量
     * @param userPermission
     * @return
     */
    int countByBusiness(UserPermission userPermission);

    List<UserPermission> queryByBusiness(UserPermission userPermission);
}
