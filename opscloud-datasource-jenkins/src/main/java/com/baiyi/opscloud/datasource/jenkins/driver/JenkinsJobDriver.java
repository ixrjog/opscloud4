package com.baiyi.opscloud.datasource.jenkins.driver;

import com.baiyi.opscloud.common.datasource.JenkinsConfig;
import com.baiyi.opscloud.datasource.jenkins.JenkinsServer;
import com.baiyi.opscloud.datasource.jenkins.model.JobWithDetails;
import com.baiyi.opscloud.datasource.jenkins.model.QueueReference;
import com.baiyi.opscloud.datasource.jenkins.server.JenkinsServerBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2022/11/8 14:46
 * @Version 1.0
 */
@Component
public class JenkinsJobDriver {

    public QueueReference buildJobWithParams(JenkinsConfig.Jenkins jenkins, String jobName, Map<String, String> params) throws URISyntaxException, IOException {
        assert jenkins != null;
        boolean crumbFlag = Optional.of(jenkins)
                .map(JenkinsConfig.Jenkins::getSecurity)
                .map(JenkinsConfig.Security::getCrumbFlag)
                .orElse(false);
        JenkinsServer jenkinsServer = JenkinsServerBuilder.build(jenkins);
        JobWithDetails job = jenkinsServer.getJob(jobName);
        return job.build(params, false);
    }

    /**`
     * 创建Job
     *
     * @param jenkins
     * @param jobName 名称
     * @param jobXml  内容
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
    public JenkinsServer createJob(JenkinsConfig.Jenkins jenkins, String jobName, String jobXml) throws URISyntaxException, IOException {
        JenkinsServer jenkinsServer = JenkinsServerBuilder.build(jenkins);
        return jenkinsServer.createJob(jobName, jobXml);
    }

}
