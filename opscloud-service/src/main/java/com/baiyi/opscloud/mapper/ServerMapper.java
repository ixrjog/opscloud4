package com.baiyi.opscloud.mapper;

import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.param.server.ServerParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ServerMapper extends Mapper<Server> {

    List<Server> queryServerByParam(ServerParam.ServerPageQuery pageQuery);

    List<Server> queryUserPermissionServerByParam(ServerParam.UserPermissionServerPageQuery pageQuery);

    List<Server> queryUserRemoteServerPage(ServerParam.UserRemoteServerPageQuery pageQuery);
}