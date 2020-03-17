package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.bo.OcServerBO;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;

/**
 * @Author baiyi
 * @Date 2020/1/10 2:31 下午
 * @Version 1.0
 */
public interface OcServerFacade {

    OcServerBO getOcServerBO(OcServer ocServer);
}
