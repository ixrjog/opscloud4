package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.auth.GroupParam;
import com.baiyi.opscloud.domain.param.auth.ResourceParam;
import com.baiyi.opscloud.domain.param.auth.RoleParam;
import com.baiyi.opscloud.domain.vo.auth.OcGroupVO;
import com.baiyi.opscloud.domain.vo.auth.OcResourceVO;
import com.baiyi.opscloud.domain.vo.auth.OcRoleVO;

/**
 * @Author baiyi
 * @Date 2020/2/13 8:20 下午
 * @Version 1.0
 */
public interface AuthFacade {

    DataTable<OcRoleVO.OcRole> queryRolePage(RoleParam.PageQuery pageQuery);

    void addRole(OcRoleVO.OcRole ocRole);

    void updateRole(OcRoleVO.OcRole ocRole);

    BusinessWrapper<Boolean> deleteRoleById(int id);

    DataTable<OcResourceVO.OcResource> queryResourcePage(ResourceParam.PageQuery pageQuery);

    void addResource(OcResourceVO.OcResource ocResource);

    void updateResource(OcResourceVO.OcResource ocResource);

    void updateResourceNeedAuth(OcResourceVO.OcResource ocResource);

    BusinessWrapper<Boolean> deleteResourceById(int id);

    // resource group
    DataTable<OcGroupVO.OcGroup> queryGroupPage(GroupParam.PageQuery pageQuery);

    void addGroup(OcGroupVO.OcGroup ocGroup);

    void updateGroup(OcGroupVO.OcGroup ocGroup);

    BusinessWrapper<Boolean> deleteGroupById(int id);

}
