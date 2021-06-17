package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.ServerGroup;
import com.baiyi.caesar.domain.param.server.ServerGroupParam;
import com.baiyi.caesar.domain.param.user.UserBusinessPermissionParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ServerGroupMapper extends Mapper<ServerGroup> {

    List<ServerGroup> queryServerGroupByParam(ServerGroupParam.ServerGroupPageQuery pageQuery);

    List<ServerGroup> queryUserPermissionServerGroupByParam(UserBusinessPermissionParam.UserBusinessPermissionPageQuery pageQuery);

    List<ServerGroup> queryUserServerGroupTreeByParam(ServerGroupParam.UserServerTreeQuery queryParam);
}