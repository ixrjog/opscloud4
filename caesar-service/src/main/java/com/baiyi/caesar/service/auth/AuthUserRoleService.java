package com.baiyi.caesar.service.auth;

import com.baiyi.caesar.domain.generator.caesar.AuthUserRole;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/14 11:50 上午
 * @Version 1.0
 */
public interface AuthUserRoleService {

    void add(AuthUserRole authUserRole);

    void deleteById(Integer id);

    List<AuthUserRole> queryByUsername(String username);

    AuthUserRole queryByUniqueKey(AuthUserRole authUserRole);
}
