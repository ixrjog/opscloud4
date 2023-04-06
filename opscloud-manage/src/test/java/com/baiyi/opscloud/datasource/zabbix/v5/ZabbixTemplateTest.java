package com.baiyi.opscloud.datasource.zabbix.v5;

import com.baiyi.opscloud.datasource.zabbix.base.BaseZabbixTest;
import com.baiyi.opscloud.zabbix.v5.driver.ZabbixV5TemplateDriver;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixTemplate;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/11/22 10:26 上午
 * @Version 1.0
 */
public class ZabbixTemplateTest extends BaseZabbixTest {

    @Resource
    private ZabbixV5TemplateDriver zabbixV5TemplateDatasource;

    @Test
    void listTemplateTest() {
        List<ZabbixTemplate.Template> templates = zabbixV5TemplateDatasource.list(getConfig().getZabbix());
        print(templates);
    }

    @Test
    void getByNameTest() {
        ZabbixTemplate.Template template = zabbixV5TemplateDatasource.getByName(getConfig().getZabbix(), "Template OS Linux by Zabbix agent");
        print(template);
    }

    @Test
    void getByIdTest() {
        ZabbixTemplate.Template template1 = zabbixV5TemplateDatasource.getByName(getConfig().getZabbix(), "Template OS Linux by Zabbix agent");
        ZabbixTemplate.Template template2 = zabbixV5TemplateDatasource.getById(getConfig().getZabbix(), template1.getTemplateid());
        print(template2);
    }

}
