package com.baiyi.opscloud.server;

import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.server.factory.ServerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/4/1 3:38 下午
 * @Version 1.0
 */
@Component
public class ServerCenter {

    /**
     * 服务器工厂 创建服务器信息
     *
     * @param ocServer
     * @return
     */
    public Boolean create(OcServer ocServer) {
        Map<String, IServer> serverContainer = ServerFactory.getIServerContainer();
        Boolean result = Boolean.TRUE;
        for (String key : serverContainer.keySet()) {
            IServer iServer = serverContainer.get(key);
            if (!iServer.create(ocServer))
                result = Boolean.FALSE;
        }
        return result;
    }

    /**
     * 服务器工厂 创建服务器信息
     *
     * @param ocServer
     * @return
     */
    public Boolean update(OcServer ocServer) {
        Map<String, IServer> serverContainer = ServerFactory.getIServerContainer();
        for (String key : serverContainer.keySet()) {
            IServer iServer = serverContainer.get(key);
            if (!iServer.update(ocServer))
                return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 服务器工厂 创建服务器信息
     *
     * @param ocServer
     * @return
     */
    public Boolean remove(OcServer ocServer) {
        Map<String, IServer> serverContainer = ServerFactory.getIServerContainer();
        for (String key : serverContainer.keySet()) {
            IServer iServer = serverContainer.get(key);
            if (!iServer.remove(ocServer))
                return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }


    public Boolean disable(OcServer ocServer) {
        Map<String, IServer> serverContainer = ServerFactory.getIServerContainer();
        for (String key : serverContainer.keySet()) {
            IServer iServer = serverContainer.get(key);
            if (!iServer.disable(ocServer))
                return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }


    public Boolean enable(OcServer ocServer) {
        Map<String, IServer> serverContainer = ServerFactory.getIServerContainer();
        for (String key : serverContainer.keySet()) {
            IServer iServer = serverContainer.get(key);
            if (!iServer.enable(ocServer))
                return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
