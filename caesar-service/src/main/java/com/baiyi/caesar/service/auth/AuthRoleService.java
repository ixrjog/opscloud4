package com.baiyi.caesar.service.auth;


import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.AuthRole;
import com.baiyi.caesar.domain.param.auth.AuthRoleParam;

/**
 * @Author baiyi
 * @Date 2021/5/12 9:26 上午
 * @Version 1.0
 */
public interface AuthRoleService {

    AuthRole getByRoleName(String roleName);

    AuthRole getById(int id);

    DataTable<AuthRole> queryPageByParam(AuthRoleParam.AuthRolePageQuery pageQuery);

    void add(AuthRole authRole);

    void update(AuthRole authRole);

    void deleteById(int id);
}
