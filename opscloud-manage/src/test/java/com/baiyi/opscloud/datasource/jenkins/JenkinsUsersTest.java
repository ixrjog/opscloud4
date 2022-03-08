package com.baiyi.opscloud.datasource.jenkins;

import com.baiyi.opscloud.datasource.jenkins.base.BaseJenkinsTest;
import com.baiyi.opscloud.datasource.jenkins.driver.JenkinsUsersDriver;
import com.baiyi.opscloud.datasource.jenkins.entity.JenkinsUser;
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
}
