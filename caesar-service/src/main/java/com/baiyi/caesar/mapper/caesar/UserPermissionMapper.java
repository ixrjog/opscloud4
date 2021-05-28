package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.UserPermission;
import tk.mybatis.mapper.common.Mapper;

public interface UserPermissionMapper extends Mapper<UserPermission> {

    int getRoleAccessLevelByUsername(String username);
}