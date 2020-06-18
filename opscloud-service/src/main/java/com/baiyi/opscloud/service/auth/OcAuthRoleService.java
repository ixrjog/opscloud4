package com.baiyi.opscloud.service.auth;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcAuthRole;
import com.baiyi.opscloud.domain.param.auth.RoleParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/12 2:11 下午
 * @Version 1.0
 */
public interface OcAuthRoleService {

    DataTable<OcAuthRole> queryOcAuthRoleByParam(RoleParam.PageQuery pageQuery);

    /**
     * 查询用户访问级别最高的角色
     * @param username
     * @return
     */
    OcAuthRole queryTopOcAuthRoleByUsername(String username);

    int queryOcAuthRoleAccessLevelByUsername(String username);

    void addOcAuthRole(OcAuthRole ocAuthRole);

    void updateOcAuthRole(OcAuthRole ocAuthRole);

    void deleteOcAuthRoleById(int id);

    OcAuthRole queryOcAuthRoleById(int id);

    OcAuthRole queryOcAuthRoleByName(String roleName);

    List<OcAuthRole> queryUserTicketOcAuthRoleByParam(RoleParam.UserTicketOcAuthRoleQuery queryParam);

    List<OcAuthRole> queryAllOcAuthRole();
}
