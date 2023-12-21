package com.baiyi.opscloud.facade.server;

import com.baiyi.opscloud.domain.generator.opscloud.Env;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.vo.server.ServerVO;
import com.baiyi.opscloud.service.sys.EnvService;
import com.google.common.base.Joiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.baiyi.opscloud.common.base.Global.ENV_PROD;

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

    public static void wrapDisplayName(ServerVO.Server server){
        server.setDisplayName(toServerName(server));
    }

    /**
     * 带序号
     *
     * @return
     */
    public static String toServerName(Server server) {
        Env env = envService.getByEnvType(server.getEnvType());
        return toName(server, env);
    }

    public static String toName(Server server, Env env) {
        if (env == null || ENV_PROD.equals(env.getEnvName())) {
            return Joiner.on("-").join(server.getName(), server.getSerialNumber());
        } else {
            return Joiner.on("-").join(server.getName(), env.getEnvName(), server.getSerialNumber());
        }
    }

    public static String toServerName(ServerVO.Server server) {
        if (server.getEnv() == null || ENV_PROD.equals(server.getEnv().getEnvName())) {
            return Joiner.on("-").join(server.getName(), server.getSerialNumber());
        } else {
            return Joiner.on("-").join(server.getName(), server.getEnv().getEnvName(), server.getSerialNumber());
        }
    }

    /**
     * 不带序号
     *
     * @return
     */
    public static String toHostname(Server server) {
        Env env = envService.getByEnvType(server.getEnvType());
        if (env == null || ENV_PROD.equals(env.getEnvName())) {
            return server.getName();
        } else {
            return Joiner.on("-").join(server.getName(), env.getEnvName());
        }
    }

}