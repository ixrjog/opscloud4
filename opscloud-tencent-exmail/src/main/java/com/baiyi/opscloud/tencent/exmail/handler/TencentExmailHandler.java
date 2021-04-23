package com.baiyi.opscloud.tencent.exmail.handler;

import com.baiyi.opscloud.common.util.HttpUtils;
import com.baiyi.opscloud.tencent.exmail.config.TencentExmailConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Joiner;
import org.apache.http.client.config.RequestConfig;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Collections;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/29 1:43 下午
 * @Since 1.0
 */

@Component
public class TencentExmailHandler {

    @Resource
    private TencentExmailConfig tencentExmailConfig;

    private final static String ACCESS_TOKEN = "access_token";

    private interface HttpRequestConfig {
        Integer socketTimeout = 10 * 1000;
        Integer connectTimeout = 10 * 1000;
        Integer connectionRequestTimeout = 10 * 1000;
    }

    private RequestConfig getRequestConfig() {
        return RequestConfig.custom()
                .setSocketTimeout(HttpRequestConfig.socketTimeout)
                .setConnectTimeout(HttpRequestConfig.connectTimeout)
                .setConnectionRequestTimeout(HttpRequestConfig.connectionRequestTimeout)
                .build();
    }

    public JsonNode httpGetExecutor(String url) throws IOException {
        return HttpUtils.httpGetExecutor(url, getRequestConfig(), Collections.emptyMap());
    }

    public JsonNode httpPostExecutor(String url, Object param) throws IOException {
        return HttpUtils.httpPostExecutor(url, param, getRequestConfig(), Collections.emptyMap());
    }

    public JsonNode httpPutExecutor(String url, Object param) throws IOException {
        return HttpUtils.httpPutExecutor(url, param, getRequestConfig(), Collections.emptyMap());
    }

    public JsonNode httpDeleteExecutor(String url) throws IOException {
        return HttpUtils.httpDeleteExecutor(url, getRequestConfig(), Collections.emptyMap());
    }

    public TencentExmailConfig.Account getMasterAccount() {
        for (TencentExmailConfig.Account account : tencentExmailConfig.getAccounts())
            if (account.getMaster())
                return account;
        return null;
    }

    public Boolean checkResponse(JsonNode data) {
        return "0".equals(data.get("errcode").asText());
    }

    public String getWebHook(String url, String token) {
        return Joiner.on("").join(tencentExmailConfig.getApiUrl(), url, "?", ACCESS_TOKEN, "=", token);
    }

}
