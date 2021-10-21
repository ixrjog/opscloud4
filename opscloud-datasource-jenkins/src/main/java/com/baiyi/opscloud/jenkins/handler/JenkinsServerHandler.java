package com.baiyi.opscloud.jenkins.handler;

import com.baiyi.opscloud.common.datasource.JenkinsDsInstanceConfig;
import com.baiyi.opscloud.jenkins.server.JenkinsServerBuilder;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Computer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/7/2 9:55 上午
 * @Version 1.0
 */
public class JenkinsServerHandler {

    public static Map<String, Computer> getComputers(JenkinsDsInstanceConfig.Jenkins jenkins) throws URISyntaxException,IOException {
        JenkinsServer jenkinsServer = JenkinsServerBuilder.build(jenkins);
        return jenkinsServer.getComputers();
    }

}
