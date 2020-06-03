package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.generator.opscloud.OcEnv;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.service.env.OcEnvService;
import com.google.common.base.Joiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2020/5/30 2:53 下午
 * @Version 1.0
 */
@Component
public class ServerBaseFacade {

    private static OcEnvService ocEnvService;

    @Autowired
    private void setOcEnvService(OcEnvService ocEnvService){
        ServerBaseFacade.ocEnvService = ocEnvService;
    }

    /**
     * 带列号
     *
     * @return
     */
    public static String acqServerName(OcServer ocServer) {
        OcEnv ocEnv = ocEnvService.queryOcEnvByType(ocServer.getEnvType());
        if (ocEnv == null || ocEnv.getEnvName().equals("prod")) {
            return Joiner.on("-").join(ocServer.getName(), ocServer.getSerialNumber());
        } else {
            return Joiner.on("-").join(ocServer.getName(), ocEnv.getEnvName(), ocServer.getSerialNumber());
        }
    }

    /**
     * 不带列号
     *
     * @return
     */
    public static String acqHostname(OcServer ocServer) {
        OcEnv ocEnv = ocEnvService.queryOcEnvByType(ocServer.getEnvType());
        if (ocEnv == null || ocEnv.getEnvName().equals("prod")) {
            return ocServer.getName();
        } else {
            return Joiner.on("-").join(ocServer.getName(), ocEnv.getEnvName());
        }
    }
}
