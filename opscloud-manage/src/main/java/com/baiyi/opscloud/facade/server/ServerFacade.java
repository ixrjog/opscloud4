package com.baiyi.opscloud.facade.server;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.server.ServerParam;
import com.baiyi.opscloud.domain.vo.server.ServerVO;

/**
 * @Author baiyi
 * @Date 2021/5/24 5:45 下午
 * @Version 1.0
 */
public interface ServerFacade {

    DataTable<ServerVO.Server> queryServerPage(ServerParam.ServerPageQuery pageQuery);

    ServerVO.Server addServer(ServerParam.AddServer server);

    void updateServer(ServerParam.UpdateServer server);

    void scanServerMonitoringStatus();

    void deleteServerById(Integer id);

    DataTable<ServerVO.Server> queryUserRemoteServerPage(ServerParam.UserRemoteServerPageQuery queryParam);

}
