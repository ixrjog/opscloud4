package com.baiyi.caesar.service.server;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.ServerGroup;
import com.baiyi.caesar.domain.param.server.ServerGroupParam;

/**
 * @Author baiyi
 * @Date 2021/5/24 1:01 下午
 * @Version 1.0
 */
public interface ServerGroupService {

    ServerGroup getById(Integer id);

    void add(ServerGroup serverGroup);

    void update(ServerGroup serverGroup);

    Integer countByServerGroupTypeId(Integer serverGroupTypeId);

    DataTable<ServerGroup> queryServerGroupPage(ServerGroupParam.ServerGroupPageQuery pageQuery);
}
