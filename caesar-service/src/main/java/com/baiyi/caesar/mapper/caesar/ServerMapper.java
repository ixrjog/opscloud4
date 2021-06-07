package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.Server;
import com.baiyi.caesar.domain.param.server.ServerParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ServerMapper extends Mapper<Server> {

    List<Server> queryServerByParam(ServerParam.ServerPageQuery pageQuery);

    List<Server> queryUserPermissionServerByParam(ServerParam.UserPermissionServerPageQuery pageQuery);
}