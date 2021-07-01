package com.baiyi.opscloud.event.listener;

import com.baiyi.opscloud.event.param.ServerEventParam;
import com.baiyi.opscloud.facade.server.ServerGroupFacade;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/15 5:46 下午
 * @Since 1.0
 */

@Slf4j
@Component
public class ServerUpdateEventListener {

    @Resource
    private ServerGroupFacade serverGroupFacade;

    @Subscribe
    public void ServerGroupCacheEvict(ServerEventParam.update update) {
        serverGroupFacade.ServerGroupCacheEvict(update.getServer().getServerGroupId());
    }
}
