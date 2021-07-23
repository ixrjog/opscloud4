package com.baiyi.opscloud.service.server;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.param.server.ServerParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/24 9:39 上午
 * @Version 1.0
 */
public interface ServerService {

    Server getById(Integer id);

    Server getByPrivateIp(String privateIp);

    void add(Server server);

    void update(Server server);

    void delete(Integer id);

    DataTable<Server> queryServerPage(ServerParam.ServerPageQuery pageQuery);

    DataTable<Server> queryUserPermissionServerPage(ServerParam.UserPermissionServerPageQuery pageQuery);

    DataTable<Server> queryUserRemoteServerPage(ServerParam.UserRemoteServerPageQuery pageQuery);

    Server getMaxSerialNumberServer(Integer serverGroupId, Integer envType);

    int countByServerGroupId(Integer serverGroupId);

    List<Server> queryByServerGroupId(Integer serverGroupId);
}
