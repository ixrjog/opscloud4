package com.baiyi.opscloud.event.handler;

import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.event.listener.OcServerEventListener;
import com.google.common.eventbus.AsyncEventBus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/22 4:01 下午
 * @Since 1.0
 */

@Slf4j
@Component
public class OcServerEventHandler {

    @Resource
    private AsyncEventBus asyncEventBus;

    @Resource
    private OcServerEventListener ocServerEventListener;

    @PostConstruct
    public void init() {
        asyncEventBus.register(ocServerEventListener);
    }

    @PreDestroy
    public void destroy() {
        asyncEventBus.unregister(ocServerEventListener);
    }

    public void eventPost(OcServer ocServer) {
        asyncEventBus.post(ocServer);
        log.info("OcServerEventHandler post event {}", ocServer.getPrivateIp());
    }
}
