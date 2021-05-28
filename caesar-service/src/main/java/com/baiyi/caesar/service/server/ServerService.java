package com.baiyi.caesar.service.server;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.Server;
import com.baiyi.caesar.domain.param.server.ServerParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/24 9:39 上午
 * @Version 1.0
 */
public interface ServerService {

    void add(Server server);

    void update(Server server);

    DataTable<Server> queryServerPage(ServerParam.ServerPageQuery pageQuery);

    Server getMaxSerialNumberServer(Integer serverGroupId, Integer envType);

    int countByServerGroupId(Integer serverGroupId);

    List<Server> queryByServerGroupId(Integer serverGroupId);
}
