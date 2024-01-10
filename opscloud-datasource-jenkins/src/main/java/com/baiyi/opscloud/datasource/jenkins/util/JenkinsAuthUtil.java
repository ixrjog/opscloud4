package com.baiyi.opscloud.datasource.jenkins.util;

import com.baiyi.opscloud.common.datasource.JenkinsConfig;
import com.baiyi.opscloud.common.util.StringFormatter;
import com.baiyi.opscloud.core.model.Authentication;
import com.google.common.base.Joiner;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @Author baiyi
 * @Date 2022/1/5 11:00 AM
 * @Version 1.0
 */
public class JenkinsAuthUtil {

    private JenkinsAuthUtil() {
    }

    public static Authentication buildAuthentication(JenkinsConfig.Jenkins jenkins) {
        return Authentication.builder()
                .token(Joiner.on(" ").join("Basic", toAuthBasic(jenkins)))
                .build();
    }

    public static String toAuthBasic(JenkinsConfig.Jenkins jenkins) {
        return new String(Base64.getEncoder().encode(
                StringFormatter.arrayFormat("{}:{}", jenkins.getUsername(), jenkins.getToken()
                ).getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }

}