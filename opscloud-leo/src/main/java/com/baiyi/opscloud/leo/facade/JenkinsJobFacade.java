package com.baiyi.opscloud.leo.facade;

import com.baiyi.opscloud.common.datasource.JenkinsConfig;
import com.baiyi.opscloud.datasource.jenkins.driver.JenkinsServerDriver;
import com.baiyi.opscloud.datasource.jenkins.model.FolderJob;
import com.baiyi.opscloud.datasource.jenkins.model.Job;
import com.baiyi.opscloud.leo.domain.model.LeoTemplateModel;
import com.baiyi.opscloud.leo.exception.LeoTemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2022/11/14 11:23
 * @Version 1.0
 */
@Slf4j
@Component
public class JenkinsJobFacade {

    /**
     * 获取Job内容
     *
     * @param templateConfig
     * @param jenkinsConfig
     * @param folder
     * @return
     */
    public String getJobXml(LeoTemplateModel.TemplateConfig templateConfig, JenkinsConfig jenkinsConfig, String folder) {
        try {
            if (StringUtils.isNotBlank(folder)) {
                // 重新拼装URL
                // https://leo-jenkins-1.chuanyinet.com/job/templates/job/tpl_test/ =>
                // https://leo-jenkins-1.chuanyinet.com/job/templates/
                final String folderUrl = templateConfig.getTemplate().toFolderURL();
                FolderJob folderJob = new FolderJob(folder, folderUrl);
                log.debug("查询JobXml: jenkinsUrl={}, folderJobUrl={}, templateName={}", jenkinsConfig.getJenkins().getUrl(), folderJob.getUrl(), templateConfig.getTemplate().getName());
                return JenkinsServerDriver.getJobXml(jenkinsConfig.getJenkins(), folderJob, templateConfig.getTemplate().getName());
            } else {
                log.debug("查询JobXml: jenkinsUrl={}, templateName={}", jenkinsConfig.getJenkins().getUrl(), templateConfig.getTemplate().getName());
                return JenkinsServerDriver.getJobXml(jenkinsConfig.getJenkins(), templateConfig.getTemplate().getName());
            }
        } catch (URISyntaxException | IOException e) {
            throw new LeoTemplateException("查询Jenkins任务模板错误: {}", e.getMessage());
        }
    }

    public void createJob(LeoTemplateModel.TemplateConfig templateConfig, JenkinsConfig jenkinsConfig, String folder, String jobXml) {
        try {
            if (StringUtils.isNotBlank(folder)) {
                // 重新拼装URL
                // https://leo-jenkins-1.chuanyinet.com/job/templates/job/tpl_test/ =>
                // https://leo-jenkins-1.chuanyinet.com/job/templates/
                final String folderUrl = templateConfig.getTemplate().toFolderURL();
                FolderJob folderJob = new FolderJob(folder, folderUrl);
                Map<String, Job> jobMap = JenkinsServerDriver.getJobs(jenkinsConfig.getJenkins(), folderJob);
                if (!jobMap.containsKey(templateConfig.getTemplate().getName())) {
                    JenkinsServerDriver.createJob(jenkinsConfig.getJenkins(), folderJob, templateConfig.getTemplate().getName(), jobXml);
                }
            } else {
                JenkinsServerDriver.createJob(jenkinsConfig.getJenkins(), templateConfig.getTemplate().getName(), jobXml);
            }
        } catch (URISyntaxException | IOException e) {
            throw new LeoTemplateException("查询Jenkins任务模板错误: {}", e.getMessage());
        }
    }

}