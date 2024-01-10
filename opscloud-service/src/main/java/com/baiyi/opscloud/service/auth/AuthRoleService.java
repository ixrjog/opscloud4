package com.baiyi.opscloud.service.auth;


import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.AuthRole;
import com.baiyi.opscloud.domain.param.auth.AuthRoleParam;

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

    int getRoleAccessLevelByUsername(String username);

}