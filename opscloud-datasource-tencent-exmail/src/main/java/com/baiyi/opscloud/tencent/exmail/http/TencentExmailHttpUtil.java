package com.baiyi.opscloud.tencent.exmail.http;

import com.baiyi.opscloud.common.util.HttpUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.http.client.config.RequestConfig;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;


/**
 * Http工具类
 */
@Component
public class TencentExmailHttpUtil {

    private final static String ACCESS_TOKEN = "access_token";

    private interface HttpRequestConfig {
        Integer SOCKET_TIMEOUT = 10 * 1000;
        Integer CONNECT_TIMEOUT = 10 * 1000;
        Integer CONNECTION_REQUEST_TIMEOUT = 10 * 1000;
    }

    private RequestConfig getRequestConfig() {
        return RequestConfig.custom()
                .setSocketTimeout(HttpRequestConfig.SOCKET_TIMEOUT)
                .setConnectTimeout(HttpRequestConfig.CONNECT_TIMEOUT)
                .setConnectionRequestTimeout(HttpRequestConfig.CONNECTION_REQUEST_TIMEOUT)
                .build();
    }

    public JsonNode httpGetExecutor(String url) throws IOException {
        return HttpUtil.httpGetExecutor(url, getRequestConfig(), Collections.emptyMap());
    }

    public JsonNode httpPostExecutor(String url, Object param) throws IOException {
        return HttpUtil.httpPostExecutor(url, param, getRequestConfig(), Collections.emptyMap());
    }

    public JsonNode httpPutExecutor(String url, Object param) throws IOException {
        return HttpUtil.httpPutExecutor(url, param, getRequestConfig(), Collections.emptyMap());
    }

    public JsonNode httpDeleteExecutor(String url) throws IOException {
        return HttpUtil.httpDeleteExecutor(url, getRequestConfig(), Collections.emptyMap());
    }


    public Boolean checkResponse(JsonNode data) {
        return "0".equals(data.get("errcode").asText());
    }

//    public String getWebHook(String url, String token) {
//        return Joiner.on("").join(tencentExmailConfig.getApiUrl(), url, "?", ACCESS_TOKEN, "=", token);
//    }

}
