package com.baiyi.opscloud.server;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.server.factory.ServerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author baiyi
 * @Date 2020/4/1 3:38 下午
 * @Version 1.0
 */
@Component
public class ServerCenter {

    private final static String ACTION_CREATE = "CREATE";

    private final static String ACTION_UPDATE = "UPDATE";

    private final static String ACTION_REMOVE = "REMOVE";

    private final static String ACTION_DISABLE = "DISABLE";

    private final static String ACTION_ENABLE = "ENABLE";

    private Boolean action(OcServer ocServer, String action) {
        Map<String, IServer> serverContainer = ServerFactory.getIServerContainer();
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        serverContainer.keySet().forEach(k -> {
            IServer iServer = serverContainer.get(k);
            BusinessWrapper<Boolean> wrapper = BusinessWrapper.SUCCESS;
            switch (action) {
                case ACTION_CREATE:
                    wrapper = iServer.create(ocServer);
                    break;
                case ACTION_UPDATE:
                    wrapper = iServer.update(ocServer);
                    break;
                case ACTION_REMOVE:
                    wrapper = iServer.remove(ocServer);
                    break;
                case ACTION_DISABLE:
                    wrapper = iServer.disable(ocServer);
                    break;
                case ACTION_ENABLE:
                    wrapper = iServer.enable(ocServer);
                    break;
            }
            if (!wrapper.isSuccess())
                atomicBoolean.set(Boolean.FALSE);
        });
        return atomicBoolean.get();
    }

    /**
     * 服务器工厂 创建服务器信息
     *
     * @param ocServer
     * @return
     */
    public Boolean create(OcServer ocServer) {
        return action(ocServer, ACTION_CREATE);
    }

    /**
     * 服务器工厂 创建服务器信息
     *
     * @param ocServer
     * @return
     */
    public Boolean update(OcServer ocServer) {
        return action(ocServer, ACTION_UPDATE);
    }

    /**
     * 服务器工厂 创建服务器信息
     *
     * @param ocServer
     * @return
     */
    public Boolean remove(OcServer ocServer) {
        return action(ocServer, ACTION_REMOVE);
    }


    public Boolean disable(OcServer ocServer) {
        return action(ocServer, ACTION_DISABLE);
    }


    public Boolean enable(OcServer ocServer) {
        return action(ocServer, ACTION_ENABLE);
    }
}
