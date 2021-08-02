package com.baiyi.opscloud.event.listener;

import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.event.param.ServerEventParam;
import com.baiyi.opscloud.facade.business.BusinessFacade;
import com.baiyi.opscloud.facade.server.ServerGroupFacade;
import com.baiyi.opscloud.service.server.ServerService;
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

    @Resource
    private BusinessFacade businessFacade;

    @Subscribe
    public void ServerGroupCacheEvict(ServerEventParam.delete delete) {
        Server server = serverService.getById(delete.getId());
        serverGroupFacade.ServerGroupCacheEvict(server.getServerGroupId());
    }

    @Subscribe
    public void deleteBusinessRelation(ServerEventParam.delete delete) {
       // businessFacade.deleteBusinessRelation(BusinessTypeEnum.SERVER.getType(), delete.getId());
    }

}
