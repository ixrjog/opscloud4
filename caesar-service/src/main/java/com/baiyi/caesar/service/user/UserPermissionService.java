package com.baiyi.caesar.service.user;

import com.baiyi.caesar.domain.generator.caesar.UserPermission;

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
}
