package com.baiyi.opscloud.mapper;

import com.baiyi.opscloud.domain.generator.opscloud.AuthResource;
import com.baiyi.opscloud.domain.param.auth.AuthResourceParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface AuthResourceMapper extends Mapper<AuthResource> {

    List<AuthResource> queryRoleBindResourceByParam(AuthResourceParam.RoleBindResourcePageQuery pageQuery);

    List<AuthResource> queryRoleUnbindResourceByParam(AuthResourceParam.RoleBindResourcePageQuery pageQuery);
}