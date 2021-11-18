package com.baiyi.opscloud.datasource.zabbix;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.datasource.zabbix.base.BaseZabbixTest;
import com.baiyi.opscloud.zabbix.entity.ZabbixHost;
import com.baiyi.opscloud.zabbix.entity.ZabbixHostTag;
import com.baiyi.opscloud.zabbix.datasource.ZabbixHostDatasource;
import com.baiyi.opscloud.zabbix.datasource.ZabbixHostTagDatasource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/8/23 4:02 下午
 * @Version 1.0
 */
@Slf4j
public class ZabbixHostTest extends BaseZabbixTest {

    @Resource
    private ZabbixHostDatasource zabbixHostHandler;

    @Resource
    private ZabbixHostTagDatasource zabbixHostTagHandler;

    @Test
    void getHostByIp() {
        ZabbixHost zabbixHost = zabbixHostHandler.getByIp(getConfig().getZabbix(), "172.16.4.166");
        ZabbixHostTag zabbixHostTag = zabbixHostTagHandler.getHostTag(getConfig().getZabbix(),zabbixHost);
        log.info(JSON.toJSONString(zabbixHost));
        log.info(JSON.toJSONString(zabbixHostTag ));
    }


}
