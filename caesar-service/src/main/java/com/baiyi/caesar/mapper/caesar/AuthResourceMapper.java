package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.AuthResource;
import com.baiyi.caesar.domain.param.auth.AuthResourceParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface AuthResourceMapper extends Mapper<AuthResource> {

    List<AuthResource> queryRoleBindResourceByParam(AuthResourceParam.RoleBindResourcePageQuery pageQuery);

    List<AuthResource> queryRoleUnbindResourceByParam(AuthResourceParam.RoleBindResourcePageQuery pageQuery);
}