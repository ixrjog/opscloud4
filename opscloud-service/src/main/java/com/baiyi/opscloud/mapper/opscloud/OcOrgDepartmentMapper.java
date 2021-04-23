package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcOrgDepartment;
import com.baiyi.opscloud.domain.param.org.DepartmentParam;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcOrgDepartmentMapper extends Mapper<OcOrgDepartment> {

    List<OcOrgDepartment> queryOcOrgDepartmentParam(DepartmentParam.PageQuery pageQuery);

    List<OcOrgDepartment> queryFirstLevelDepartmentPage(DepartmentParam.PageQuery pageQuery);

    List<OcOrgDepartment> queryOcOrgDepartmentByIdList(@Param("idList") List<Integer> idList);
}