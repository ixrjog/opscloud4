package com.baiyi.opscloud.zabbix;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.zabbix.entry.ZabbixUser;
import com.baiyi.opscloud.zabbix.entry.ZabbixUsergroup;
import com.baiyi.opscloud.zabbix.server.ZabbixUserServer;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/1/8 1:13 下午
 * @Version 1.0
 */
public class ZabbixUserServerTest extends BaseUnit {

    @Resource
    private ZabbixUserServer zabbixUserServer;

    @Test
    void testGetUser() {
        ZabbixUser user = zabbixUserServer.getUser("baiyi");
        System.err.println(JSON.toJSONString(user));
    }

    @Test
    void testGetUsergroup() {
        ZabbixUsergroup usergroup = zabbixUserServer.getUsergroup("users_default");
        System.err.println(JSON.toJSONString(usergroup));
    }
}
