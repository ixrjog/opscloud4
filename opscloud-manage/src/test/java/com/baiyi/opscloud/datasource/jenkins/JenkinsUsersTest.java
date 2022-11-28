package com.baiyi.opscloud.datasource.jenkins;

import com.baiyi.opscloud.common.datasource.JenkinsConfig;
import com.baiyi.opscloud.datasource.jenkins.base.BaseJenkinsTest;
import com.baiyi.opscloud.datasource.jenkins.driver.JenkinsUsersDriver;
import com.baiyi.opscloud.datasource.jenkins.entity.JenkinsUser;
import com.baiyi.opscloud.leo.domain.model.JenkinsPipeline;
import com.baiyi.opscloud.leo.driver.BlueRestDriver;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/1/5 11:02 AM
 * @Version 1.0
 */
public class JenkinsUsersTest extends BaseJenkinsTest {

    @Resource
    private JenkinsUsersDriver jenkinsUsersDrive;

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
    void getPipelineRunNodesTest(){
        //     public List<JenkinsPipeline.Node> getPipelineRunNodes(JenkinsConfig.Jenkins config, String jobName, String buildNumber) {
        // config_leo-jenkins-1.chuanyinet.com
        JenkinsConfig jenkinsConfig = getConfigById(60);
        List<JenkinsPipeline.Node> nodes = blueRestDriver.getPipelineRunNodes(jenkinsConfig.getJenkins(),"MERCHANT-RSS_MERCHANT-RSS-DEV_20","1");
        print(nodes);
    }


}
