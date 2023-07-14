package com.baiyi.opscloud.leo.helper;

import com.baiyi.opscloud.common.datasource.JenkinsConfig;
import com.baiyi.opscloud.datasource.jenkins.driver.JenkinsServerDriver;
import com.baiyi.opscloud.datasource.jenkins.model.FolderJob;
import com.baiyi.opscloud.leo.domain.model.LeoTemplateModel;
import com.baiyi.opscloud.leo.exception.LeoTemplateException;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @Author baiyi
 * @Date 2022/11/14 11:23
 * @Version 1.0
 */
@Slf4j
@Component
public class JenkinsJobHelper {

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
                URL url = new URL(templateConfig.getTemplate().getUrl());
                final String folderUrl = Joiner.on("").skipNulls().join(url.getProtocol(), "://", url.getHost(), url.getPort() == -1 ? null : ":" + url.getPort(), "/job/", folder, "/");
                FolderJob folderJob = new FolderJob(folder, folderUrl);
                log.info("查询JobXml: jenkinsUrl={}, folderJobUrl={}, templateName={}", jenkinsConfig.getJenkins().getUrl(), folderJob.getUrl(), templateConfig.getTemplate().getName());
                return JenkinsServerDriver.getJobXml(jenkinsConfig.getJenkins(), folderJob, templateConfig.getTemplate().getName());
            } else {
                log.info("查询JobXml: jenkinsUrl={}, templateName={}", jenkinsConfig.getJenkins().getUrl(), templateConfig.getTemplate().getName());
                return JenkinsServerDriver.getJobXml(jenkinsConfig.getJenkins(), templateConfig.getTemplate().getName());
            }
        } catch (URISyntaxException | IOException e) {
            throw new LeoTemplateException("查询Jenkins任务模板错误: {}", e.getMessage());
        }
    }
}