package com.baiyi.opscloud.mapper;

import com.baiyi.opscloud.domain.generator.OcAuthUserRole;
import com.baiyi.opscloud.domain.param.auth.UserRoleParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcAuthUserRoleMapper extends Mapper<OcAuthUserRole> {

    List<OcAuthUserRole> queryOcAuthUserRoleByParam(UserRoleParam.PageQuery pageQuery);

}