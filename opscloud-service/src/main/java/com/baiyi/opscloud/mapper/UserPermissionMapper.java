package com.baiyi.opscloud.mapper;

import com.baiyi.opscloud.domain.generator.opscloud.UserPermission;
import tk.mybatis.mapper.common.Mapper;

public interface UserPermissionMapper extends Mapper<UserPermission> {

    // int getRoleAccessLevelByUsername(String username);

    int statTotal(int businessType);

}