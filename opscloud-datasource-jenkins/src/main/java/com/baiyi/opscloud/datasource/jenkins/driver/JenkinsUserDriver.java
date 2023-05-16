package com.baiyi.opscloud.datasource.jenkins.driver;

import com.baiyi.opscloud.common.datasource.JenkinsConfig;
import com.baiyi.opscloud.datasource.jenkins.entity.JenkinsUser;
import com.baiyi.opscloud.datasource.jenkins.feign.JenkinsUserFeign;
import com.baiyi.opscloud.datasource.jenkins.util.JenkinsAuthUtil;
import feign.Feign;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/1/5 10:53 AM
 * @Version 1.0
 */
@SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
@Component
public class JenkinsUserDriver {

    public JenkinsUser.User getUser(JenkinsConfig.Jenkins jenkins, String username) {
        JenkinsUserFeign jenkinsAPI = Feign.builder()
                .retryer(new Retryer.Default(3000, 3000, 3))
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(JenkinsUserFeign.class, jenkins.getUrl());
        return jenkinsAPI.getUser(JenkinsAuthUtil.toAuthBasic(jenkins), username);
    }

    public List<JenkinsUser.User> listUsers(JenkinsConfig.Jenkins jenkins) {
        JenkinsUserFeign jenkinsAPI = Feign.builder()
                .retryer(new Retryer.Default(3000, 3000, 3))
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(JenkinsUserFeign.class, jenkins.getUrl());
        return jenkinsAPI.listUsers(JenkinsAuthUtil.toAuthBasic(jenkins));
    }

}