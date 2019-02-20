package com.sdg.cmdb.service;

import com.sdg.cmdb.domain.configCenter.ConfigCenterDO;
import com.sdg.cmdb.domain.configCenter.ConfigCenterItemGroupEnum;
import com.sdg.cmdb.domain.configCenter.itemEnum.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * Created by liangjian on 2017/5/26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class ConfigCenterServiceTest {

    @Resource
    private ConfigCenterService configCenterService;

    @Value("#{cmdb['invoke.env']}")
    private String invokeEnv;


    @Test
    public void testEnv() {
        System.err.println(invokeEnv);

    }




    @Test
    public void testGetConfigCenterItemGroup() {

        HashMap<String, ConfigCenterDO> map = configCenterService.getConfigCenterItemGroup("ZABBIX","");
        for (String key : map.keySet()) {
            ConfigCenterDO c = map.get(key);
            System.err.println(c);
        }

    }


}
