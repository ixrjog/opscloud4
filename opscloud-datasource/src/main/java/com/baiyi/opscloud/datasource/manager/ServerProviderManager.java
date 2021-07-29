package com.baiyi.opscloud.datasource.manager;

import com.baiyi.opscloud.common.base.Global;
import com.baiyi.opscloud.datasource.server.IServer;
import com.baiyi.opscloud.datasource.server.factory.ServerFactory;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/22 1:42 下午
 * @Since 1.0
 */
@Component
public class ServerProviderManager {

    @Async(value = Global.TaskPools.EXECUTOR)
    public void create(Server server) {
        Map<String, IServer> serverProviders = ServerFactory.getIServerContainer();
        serverProviders.forEach((k, v) -> v.create(server));
    }

    @Async(value = Global.TaskPools.EXECUTOR)
    public void update(Server server) {
        Map<String, IServer> serverProviders = ServerFactory.getIServerContainer();
        serverProviders.forEach((k, v) -> v.update(server));
    }

    @Async(value = Global.TaskPools.EXECUTOR)
    public void destroy(Integer id) {
        Map<String, IServer> serverProviders = ServerFactory.getIServerContainer();
        serverProviders.forEach((k, v) -> v.destroy(id));
    }

}