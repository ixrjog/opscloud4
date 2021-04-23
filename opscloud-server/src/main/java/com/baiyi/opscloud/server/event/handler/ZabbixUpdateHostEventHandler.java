package com.baiyi.opscloud.server.event.handler;

import com.baiyi.opscloud.server.bo.ZabbixEventBO;
import com.baiyi.opscloud.server.event.listener.ZabbixUpdateHostEventListener;
import com.google.common.eventbus.AsyncEventBus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/20 3:57 下午
 * @Since 1.0
 */

@Slf4j
@Component
public class ZabbixUpdateHostEventHandler {

    @Resource
    private AsyncEventBus asyncEventBus;

    @Resource
    private ZabbixUpdateHostEventListener zabbixUpdateHostEventListener;

    @PostConstruct
    public void init() {
        asyncEventBus.register(zabbixUpdateHostEventListener);
    }

    @PreDestroy
    public void destroy() {
        asyncEventBus.unregister(zabbixUpdateHostEventListener);
    }

    public void eventPost(ZabbixEventBO.HostUpdate host) {
        asyncEventBus.post(host);
        log.info("Zabbix更新主机事件触发，ip:{}", host.getOcServer().getPrivateIp());
    }
}
