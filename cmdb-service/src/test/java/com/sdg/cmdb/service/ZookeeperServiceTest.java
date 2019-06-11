package com.sdg.cmdb.service;


import com.sdg.cmdb.domain.dubbo.DubboProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class ZookeeperServiceTest {


    @Autowired
    private ZookeeperService zookeeperService;

    @Test
    public void test() {
        HashMap<String,DubboProvider> map=  zookeeperService.getProviderMap();
        for(String key:map.keySet())
            System.err.println(map.get(key));
    }

}
