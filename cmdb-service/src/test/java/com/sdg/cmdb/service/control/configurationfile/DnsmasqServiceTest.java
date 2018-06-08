package com.sdg.cmdb.service.control.configurationfile;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by liangjian on 2017/7/11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class DnsmasqServiceTest {

    @Resource
    private DnsmasqService dnsmasqService;

    @Test
    public void test() {
      System.err.println(dnsmasqService.acqConfig(1));
    }



}
