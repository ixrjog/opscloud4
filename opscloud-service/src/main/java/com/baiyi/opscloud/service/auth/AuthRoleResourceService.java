package com.baiyi.opscloud.service.auth;

import com.baiyi.opscloud.domain.generator.opscloud.AuthRoleResource;

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

    void deleteByResourceId(Integer resourceId);

}