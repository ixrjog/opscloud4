package com.sdg.cmdb.service;

import com.offbytwo.jenkins.helper.JenkinsVersion;
import com.offbytwo.jenkins.model.BuildWithDetails;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.JobWithDetails;
import com.offbytwo.jenkins.model.Plugin;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.ci.CiBuildDO;
import com.sdg.cmdb.domain.ci.jobParametersYaml.JobParametersYaml;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface JenkinsService {

    Map<String, Job> getJobs();


    // TODO 新版本

    Job getJob(String jobName);

    JobWithDetails build(Job job, HashMap<String, String> paramList);

    /**
     * 查询任务详情
     * @return
     */
    BuildWithDetails getBuildDetail(CiBuildDO ciBuildDO);

    /**
     * 按模版更新Job
     * @param jobName
     * @param templateName
     * @return
     */
    boolean updateJob(String jobName, String templateName);

    boolean updateJob(String jobName, String templateName,JobParametersYaml jobYaml);

    JenkinsVersion version();

    List<Plugin> getPlugin();


    JobWithDetails getJobDetails(String jobName) ;

    String  getJobXml(String jobName);

    boolean createJobByTemplate(String jobName, String templateName);

    boolean createJobByTemplate(String jobName, String templateName,JobParametersYaml jobYaml);

    /**
     * 计算job配置文件Hash(MD%)
     * @param jobName
     * @return
     */
    String  getJobXmlMd5(String jobName);

}
