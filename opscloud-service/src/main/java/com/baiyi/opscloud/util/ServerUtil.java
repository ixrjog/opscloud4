package com.baiyi.opscloud.util;

import com.baiyi.opscloud.domain.generator.opscloud.Env;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.vo.server.ServerVO;
import com.baiyi.opscloud.service.sys.EnvService;
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

    public static void wrapDisplayName(ServerVO.Server server){
        server.setDisplayName(toServerName(server));
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

    private static String toServerName(ServerVO.Server server) {
        if (server.getEnv() == null || server.getEnv().getEnvName().equals("prod")) {
            return Joiner.on("-").join(server.getName(), server.getSerialNumber());
        } else {
            return Joiner.on("-").join(server.getName(), server.getEnv().getEnvName(), server.getSerialNumber());
        }
    }
}
