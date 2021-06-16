package com.baiyi.caesar.event.handler;

import com.baiyi.caesar.event.listener.ServerGroupDeleteEventListener;
import com.baiyi.caesar.event.param.ServerGroupEventParam;
import com.google.common.eventbus.AsyncEventBus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/16 10:22 上午
 * @Since 1.0
 */

@Slf4j
@Component
public class ServerGroupEventHandler {

    @Resource
    private AsyncEventBus asyncEventBus;

    @Resource
    private ServerGroupDeleteEventListener serverGroupDeleteEventListener;

    @PostConstruct
    public void init() {
        asyncEventBus.register(serverGroupDeleteEventListener);
    }

    @PreDestroy
    public void destroy() {
        asyncEventBus.unregister(serverGroupDeleteEventListener);
    }

    public void deleteHandle(ServerGroupEventParam.delete delete) {
        log.info("服务器组删除事件触发，服务器组id:{}", delete.getId());
        asyncEventBus.post(delete);
    }
}
