package com.sdg.cmdb.service;


import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.BuildWithDetails;
import com.offbytwo.jenkins.model.JobWithDetails;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class JenkinsServiceTest {

    @Autowired
    private JenkinsService jenkinsService;


    @Test
    public void test() {
        JobWithDetails jd = jenkinsService.getJobDetails("java_opscloud_prod");
        Build build =  jd.getBuildByNumber(128);
        try{
            BuildWithDetails bd= build.details();
            System.err.println(bd);
        }catch (Exception e){
            e.printStackTrace();
        }

        System.err.println(jd);
    }


    @Test
    public void testUpdatJob() {
        boolean result = jenkinsService.updateJob("template_java-war_cd","template_java-war_cd");
        System.err.println(result);
    }

    @Test
    public void testJobMd5() {
        String md51 = jenkinsService.getJobXmlMd5("java_opscloud_prod");
        String md52 = jenkinsService.getJobXmlMd5("template_java-war_cd");
        String md53 = jenkinsService.getJobXmlMd5("test_1");
        System.err.println(md51 );
        System.err.println(md52 );
        System.err.println(md53 );
    }


    @Test
    public void testJobXml() {
        //String md51 = jenkinsService.getJobXml("java_opscloud_prod");
        String md51 = jenkinsService.getJobXml("test_1");
        System.err.println("----------------------------------");
        System.err.println(md51 );
        System.err.println("----------------------------------");
        //System.err.println(md52 );

    }

    @Test
    public void testCreateJob() {


        jenkinsService.createJobByTemplate("test_1","template_java-war_cd");
    }


}


