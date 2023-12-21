package com.baiyi.opscloud.facade.server.converter;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.IdUtil;
import com.baiyi.opscloud.common.util.ValidationUtil;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.param.server.ServerParam;
import com.baiyi.opscloud.service.server.ServerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2023/12/8 17:50
 * @Version 1.0
 */
@Component
@AllArgsConstructor
public class ServerConverter {

    private final ServerService serverService;

    public Server to(ServerParam.AddServer server) {
        return toServer(BeanCopierUtil.copyProperties(server, Server.class));
    }

    public Server to(ServerParam.UpdateServer server) {
        return toServer(BeanCopierUtil.copyProperties(server, Server.class));
    }

    private Server toServer(Server server) {
        server.setName(server.getName().trim());
        ValidationUtil.tryServerNameRule(server.getName());
        if (IdUtil.isEmpty(server.getSerialNumber())) {
            Server maxSerialNumberServer = serverService.getMaxSerialNumberServer(server.getServerGroupId(), server.getEnvType());
            server.setSerialNumber(null == maxSerialNumberServer ? 1 : maxSerialNumberServer.getSerialNumber() + 1);
        }
        if (server.getMonitorStatus() == null) {
            server.setMonitorStatus(-1);
        }
        if (server.getServerStatus() == null) {
            server.setServerStatus(1);
        }
        return server;
    }

}