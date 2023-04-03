package com.baiyi.opscloud.datasource.jenkins;

import com.baiyi.opscloud.common.datasource.JenkinsConfig;
import com.baiyi.opscloud.datasource.jenkins.base.BaseJenkinsTest;
import com.baiyi.opscloud.datasource.jenkins.driver.JenkinsUserDriver;
import com.baiyi.opscloud.datasource.jenkins.entity.JenkinsUser;
import com.baiyi.opscloud.leo.domain.model.JenkinsPipeline;
import com.baiyi.opscloud.leo.driver.BlueRestDriver;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/1/5 11:02 AM
 * @Version 1.0
 */
public class JenkinsUsersTest extends BaseJenkinsTest {

    @Resource
    private JenkinsUserDriver jenkinsUsersDrive;

    @Resource
    private BlueRestDriver blueRestDriver;

    @Test
    void getUserTest() {
        JenkinsUser.User user = jenkinsUsersDrive.getUser(getConfig().getJenkins(), "baiyi");
        print(user);
    }

    @Test
    void listUsersTest() {
        List<JenkinsUser.User> users = jenkinsUsersDrive.listUsers(getConfig().getJenkins());
        print(users);
    }

    @Test
    void getPipelineRunNodesTest() {
        // config_leo-jenkins-1.chuanyinet.com
        JenkinsConfig jenkinsConfig = getConfigById(60);
        List<JenkinsPipeline.Node> nodes = blueRestDriver.getPipelineNodes(jenkinsConfig.getJenkins(), "MERCHANT-RSS_MERCHANT-RSS-DEV_20", "1");
        print(nodes);
    }

    @Test
    void getPipelineRunNodeStepsTest() {
        // config_leo-jenkins-1.chuanyinet.com
        JenkinsConfig jenkinsConfig = getConfigById(60);
        //                                                getPipelineRunNodeSteps
        List<JenkinsPipeline.Step> steps = blueRestDriver.getPipelineNodeSteps(jenkinsConfig.getJenkins(), "MERCHANT-RSS_MERCHANT-RSS-DEV_20", "1", "13");
        print(steps);
    }

    @Test
    void getPipelineRunNodeStepLogTest() {
        // config_leo-jenkins-1.chuanyinet.com
        JenkinsConfig jenkinsConfig = getConfigById(60);
        // https://leo-jenkins-1.chuanyinet.com/blue/rest/organizations/jenkins/pipelines/MERCHANT-RSS_MERCHANT-RSS-DEV_20/runs/1/nodes/8/steps/9/log/
        String log = blueRestDriver.getPipelineNodeStepLog(jenkinsConfig.getJenkins(), "MERCHANT-RSS_MERCHANT-RSS-DEV_20", "1", "8", "9");
        print(log);
    }

}
