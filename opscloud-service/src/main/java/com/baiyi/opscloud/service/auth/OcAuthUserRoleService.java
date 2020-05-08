package com.baiyi.opscloud.service.auth;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcAuthUserRole;
import com.baiyi.opscloud.domain.param.auth.UserRoleParam;

/**
 * @Author baiyi
 * @Date 2020/2/12 3:21 下午
 * @Version 1.0
 */
public interface OcAuthUserRoleService {

    int countByRoleId(int roleId);

    DataTable<OcAuthUserRole> queryOcAuthUserRoleByParam(UserRoleParam.PageQuery pageQuery);

    void addOcAuthUserRole(OcAuthUserRole ocAuthUserRole);

    void deleteOcAuthUserRoleById(int id);

    OcAuthUserRole queryOcAuthUserRoleById(int id);

    boolean authenticationByUsernameAndResourceName(String username, String resourceName);

}
