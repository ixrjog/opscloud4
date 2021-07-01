package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.AuthRole;
import tk.mybatis.mapper.common.Mapper;

public interface AuthRoleMapper extends Mapper<AuthRole> {

   int getRoleAccessLevelByUsername(String username);
}