package com.sdg.cmdb.service.impl;


import com.offbytwo.jenkins.model.Job;
import com.sdg.cmdb.dao.cmdb.JenkinsDao;
import com.sdg.cmdb.domain.jenkins.JenkinsProjectsDO;
import com.sdg.cmdb.domain.jenkins.JobNoteDO;
import com.sdg.cmdb.domain.jenkins.JobNoteVO;
import com.sdg.cmdb.service.JenkinsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.net.URL;
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class JenkinsServiceImplTest {


    @Resource
    protected JenkinsServiceImpl jenkinsServiceImpl;

    @Resource
    protected JenkinsService jenkinsService;

    @Resource
    protected JenkinsDao jenkinsDao;

    @Test
    public void testGetJobs() {
        jenkinsServiceImpl.getJobs();
    }

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

    }

    @Test
    public void testDate() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        System.err.println(c.get(Calendar.YEAR));

        System.err.println(c.get(Calendar.MONTH));

        System.err.println(c.get(Calendar.DAY_OF_MONTH));


        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("MMdd_HHmmss");
        java.util.Date currentTime = new java.util.Date();//得到当前系统时间
        String str_date1 = formatter.format(currentTime); //将日期时间格式化
        //String str_date2 = currentTime.toString(); //将Date型日期时间转换成字符串形式
        System.err.println(str_date1);
        //System.err.println(str_date2) ;

    }

    @Test
    public void testSaveProjects() {
        JenkinsProjectsDO project = new JenkinsProjectsDO();
        project.setProjectName("aaa");
        project.setBuildType(1);
        project.setRepositoryUrl("bbb");
        project.setContent("cccc");

        jenkinsServiceImpl.saveProject(project);


    }


    // RefsVO updateJobRefs(long id);

    @Test
    public void testUpdateJobRefs() {
        // git@gitlab.51xianqu.com:xq_ios/xq_jianhuo.git
        long id=163;

        System.err.println(jenkinsServiceImpl.updateJobRefs(id));


    }
}
