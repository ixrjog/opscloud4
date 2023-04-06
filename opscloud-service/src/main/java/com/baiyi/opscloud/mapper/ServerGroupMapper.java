package com.baiyi.opscloud.mapper;

import com.baiyi.opscloud.domain.generator.opscloud.ServerGroup;
import com.baiyi.opscloud.domain.param.server.ServerGroupParam;
import com.baiyi.opscloud.domain.param.user.UserBusinessPermissionParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ServerGroupMapper extends Mapper<ServerGroup> {

    List<ServerGroup> queryServerGroupByParam(ServerGroupParam.ServerGroupPageQuery pageQuery);

    List<ServerGroup> queryUserPermissionServerGroupByParam(UserBusinessPermissionParam.UserBusinessPermissionPageQuery pageQuery);

    List<ServerGroup> queryUserServerGroupTreeByParam(ServerGroupParam.UserServerTreeQuery queryParam);
}