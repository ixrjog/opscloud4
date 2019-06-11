package com.sdg.cmdb.service.impl;


import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.helper.JenkinsVersion;
import com.offbytwo.jenkins.model.*;


import com.sdg.cmdb.domain.ci.CiBuildDO;
import com.sdg.cmdb.domain.ci.jobParametersYaml.JobControllerYaml;
import com.sdg.cmdb.domain.ci.jobParametersYaml.JobParameterYaml;
import com.sdg.cmdb.domain.ci.jobParametersYaml.JobParametersYaml;
import com.sdg.cmdb.service.*;
import com.sdg.cmdb.util.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@Service
public class JenkinsServiceImpl implements JenkinsService, InitializingBean {

    private static final Logger coreLogger = LoggerFactory.getLogger("coreLogger");
    private static final Logger logger = LoggerFactory.getLogger(JenkinsServiceImpl.class);

    private static JenkinsServer jenkinsServer;

    @Value(value = "${jenkins.url}")
    private String jenkinsUrl;

    @Value(value = "${jenkins.user}")
    private String jenkinsUser;

    @Value(value = "${jenkins.token}")
    private String jenkinsToken;


    @Value("#{cmdb['invoke.env']}")
    private String invokeEnv;

    private boolean createJob(String jobName, String jobXml) {
        try {
            jenkinsServer.createJob(jobName, jobXml, true);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取job
     *
     * @param jobName
     * @return
     */
    public Job getJob(String jobName) {
        Map<String, Job> jobMap = getJobs();
        if (jobMap.containsKey(jobName)) {
            // 存在
            return jobMap.get(jobName);
        } else {
            // 不存在
            return null;
        }
    }

    /**
     * 查询所有job
     *
     * @return
     */
    @Override
    public Map<String, Job> getJobs() {
        Map<String, Job> jobMap = new HashMap<>();
        try {
            jobMap = jenkinsServer.getJobs();
            return jobMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jobMap;
    }

    @Override
    public JobWithDetails build(Job job, HashMap<String, String> paramList) {
        coreLogger.info("Jenkins job={} run build", job.getName());
        try {
            QueueReference queueReference = job.build(paramList, true);
            JobWithDetails jobWithDetails = job.details();
            //Build build = jobWithDetails.getLastBuild();
            //BuildWithDetails buildWithDetails = build.details();
            // TODO 返回构建任务
            return jobWithDetails;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public BuildWithDetails getBuildDetail(CiBuildDO ciBuildDO) {
        if (ciBuildDO.getBuildNumber() <= 0) return null;
        JobWithDetails jd = getJobDetails(ciBuildDO.getJobName());
        if (jd == null) return null;
        for (Build build : jd.getBuilds()) {
            if (build.getNumber() == ciBuildDO.getBuildNumber())
                try {
                    return build.details();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
        }
        return null;
    }

    @Override
    public JobWithDetails getJobDetails(String jobName) {
        try {
            JobWithDetails jobWithDetails = jenkinsServer.getJob(jobName);
            return jobWithDetails;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getJobXml(String jobName) {
        try {
            String jobXml = jenkinsServer.getJobXml(jobName);
            return jobXml;
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public boolean createJobByTemplate(String jobName, String templateName) {
        try {
            String jobXml = jenkinsServer.getJobXml(templateName);
            return createJob(jobName, jobXml);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean createJobByTemplate(String jobName, String templateName, JobParametersYaml jobYaml) {
        try {
            String jobXml = jenkinsServer.getJobXml(templateName);
            return createJob(jobName, getJobXml(jobXml,jobYaml));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String invokeParams(String jobXml, String invokeParams, List<JobParameterYaml> jobParameterYamls){
        Map<String, String> valuesMap = new HashMap<String, String>();
        String values[] = invokeParams.split(",");
        for(String v: values){
            for(JobParameterYaml jpy:jobParameterYamls){
                if(jpy.getName().equals(v)){
                    valuesMap.put(v, jpy.getValue());
                    continue;
                }
            }
        }
        StrSubstitutor sub = new StrSubstitutor(valuesMap);
        return sub.replace(jobXml);

    }

    @Override
    public boolean updateJob(String jobName, String templateName) {
        try {
            String jobXml = jenkinsServer.getJobXml(templateName);
            jenkinsServer.updateJob(jobName, jobXml, true);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public  boolean updateJob(String jobName, String templateName,JobParametersYaml jobYaml){
        try {
            String jobXml = jenkinsServer.getJobXml(templateName);
            jenkinsServer.updateJob(jobName, getJobXml(jobXml,jobYaml), true);
            return  true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String getJobXml(String jobXml,JobParametersYaml jobYaml){
        if (jobYaml.getControllers() != null && jobYaml.getControllers().size() != 0) {
            // 遍历控制器
            for(JobControllerYaml controller:jobYaml.getControllers()){
                if(controller.getName().equals("@InvokeParams"))
                    jobXml= invokeParams(jobXml,controller.getValue(),jobYaml.getParameters());
            }
        }
        return jobXml;
    }

    @Override
    public String getJobXmlMd5(String jobName) {
        String xml = getJobXml(jobName);
        if (StringUtils.isEmpty(xml)) return "";
        return EncryptionUtil.md5(xml);
    }

    @Override
    public JenkinsVersion version() {
        JenkinsVersion version = new JenkinsVersion();
        if (jenkinsServer.isRunning()) {
            version = jenkinsServer.getVersion();
        }
        return version;
    }

    @Override
    public List<Plugin> getPlugin() {
        try {
            PluginManager pluginManager = jenkinsServer.getPluginManager();
            List<Plugin> list = pluginManager.getPlugins();
            return list;
        } catch (Exception e) {
            return null;
        }

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            URI host = new URI(jenkinsUrl);
            JenkinsServer jenkins = new JenkinsServer(host, jenkinsUser, jenkinsToken);
            this.jenkinsServer = jenkins;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
