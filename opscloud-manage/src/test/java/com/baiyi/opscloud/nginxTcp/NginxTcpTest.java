package com.baiyi.opscloud.nginxTcp;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.facade.NginxTcpServerFacade;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/10/19 1:45 下午
 * @Version 1.0
 */
public class NginxTcpTest extends BaseUnit {

    @Resource
    private NginxTcpServerFacade nginxTcpServerFacade;

    @Test
    void testBuildNginxDubboTcpServerByEnv() {
        String body = nginxTcpServerFacade.buildNginxDubboTcpServerByEnv("dev");
        System.err.println(body);
    }

    @Test
    void testWriteNginxDubboTcpServerByEnv() {
        nginxTcpServerFacade.writeNginxDubboTcpServerConfByEnv("dev");
        nginxTcpServerFacade.writeNginxDubboTcpServerConfByEnv("daily");
    }
}
