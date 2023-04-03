package com.baiyi.opscloud.datasource.zabbix.v5;

import com.baiyi.opscloud.datasource.zabbix.base.BaseZabbixTest;
import com.baiyi.opscloud.zabbix.v5.driver.ZabbixV5UserDriver;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixUser;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/11/22 10:52 上午
 * @Version 1.0
 */
public class ZabbixUserTest extends BaseZabbixTest {

    @Resource
    private ZabbixV5UserDriver zabbixV5UserDatasource;

    @Test
    void listTest() {
        List<ZabbixUser.User> users = zabbixV5UserDatasource.list(getConfig().getZabbix());
        print(users);
    }

    @Test
    void getByUsernameTest() {
        ZabbixUser.User user = zabbixV5UserDatasource.getByUsername(getConfig().getZabbix(), "baiyi");
        print(user);
    }

}
