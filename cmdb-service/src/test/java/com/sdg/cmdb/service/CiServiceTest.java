package com.sdg.cmdb.service;


import com.offbytwo.jenkins.helper.JenkinsVersion;
import com.offbytwo.jenkins.model.Job;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class CiServiceTest {

    @Autowired
    private CiService ciService;

    @Autowired
    private JenkinsService jenkinsService;

    @Test
    public void testVersion() {
        JenkinsVersion version = ciService.getJenkinsVersion();
        System.err.println(version);
    }


    @Test
    public void testGetJobs() {
        Map<String, Job> map = jenkinsService.getJobs();
        for(String key:map.keySet()){
            System.err.println(key);
        }

    }



}
