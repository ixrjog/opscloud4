package com.baiyi.opscloud.mapper;

import com.baiyi.opscloud.domain.generator.opscloud.UserGroup;
import com.baiyi.opscloud.domain.param.user.UserBusinessPermissionParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserGroupMapper extends Mapper<UserGroup> {

    List<UserGroup> queryUserPermissionGroupByParam(UserBusinessPermissionParam.UserBusinessPermissionPageQuery pageQuery);
}