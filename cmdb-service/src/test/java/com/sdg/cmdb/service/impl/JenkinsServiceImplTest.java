package com.sdg.cmdb.service.impl;


import com.alibaba.fastjson.JSON;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.BuildWithDetails;
import com.offbytwo.jenkins.model.JobWithDetails;
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

    @Test
    public void test() {
        jenkinsServiceImpl.getPlugin();
    }


    @Test
    public void testGetJob() {
        try{
            JobWithDetails jd= jenkinsServiceImpl.getJobDetails("zebraprime-2.3.1");
            for( Build build :jd.getBuilds()){
              if( build.getNumber() == 8) {
                  BuildWithDetails bd= build.details();
                  System.err.println(bd.getActions());

                  break;
              }
            }


            System.err.println(JSON.toJSONString(jd));

        }catch (Exception e){
            e.printStackTrace();
        }


    }


}
