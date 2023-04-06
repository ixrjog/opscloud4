package com.baiyi.opscloud.service.server;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.facade.server.SimpleServerNameFacade;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/5/31 09:19
 * @Version 1.0
 */
public class ServerServiceTest extends BaseUnit {

    @Resource
    private ServerService serverService;

    @Resource
    private SimpleServerNameFacade simpleServerNameFacade;

    @Test
    void test() {
        List<Server> servers = serverService.selectAll();
        servers.forEach(e -> {
            String displayName = SimpleServerNameFacade.toServerName(e);
            print(displayName);
            e.setDisplayName(displayName);
            serverService.updateNotEvent(e);
        });
    }

}
