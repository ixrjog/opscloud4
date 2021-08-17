package com.baiyi.opscloud.facade.server;

import com.baiyi.opscloud.domain.generator.opscloud.Env;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.service.sys.EnvService;
import com.google.common.base.Joiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2020/5/30 2:53 下午
 * @Version 1.0
 */
@Component
public class SimpleServerNameFacade {

    private static EnvService envService;

    @Autowired
    private void setEnvService(EnvService envService) {
        SimpleServerNameFacade.envService = envService;
    }


    /**
     * 带列号
     *
     * @return
     */
    public static String toServerName(Server server) {
        Env env = envService.getByEnvType(server.getEnvType());
        return acqServerName(server, env);
    }

    private static String acqServerName(Server server, Env env) {
        if (env == null || env.getEnvName().equals("prod")) {
            return Joiner.on("-").join(server.getName(), server.getSerialNumber());
        } else {
            return Joiner.on("-").join(server.getName(), env.getEnvName(), server.getSerialNumber());
        }
    }

    /**
     * 不带列号
     *
     * @return
     */
    public static String toHostname(Server server) {
        Env env = envService.getByEnvType(server.getEnvType());
        if (env == null || env.getEnvName().equals("prod")) {
            return server.getName();
        } else {
            return Joiner.on("-").join(server.getName(), env.getEnvName());
        }
    }
}
