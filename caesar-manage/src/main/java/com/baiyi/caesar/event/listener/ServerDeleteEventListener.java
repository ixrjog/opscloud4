package com.baiyi.caesar.event.listener;

import com.baiyi.caesar.domain.generator.caesar.Server;
import com.baiyi.caesar.event.param.ServerEventParam;
import com.baiyi.caesar.facade.server.ServerGroupFacade;
import com.baiyi.caesar.service.server.ServerService;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/15 5:47 下午
 * @Since 1.0
 */

@Slf4j
@Component
public class ServerDeleteEventListener {

    @Resource
    private ServerGroupFacade serverGroupFacade;

    @Resource
    private ServerService serverService;

    @Subscribe
    public void ServerGroupCacheEvict(ServerEventParam.delete delete) {
        Server server = serverService.getById(delete.getId());
        serverGroupFacade.ServerGroupCacheEvict(server.getServerGroupId());
    }

}
