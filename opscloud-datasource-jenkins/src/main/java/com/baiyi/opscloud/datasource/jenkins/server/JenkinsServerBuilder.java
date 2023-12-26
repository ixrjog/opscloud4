package com.baiyi.opscloud.datasource.jenkins.server;

import com.baiyi.opscloud.common.datasource.JenkinsConfig;
import com.baiyi.opscloud.datasource.jenkins.JenkinsServer;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @Author baiyi
 * @Date 2021/7/1 2:10 下午
 * @Version 1.0
 */
public final class JenkinsServerBuilder {

    private JenkinsServerBuilder() {
    }

    public static JenkinsServer build(JenkinsConfig.Jenkins jenkins) throws URISyntaxException {
        return new JenkinsServer(new URI(jenkins.getUrl()),
                jenkins.getUsername(), jenkins.getToken());
    }

}