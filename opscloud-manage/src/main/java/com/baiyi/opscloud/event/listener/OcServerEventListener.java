package com.baiyi.opscloud.event.listener;

import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/22 4:02 下午
 * @Since 1.0
 */

@Slf4j
@Component
public class OcServerEventListener {

    @Subscribe
    public void jumpserverTest(OcServer ocServer) {
        log.error("jumpserver收到OcServerEvent,ip:{}", ocServer.getPrivateIp());
    }

    @Subscribe
    public void zabbixTest(OcServer ocServer) {
        log.error("zabbix收到OcServerEvent,ip:{}", ocServer.getPrivateIp());
    }




}
