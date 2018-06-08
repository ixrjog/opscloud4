package com.sdg.cmdb.service;

import com.sdg.cmdb.service.impl.EcsCreateServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by liangjian on 2017/4/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class EcsCreateServiceTest {

    @Resource
    private EcsCreateService ecsCreateService;

    @Resource
    private EcsCreateServiceImpl ecsCreateServiceImpl;

    @Test
    public void test(){
      // System.err.println( ecsCreateService.create(null));
    }

    @Test
    public void testQuery(){
        ecsCreateService.queryImages(null,"m-bp1");
    }

    @Test
    public void testAllocatePublicIpAddress(){
        System.err.println(ecsCreateService.allocateIpAddress("i-bp"));
    }

    @Test
    public void testAllocateIpAddress(){

        ecsCreateServiceImpl.allocatePublicIpAddress("i-bp1g");
    }



}
