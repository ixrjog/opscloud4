package com.sdg.cmdb.service.impl;


import com.sdg.cmdb.service.JenkinsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class JenkinsServiceImplTest {


    @Resource
    protected JenkinsServiceImpl jenkinsServiceImpl;




    @Test
    public void testGetJobs() {
        jenkinsServiceImpl.getJobs();
    }




}
