package com.baiyi.opscloud.service.org;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcOrgDepartment;
import com.baiyi.opscloud.domain.param.org.DepartmentParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/4/20 6:40 下午
 * @Version 1.0
 */
public interface OcOrgDepartmentService {

    DataTable<OcOrgDepartment> queryOcOrgDepartmentParam(DepartmentParam.PageQuery pageQuery);

    List<OcOrgDepartment> queryOcOrgDepartmentByParentId(int parentId);

    OcOrgDepartment queryOcOrgDepartmentById(int id);

    void addOcOrgDepartment(OcOrgDepartment ocOrgDepartment);

    void updateOcOrgDepartment(OcOrgDepartment ocOrgDepartment);

    void deleteOcOrgDepartmentById(int id);
}
