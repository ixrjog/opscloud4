package com.baiyi.opscloud.service.server;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.ServerGroup;
import com.baiyi.opscloud.domain.param.server.ServerGroupParam;
import com.baiyi.opscloud.domain.param.user.UserBusinessPermissionParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/24 1:01 下午
 * @Version 1.0
 */
public interface ServerGroupService {

    ServerGroup getById(Integer id);

    ServerGroup getByName(String name);

    void add(ServerGroup serverGroup);

    void update(ServerGroup serverGroup);

    Integer countByServerGroupTypeId(Integer serverGroupTypeId);

    void delete(ServerGroup serverGroup);

    DataTable<ServerGroup> queryPageByParam(ServerGroupParam.ServerGroupPageQuery pageQuery);

    DataTable<ServerGroup> queryPageByParam(UserBusinessPermissionParam.UserBusinessPermissionPageQuery pageQuery);

    List<ServerGroup> queryUserServerGroupTreeByParam(ServerGroupParam.UserServerTreeQuery queryParam);

}