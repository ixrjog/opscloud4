package com.baiyi.opscloud.event.handler;

import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.event.listener.UserRetireEventListener;
import com.google.common.eventbus.AsyncEventBus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/30 10:51 上午
 * @Since 1.0
 */
@Slf4j
@Component
public class UserRetireEventHandler {

    @Resource
    private AsyncEventBus asyncEventBus;

    @Resource
    private UserRetireEventListener userRetireEventListener;

    @PostConstruct
    public void init() {
        asyncEventBus.register(userRetireEventListener);
    }

    @PreDestroy
    public void destroy() {
        asyncEventBus.unregister(userRetireEventListener);
    }

    public void eventPost(OcUser ocUser) {
        asyncEventBus.post(ocUser);
        log.info("用户离职事件触发，离职用户{}，{}", ocUser.getUsername(), ocUser.getDisplayName());
    }
}
