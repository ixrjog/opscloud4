package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.OcOrgDepartment;
import com.baiyi.opscloud.domain.param.org.DepartmentParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcOrgDepartmentMapper extends Mapper<OcOrgDepartment> {

    List<OcOrgDepartment> queryOcOrgDepartmentParam(DepartmentParam.PageQuery pageQuery);
}