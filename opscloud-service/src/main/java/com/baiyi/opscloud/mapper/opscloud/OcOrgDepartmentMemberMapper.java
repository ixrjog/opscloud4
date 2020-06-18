package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcOrgDepartmentMember;
import com.baiyi.opscloud.domain.param.org.DepartmentMemberParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcOrgDepartmentMemberMapper extends Mapper<OcOrgDepartmentMember> {

    List<OcOrgDepartmentMember> queryOcOrgDepartmentMemberParam(DepartmentMemberParam.PageQuery pageQuery);
}