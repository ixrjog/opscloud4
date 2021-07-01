package com.baiyi.opscloud.jenkins.client;

import com.baiyi.caesar.domain.generator.caesar.CsJenkinsInstance;
import com.baiyi.caesar.jenkins.api.http.Authentication;
import com.baiyi.caesar.jenkins.api.http.HttpUtil;
import com.baiyi.caesar.service.jenkins.CsJenkinsInstanceService;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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


    @Resource
    private StringEncryptor stringEncryptor;

    public JsonNode get(String serverName, String api) throws IOException {
        CsJenkinsInstance instance = csJenkinsInstanceService.queryCsJenkinsInstanceByName(serverName);
        String auth = new String(Base64.getEncoder().encode(String.format("%s:%s", instance.getUsername(),
                stringEncryptor.decrypt(instance.getToken())).getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        Authentication authentication = Authentication.builder()
                .header("Authorization")
                .token("Basic " + auth)
                .build();
        String url = Joiner.on("/").join(instance.getUrl(), api);
        return HttpUtil.httpGetExecutor(url, Maps.newHashMap(), authentication);
    }

    public String getString(String serverName, String api) throws IOException {
        CsJenkinsInstance instance = csJenkinsInstanceService.queryCsJenkinsInstanceByName(serverName);
        String auth = new String(Base64.getEncoder().encode(String.format("%s:%s", instance.getUsername(),
                stringEncryptor.decrypt(instance.getToken())).getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        Authentication authentication = Authentication.builder()
                .header("Authorization")
                .token("Basic " + auth)
                .build();
        String url = Joiner.on("/").join(instance.getUrl(), api);
        return HttpUtil.httpGetExecutorCommon(url, Maps.newHashMap(), authentication);
    }

}
