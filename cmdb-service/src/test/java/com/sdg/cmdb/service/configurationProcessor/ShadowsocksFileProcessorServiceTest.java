package com.sdg.cmdb.service.configurationProcessor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class ShadowsocksFileProcessorServiceTest {

    @Resource
    private ShadowsocksFileProcessorService shadowsocksFileProcessorService;


    @Test
    public void test() {
        System.err.println( shadowsocksFileProcessorService.getConfig());
    }

}
