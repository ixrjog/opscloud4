package com.baiyi.opscloud.datasource.jenkins.driver;

import com.baiyi.opscloud.common.datasource.JenkinsConfig;
import com.baiyi.opscloud.datasource.jenkins.JenkinsServer;
import com.baiyi.opscloud.datasource.jenkins.helper.JenkinsVersion;
import com.baiyi.opscloud.datasource.jenkins.model.Computer;
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
        Map<String, Computer> result;
        try {
            result = jenkinsServer.getComputers();
        } catch (IOException e) {
            jenkinsServer.close();
            throw new IOException(e.getMessage());
        }
        return result;
    }

    //    @Retryable(value = Exception.class, maxAttempts = 5, backoff = @Backoff(delay = 1000))
    public static JenkinsVersion getVersion(JenkinsConfig.Jenkins jenkins) throws URISyntaxException, IOException{
        JenkinsServer jenkinsServer = JenkinsServerBuilder.build(jenkins);
        try {
            return jenkinsServer.getVersion();
        } catch (Exception e) {
            return null;
        }
    }

}
