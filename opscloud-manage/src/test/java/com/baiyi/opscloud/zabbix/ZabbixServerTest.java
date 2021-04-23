package com.baiyi.opscloud.zabbix;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.service.server.OcServerGroupService;
import com.baiyi.opscloud.zabbix.entry.ZabbixAction;
import com.baiyi.opscloud.zabbix.server.ZabbixServer;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/1/2 9:38 上午
 * @Version 1.0
 */
public class ZabbixServerTest extends BaseUnit {

    @Resource
    private ZabbixServer zabbixServer;

    @Resource
    private OcServerGroupService ocServerGroupService;
    
    @Test
    void testGetAction() {
        ZabbixAction action = zabbixServer.getAction("users_data-center");
        System.err.println(JSON.toJSONString(action));
    }


    @Test
    void testDeleteAction() {
        zabbixServer.deleteAction("users_ansible");
    }

    @Test
    void testCreateAction() {
        zabbixServer.createAction("users_ansible");
    }

    /**
     * 全量更新Action
     */
    @Test
    void test() {
        ocServerGroupService.queryAll().forEach(e -> {
            String name = e.getName().replace("group_", "users_");
            ZabbixAction zabbixAction = zabbixServer.getAction(name);
            if(zabbixAction != null){
                System.err.println("Delete " + name);
                zabbixServer.deleteAction(name);
            }
            zabbixServer.createAction(name);
        });
    }

}
