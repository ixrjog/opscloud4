package com.baiyi.opscloud.service.org;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcOrgDepartmentMember;
import com.baiyi.opscloud.domain.param.org.DepartmentMemberParam;

/**
 * @Author baiyi
 * @Date 2020/4/21 4:24 下午
 * @Version 1.0
 */
public interface OcOrgDepartmentMemberService {

    DataTable<OcOrgDepartmentMember> queryOcOrgDepartmentMemberParam(DepartmentMemberParam.PageQuery pageQuery);

    void addOcOrgDepartmentMember(OcOrgDepartmentMember ocOrgDepartmentMember);

    void updateOcOrgDepartmentMember(OcOrgDepartmentMember ocOrgDepartmentMember);

    OcOrgDepartmentMember getOcOrgDepartmentMemberByUniqueKey(int departmentId,int userId);

    int countOcOrgDepartmentMemberByDepartmentId(int departmentId);

    OcOrgDepartmentMember queryOcOrgDepartmentMemberById(int id);

    void deleteOcOrgDepartmentMemberById(int id);
}
