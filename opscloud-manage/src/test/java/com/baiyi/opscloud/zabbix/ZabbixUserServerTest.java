package com.baiyi.opscloud.zabbix;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.service.server.OcServerGroupService;
import com.baiyi.opscloud.zabbix.entry.ZabbixHostgroup;
import com.baiyi.opscloud.zabbix.entry.ZabbixMedia;
import com.baiyi.opscloud.zabbix.entry.ZabbixUser;
import com.baiyi.opscloud.zabbix.entry.ZabbixUsergroup;
import com.baiyi.opscloud.zabbix.server.ZabbixHostgroupServer;
import com.baiyi.opscloud.zabbix.server.ZabbixUserServer;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/8 1:13 下午
 * @Version 1.0
 */
public class ZabbixUserServerTest extends BaseUnit {

    @Resource
    private ZabbixUserServer zabbixUserServer;

    @Resource
    private OcServerGroupService ocServerGroupService;


    @Resource
    private ZabbixHostgroupServer zabbixHostgroupServer;

    @Test
    void testGetUser() {
        ZabbixUser user = zabbixUserServer.getUser("baiyi");
        System.err.println(JSON.toJSONString(user));
    }


    @Test
    void testUserUsrgrps() {
        BusinessWrapper wrapper = zabbixUserServer.getUserUsrgrps("baiyi");
        if (wrapper.isSuccess()) {
            List<ZabbixUsergroup> usergroups = (List<ZabbixUsergroup>) wrapper.getBody();
            System.err.println(JSON.toJSON(usergroups));
        }
    }

    @Test
    void testUserMediaids() {
        BusinessWrapper wrapper = zabbixUserServer.getUserMedias("baiyi");
        if (wrapper.isSuccess()) {
            List<ZabbixMedia> medias = (List<ZabbixMedia>) wrapper.getBody();
            System.err.println(JSON.toJSON(medias));
        }
    }

    @Test
    void testGetUsergroup() {
        ZabbixUsergroup usergroup = zabbixUserServer.getUsergroup("users_aibox");
        System.err.println(JSON.toJSONString(usergroup));
    }

    @Test
    void testCreateUsergroup() {
        ocServerGroupService.queryAll().forEach(g -> {
            ZabbixHostgroup zabbixHostgroup = zabbixHostgroupServer.getHostgroup(g.getName());
            ZabbixUsergroup zabbixUsergroup = zabbixUserServer.updateUsergroup(g.getName().replace("group_", "users_"), zabbixHostgroup);
            System.err.println(JSON.toJSONString(zabbixUsergroup));
        });
    }

}
