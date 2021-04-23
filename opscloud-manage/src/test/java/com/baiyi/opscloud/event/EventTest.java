package com.baiyi.opscloud.event;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.event.handler.OcServerEventHandler;
import com.baiyi.opscloud.service.server.OcServerService;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/22 4:08 下午
 * @Since 1.0
 */
public class EventTest extends BaseUnit {

    @Resource
    private OcServerEventHandler ocServerEventHandler;

    @Resource
    private OcServerService ocServerService;

    @Test
    public void ocServerEventTest() {
        OcServer ocServer = ocServerService.queryOcServerByPrivateIp("172.16.201.24");
        ocServerEventHandler.eventPost(ocServer);
    }
}
