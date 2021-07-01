package com.baiyi.opscloud.event.handler;

import com.baiyi.opscloud.event.listener.ServerDeleteEventListener;
import com.baiyi.opscloud.event.listener.ServerUpdateEventListener;
import com.baiyi.opscloud.event.param.ServerEventParam;
import com.google.common.eventbus.AsyncEventBus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/15 5:45 下午
 * @Since 1.0
 */

@Slf4j
@Component
public class ServerEventHandler {

    @Resource
    private AsyncEventBus asyncEventBus;

    @Resource
    private ServerDeleteEventListener serverDeleteEventListener;

    @Resource
    private ServerUpdateEventListener serverUpdateEventListener;

    @PostConstruct
    public void init() {
        asyncEventBus.register(serverDeleteEventListener);
        asyncEventBus.register(serverUpdateEventListener);
    }

    @PreDestroy
    public void destroy() {
        asyncEventBus.unregister(serverDeleteEventListener);
        asyncEventBus.unregister(serverUpdateEventListener);
    }

    public void updateHandle(ServerEventParam.update update) {
        log.info("服务器更新事件触发，服务器id:{}", update.getServer().getId());
        asyncEventBus.post(update);
    }

    public void deleteHandle(ServerEventParam.delete delete) {
        log.info("服务器删除事件触发，服务器id:{}", delete.getId());
        asyncEventBus.post(delete);
    }

}
