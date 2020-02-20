package com.baiyi.opscloud.mapper;

import com.baiyi.opscloud.domain.generator.OcAuthRole;
import com.baiyi.opscloud.domain.param.auth.RoleParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcAuthRoleMapper extends Mapper<OcAuthRole> {

    List<OcAuthRole> queryOcAuthRoleByParam(RoleParam.PageQuery pageQuery);
}