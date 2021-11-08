package com.baiyi.opscloud.datasource.zabbix;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.datasource.ZabbixDsInstanceConfig;
import com.baiyi.opscloud.core.factory.DsConfigHelper;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.service.datasource.DsConfigService;
import com.baiyi.opscloud.zabbix.entry.ZabbixHostGroup;
import com.baiyi.opscloud.zabbix.entry.ZabbixUserGroup;
import com.baiyi.opscloud.zabbix.facade.ZabbixFacade;
import com.baiyi.opscloud.zabbix.handler.ZabbixHostGroupHandler;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/8/12 11:41 上午
 * @Version 1.0
 */
public class ZabbixHostGroupTest extends BaseUnit {

    @Resource
    private DsConfigService dsConfigService;

    @Resource
    private DsConfigHelper dsFactory;

    @Resource
    private ZabbixHostGroupHandler zabbixHostGroupHandler;

    @Resource
    private ZabbixFacade zabbixFacade;

    @Test
    void t1() {
        ZabbixHostGroup group = zabbixHostGroupHandler.getByName(getConfig().getZabbix(), "group_trade");
        System.err.println(JSON.toJSONString(group));
    }

    @Test
    void t2() {
        ZabbixUserGroup zabbixUserGroup = zabbixFacade.getOrCreateUserGroup(getConfig().getZabbix(), "users_test1111");
        System.err.println(JSON.toJSONString(zabbixUserGroup));
    }

    @Test
    ZabbixDsInstanceConfig getConfig() {
        DatasourceConfig datasourceConfig = dsConfigService.getById(4);
        return dsFactory.build(datasourceConfig, ZabbixDsInstanceConfig.class);
    }

}
