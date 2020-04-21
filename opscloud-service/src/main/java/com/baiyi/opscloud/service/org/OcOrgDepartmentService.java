package com.baiyi.opscloud.service.org;

import com.baiyi.opscloud.domain.generator.OcOrgDepartment;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/4/20 6:40 下午
 * @Version 1.0
 */
public interface OcOrgDepartmentService {

    List<OcOrgDepartment> queryOcOrgDepartmentByParentId(int parentId);

    OcOrgDepartment queryOcOrgDepartmentById(int id);

    void updateOcOrgDepartment(OcOrgDepartment ocOrgDepartment);
}
