package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.UserGroup;
import com.baiyi.caesar.domain.param.user.UserBusinessPermissionParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserGroupMapper extends Mapper<UserGroup> {

    List<UserGroup> queryUserPermissionGroupByParam(UserBusinessPermissionParam.UserBusinessPermissionPageQuery pageQuery);
}