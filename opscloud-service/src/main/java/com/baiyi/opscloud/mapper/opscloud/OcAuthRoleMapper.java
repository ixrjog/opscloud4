package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcAuthRole;
import com.baiyi.opscloud.domain.param.auth.RoleParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcAuthRoleMapper extends Mapper<OcAuthRole> {

    List<OcAuthRole> queryOcAuthRoleByParam(RoleParam.PageQuery pageQuery);

    OcAuthRole queryTopOcAuthRoleByUsername(String username);

    int queryOcAuthRoleAccessLevelByUsername(String username);

    List<OcAuthRole> queryUserTicketOcAuthRoleByParam(RoleParam.UserTicketOcAuthRoleQuery queryParam);
}