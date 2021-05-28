package com.baiyi.caesar.util;

import com.baiyi.caesar.domain.generator.caesar.Env;
import com.baiyi.caesar.domain.generator.caesar.Server;
import com.baiyi.caesar.service.sys.EnvService;
import com.google.common.base.Joiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/5/28 2:07 下午
 * @Version 1.0
 */
@Component
public class ServerUtil {

    private static EnvService envService;

    @Autowired
    private void setEnvService(EnvService envService) {
        ServerUtil.envService = envService;
    }


    /**
     * 带列号
     *
     * @return
     */
    public static String toServerName(Server server) {
        Env env = envService.getByEnvType(server.getEnvType());
        return toServerName(server, env);
    }

    private static String toServerName(Server server, Env env) {
        if (env == null || env.getEnvName().equals("prod")) {
            return Joiner.on("-").join(server.getName(), server.getSerialNumber());
        } else {
            return Joiner.on("-").join(server.getName(), env.getEnvName(), server.getSerialNumber());
        }
    }
}
