package com.sdg.cmdb.service.impl;


<<<<<<< HEAD
import com.offbytwo.jenkins.model.Artifact;
import com.offbytwo.jenkins.model.Job;
import com.sdg.cmdb.dao.cmdb.JenkinsDao;
import com.sdg.cmdb.domain.jenkins.JenkinsProjectsDO;
import com.sdg.cmdb.domain.jenkins.JobNoteDO;
import com.sdg.cmdb.domain.jenkins.JobNoteVO;
=======
>>>>>>> develop
import com.sdg.cmdb.service.JenkinsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
<<<<<<< HEAD
import java.io.IOException;
import java.net.URL;
import java.util.*;
=======
>>>>>>> develop

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class JenkinsServiceImplTest {


    @Resource
    protected JenkinsServiceImpl jenkinsServiceImpl;




    @Test
    public void testGetJobs() {
        jenkinsServiceImpl.getJobs();
    }

<<<<<<< HEAD
    @Test
    public void testJobNotes() {
        //
        List<JobNoteDO> list = jenkinsDao.queryJobNoteByJobNameAndBuildNumber("one.distribution.daily", 66);
        for (JobNoteDO note : list) {
            //   jenkinsService. dingtalkNotes(note);
        }

    }

    @Test
    public void testQueryJob() {
        Map<String, Job> map = jenkinsService.getJobs();

        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String)entry.getKey();
            Job job = (Job)entry.getValue();
            System.err.println(job.getName());
        }
    }


    @Test
    public void testQueryJobTask() {
        Map<String, Job> map = jenkinsService.getJobs();

        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String)entry.getKey();
            Job job = (Job)entry.getValue();
            System.err.println(job.getName());
            try{
                List<Artifact>  as= job.details().getLastBuild().details().getArtifacts();
                for(Artifact artifact :as){
                    System.err.println( artifact.getDisplayPath());
                    System.err.println( artifact.getFileName());
                    System.err.println( artifact.getRelativePath());
                }

            }catch (IOException ie){

            }
        }
    }


    @Test
    public void testCreateJob() {
        System.err.println(jenkinsService.createFtJob("one.demo.daily", 2));

    }

    @Test
    public void testUrl() {
        //"http://gitlab.51xianqu.com/one/distribution"
        try {
            URL url = new URL("http://gitlab.51xianqu.com/one/distribution");
            System.err.println(url.getHost());

            System.err.println(url.getPath());
            String path = url.getPath();
            String s[] = path.split("/");

            System.err.println(s[1]);


        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    @Test
    public void testString() {
        //"http://gitlab.51xianqu.com/one/distribution"
        try {
            String repositoryUrl = "git@gitlab.51xianqu.com:one/distribution.git";
            URL url = new URL("http://gitlab.51xianqu.com/one/distribution");
            String[] r = repositoryUrl.split(":");
            String[] p = r[1].split("/");
            System.err.println(p[0]);


        } catch (Exception e) {
            e.printStackTrace();

        }
=======
>>>>>>> develop



}
