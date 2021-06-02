package com.baiyi.caesar.facade.server;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.param.server.ServerParam;
import com.baiyi.caesar.domain.vo.server.ServerVO;

/**
 * @Author baiyi
 * @Date 2021/5/24 5:45 下午
 * @Version 1.0
 */
public interface ServerFacade {

    DataTable<ServerVO.Server> queryServerPage(ServerParam.ServerPageQuery pageQuery);

    void addServer(ServerVO.Server server);

    void updateServer(ServerVO.Server server);

    void deleteServerById(Integer id);

}
