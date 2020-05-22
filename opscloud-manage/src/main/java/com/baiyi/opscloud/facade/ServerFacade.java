package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.param.server.ServerParam;
import com.baiyi.opscloud.domain.vo.server.ServerAttributeVO;
import com.baiyi.opscloud.domain.vo.server.ServerVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/21 4:47 下午
 * @Version 1.0
 */
public interface ServerFacade {

    DataTable<ServerVO.Server> queryServerPage(ServerParam.PageQuery pageQuery);

    DataTable<ServerVO.Server> fuzzyQueryServerPage(ServerParam.PageQuery pageQuery);

    List<ServerAttributeVO.ServerAttribute> queryServerAttribute(int id);

    BusinessWrapper<Boolean> saveServerAttribute(ServerAttributeVO.ServerAttribute serverAttribute);

    BusinessWrapper<Boolean> addServer(ServerVO.Server server);

    BusinessWrapper<Boolean> updateServer(ServerVO.Server server);

    BusinessWrapper<Boolean> deleteServerById(int id);

    /**
     * 带列号
     *
     * @return
     */
    String acqServerName(OcServer ocServer);

    /**
     * 不带列号
     *
     * @return
     */
    String acqHostname(OcServer ocServer);
}
