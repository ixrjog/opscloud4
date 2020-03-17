package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.param.server.ServerParam;
import com.baiyi.opscloud.domain.vo.server.OcServerAttributeVO;
import com.baiyi.opscloud.domain.vo.server.OcServerVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/21 4:47 下午
 * @Version 1.0
 */
public interface ServerFacade {

    DataTable<OcServerVO.Server> queryServerPage(ServerParam.PageQuery pageQuery);

    DataTable<OcServerVO.Server> fuzzyQueryServerPage(ServerParam.PageQuery pageQuery);

    List<OcServerAttributeVO.ServerAttribute> queryServerAttribute(int id);

    BusinessWrapper<Boolean> saveServerAttribute(OcServerAttributeVO.ServerAttribute serverAttribute);

    BusinessWrapper<Boolean> addServer(OcServerVO.Server server);

    BusinessWrapper<Boolean> updateServer(OcServerVO.Server server);

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
