package com.sdg.cmdb.service;

import com.offbytwo.jenkins.helper.JenkinsVersion;
import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.JobWithDetails;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.gitlab.GitlabWebHooksDO;
import com.sdg.cmdb.domain.gitlab.RefsVO;
import com.sdg.cmdb.domain.jenkins.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface JenkinsService {

    Map<String, Job> getJobs();


    // TODO 新版本

    Job getJob(String jobName);

    JobWithDetails build(Job job, HashMap<String, String> paramList);

    /**
     * 按模版更新Job
     * @param jobName
     * @param templateName
     * @return
     */
    boolean updateJob(String jobName, String templateName);

    JenkinsVersion version();


    JobWithDetails getJobDetails(String jobName) ;

    String  getJobXml(String jobName);

    boolean createJobByTemplate(String jobName, String templateName);

    /**
     * 计算job配置文件Hash(MD%)
     * @param jobName
     * @return
     */
    String  getJobXmlMd5(String jobName);

}
