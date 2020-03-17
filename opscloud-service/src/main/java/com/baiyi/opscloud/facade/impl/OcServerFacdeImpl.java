package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.domain.bo.OcServerBO;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.facade.OcServerFacade;
import com.baiyi.opscloud.service.env.OcEnvService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/1/10 2:32 下午
 * @Version 1.0
 */
@Service
public class OcServerFacdeImpl implements OcServerFacade {

    @Resource
    private OcEnvService ocEnvService;

    @Override
    public OcServerBO getOcServerBO(OcServer ocServer) {
        return OcServerBO.builder()
                .ocServer(ocServer)
                .ocEnv(ocEnvService.queryOcEnvById(ocServer.getEnvType()))
                .build();
    }


}
