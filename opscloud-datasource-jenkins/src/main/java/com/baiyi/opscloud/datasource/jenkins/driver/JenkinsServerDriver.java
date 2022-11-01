package com.baiyi.opscloud.datasource.jenkins.driver;

import com.baiyi.opscloud.common.datasource.JenkinsConfig;
import com.baiyi.opscloud.datasource.jenkins.JenkinsServer;
import com.baiyi.opscloud.datasource.jenkins.helper.JenkinsVersion;
import com.baiyi.opscloud.datasource.jenkins.model.Computer;
import com.baiyi.opscloud.datasource.jenkins.model.FolderJob;
import com.baiyi.opscloud.datasource.jenkins.model.Job;
import com.baiyi.opscloud.datasource.jenkins.server.JenkinsServerBuilder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/7/2 9:55 上午
 * @Version 1.0
 */
public class JenkinsServerDriver {

    public static Map<String, Computer> getComputers(JenkinsConfig.Jenkins jenkins) throws URISyntaxException, IOException {
        JenkinsServer jenkinsServer = JenkinsServerBuilder.build(jenkins);
        return jenkinsServer.getComputers();

    }

    public static Map<String, Job> getJobs(JenkinsConfig.Jenkins jenkins) throws URISyntaxException, IOException {
        JenkinsServer jenkinsServer = JenkinsServerBuilder.build(jenkins);
        return jenkinsServer.getJobs();
    }

    public static Map<String, Job> getJobs(JenkinsConfig.Jenkins jenkins, FolderJob folder) throws URISyntaxException, IOException {
        JenkinsServer jenkinsServer = JenkinsServerBuilder.build(jenkins);
        return jenkinsServer.getJobs(folder);
    }

    //    @Retryable(value = Exception.class, maxAttempts = 5, backoff = @Backoff(delay = 1000))
    public static JenkinsVersion getVersion(JenkinsConfig.Jenkins jenkins) throws URISyntaxException, IOException {
        JenkinsServer jenkinsServer = JenkinsServerBuilder.build(jenkins);
        return jenkinsServer.getVersion();
    }

}
