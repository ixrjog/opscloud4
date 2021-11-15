package com.baiyi.opscloud.datasource.jenkins.api.client;

import com.baiyi.opscloud.common.datasource.JenkinsConfig;
import com.baiyi.opscloud.core.model.Authentication;
import com.baiyi.opscloud.datasource.jenkins.api.http.HttpUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @Author baiyi
 * @Date 2021/3/30 2:08 下午
 * @Version 1.0
 */
@Component
public class JenkinsClient {

    /**
     * https://github.com/jenkinsci/blueocean-plugin/tree/master/blueocean-rest
     * @param jenkins
     * @param api
     * @return
     * @throws IOException
     */

    public static JsonNode get(JenkinsConfig.Jenkins jenkins, String api) throws IOException {
        Authentication authentication = Authentication.builder()
                .token(Joiner.on(" ").join("Basic", buildAuth(jenkins)))
                .build();
        String url = Joiner.on("/").join(jenkins.getUrl(), api);
        return HttpUtil.httpGetExecutor(url, Maps.newHashMap(), authentication);
    }

    public static String getString(JenkinsConfig.Jenkins jenkins, String api) throws IOException {
        Authentication authentication = Authentication.builder()
                .token(Joiner.on(" ").join("Basic", buildAuth(jenkins)))
                .build();
        String url = Joiner.on("/").join(jenkins.getUrl(), api);
        return HttpUtil.httpGetExecutorCommon(url, Maps.newHashMap(), authentication);
    }

    private static String buildAuth(JenkinsConfig.Jenkins jenkins) {
        return new String(Base64.getEncoder().encode(String.format("%s:%s", jenkins.getUsername(),
                jenkins.getToken()).getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }

}
