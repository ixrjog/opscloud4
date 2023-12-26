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

    List<Server> selectAll();

    Server getByUniqueKey(Integer envType, Integer serialNumber, Integer serverGroupId);

    Server getByPrivateIp(String privateIp);

    /**
     * 创建并触发事件
     *
     * @param server
     */
    void add(Server server);

    /**
     * 更新并触发事件
     *
     * @param server
     */
    void update(Server server);

    void updateNotEvent(Server server);

    void deleteById(Integer id);

    /**
     * 删除并触发事件
     *
     * @param server
     */
    void delete(Server server);

    DataTable<Server> queryServerPage(ServerParam.ServerPageQuery pageQuery);

    DataTable<Server> queryUserPermissionServerPage(ServerParam.UserPermissionServerPageQuery pageQuery);

    DataTable<Server> queryUserRemoteServerPage(ServerParam.UserRemoteServerPageQuery pageQuery);

    Server getMaxSerialNumberServer(Integer serverGroupId, Integer envType);

    List<Server> queryByGroupIdAndEnvType(Integer serverGroupId, Integer envType);

    int countByServerGroupId(Integer serverGroupId);

    List<Server> queryByServerGroupId(Integer serverGroupId);

}