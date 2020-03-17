package com.baiyi.opscloud.zabbix;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.zabbix.handler.ZabbixHandler;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;


/**
 * @Author baiyi
 * @Date 2019/12/31 4:05 下午
 * @Version 1.0
 */

public class ZabbixHandlerTest extends BaseUnit {

    @Resource
    private ZabbixHandler zabbixHandler;

    @Test
    void testZabbixLogin() {
        System.err.println(zabbixHandler.login());
    }

}
