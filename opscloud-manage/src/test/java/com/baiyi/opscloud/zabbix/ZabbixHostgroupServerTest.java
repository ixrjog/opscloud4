package com.baiyi.opscloud.zabbix;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.account.impl.ZabbixAccount;
import com.baiyi.opscloud.zabbix.entry.ZabbixHostgroup;
import com.baiyi.opscloud.zabbix.server.ZabbixHostgroupServer;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/1/8 1:16 下午
 * @Version 1.0
 */
public class ZabbixHostgroupServerTest extends BaseUnit {

    @Resource
    private ZabbixHostgroupServer zabbixHostgroupServer;

    @Test
    void testCreateHostgroup() {
        ZabbixHostgroup group = zabbixHostgroupServer.createHostgroup(ZabbixAccount.ZABBIX_DEFAULT_HOSTGROUP);
        System.err.println(JSON.toJSONString(group));
    }

    @Test
    void testGetHostgroup() {
        ZabbixHostgroup group = zabbixHostgroupServer.getHostgroup(ZabbixAccount.ZABBIX_DEFAULT_HOSTGROUP);
        System.err.println(JSON.toJSONString(group));
    }
}
