package com.baiyi.opscloud.service.auth;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcAuthUserRole;
import com.baiyi.opscloud.domain.param.auth.UserRoleParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/12 3:21 下午
 * @Version 1.0
 */
public interface OcAuthUserRoleService {

    int countByRoleId(int roleId);

    DataTable<OcAuthUserRole> queryOcAuthUserRoleByParam(UserRoleParam.UserRolePageQuery pageQuery);

    List<OcAuthUserRole> queryOcAuthUserRolesByUsername(String username);

    void addOcAuthUserRole(OcAuthUserRole ocAuthUserRole);

    void deleteOcAuthUserRoleById(int id);

    OcAuthUserRole queryOcAuthUserRoleById(int id);

    OcAuthUserRole queryOcAuthUserRoleByUniqueKey(OcAuthUserRole ocAuthUserRole);

    boolean authenticationByUsernameAndResourceName(String username, String resourceName);

}
