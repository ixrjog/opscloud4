package com.baiyi.caesar.service.auth;

import com.baiyi.caesar.domain.generator.caesar.AuthRoleResource;

/**
 * @Author baiyi
 * @Date 2021/5/12 4:13 下午
 * @Version 1.0
 */
public interface AuthRoleResourceService {

    void add(AuthRoleResource authRoleResource);

    void deleteById(int id);

    Integer countByRoleId(Integer roleId);

    Integer countByResourceId(Integer resourceId);
}
