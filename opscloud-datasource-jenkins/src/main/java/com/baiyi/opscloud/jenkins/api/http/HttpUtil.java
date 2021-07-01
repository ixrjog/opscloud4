package com.baiyi.caesar.jenkins.api.http;

import com.alibaba.fastjson.JSON;
import com.baiyi.caesar.common.util.JSONMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/9/8 5:50 下午
 * @Version 1.0
 */
@Component
public class HttpUtil {

    private static final int SOCKET_TIMEOUT = 10 * 1000;
    private static final int CONNECT_TIMEOUT = 10 * 1000;
    private static final int CONNECTION_REQUEST_TIMEOUT = 10 * 1000;

    private static RequestConfig buildRequestConfig() {
        return RequestConfig.custom()
                .setSocketTimeout(SOCKET_TIMEOUT)
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                .build();
    }

    private static void settingRequest(HttpRequestBase httpRequestBase, Authentication authentication) {
        httpRequestBase.setConfig(buildRequestConfig());
        httpRequestBase.setHeader("Content-Type", "application/json;charset=utf-8");
        if (!authentication.getIsFree())
            httpRequestBase.setHeader(authentication.getHeader(), authentication.getToken());
    }

    private static JsonNode readTree(byte[] data) throws JsonProcessingException {
        JSONMapper mapper = new JSONMapper();
        return mapper.readTree(new String(data));
    }

    private static String buildGetParam(Map<String, String> param) {
        if (CollectionUtils.isEmpty(param))
            return "";
        List<String> s = Lists.newArrayListWithCapacity(param.size());
        param.forEach((k, v) -> s.add(Joiner.on("=").join(k, v)));
        return "?" + Joiner.on("&").join(s);
    }


    public static String httpGetExecutorCommon(String url, Map<String, String> param, Authentication authentication) throws IOException {
        HttpGet httpGet = new HttpGet(url + buildGetParam(param));
        settingRequest(httpGet, authentication);
        HttpClient httpClient = HttpClients.createDefault();
        HttpResponse response = httpClient.execute(httpGet, new HttpClientContext());
        HttpEntity entity = response.getEntity();
        byte[] data = EntityUtils.toByteArray(entity);
        return new String(data);
    }

    public static JsonNode httpGetExecutor(String url, Map<String, String> param, Authentication authentication) throws IOException {
        HttpGet httpGet = new HttpGet(url + buildGetParam(param));
        settingRequest(httpGet, authentication);
        HttpClient httpClient = HttpClients.createDefault();
        HttpResponse response = httpClient.execute(httpGet, new HttpClientContext());
        HttpEntity entity = response.getEntity();
        byte[] data = EntityUtils.toByteArray(entity);
        return readTree(data);
    }

    public static JsonNode httpPostExecutor(String url, Object param, Authentication authentication) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        settingRequest(httpPost, authentication);
        HttpClient httpClient = HttpClients.createDefault();
        httpPost.setEntity(
                new StringEntity(JSON.toJSONString(param), "utf-8"));
        HttpResponse response
                = httpClient.execute(httpPost, new HttpClientContext());
        HttpEntity entity = response.getEntity();
        byte[] data = EntityUtils.toByteArray(entity);
        return readTree(data);
    }

    public static JsonNode httpPutExecutor(String url, Object param, Authentication authentication) throws IOException {
        HttpPut httpPut = new HttpPut(url);
        settingRequest(httpPut, authentication);
        HttpClient httpClient = HttpClients.createDefault();
        httpPut.setEntity(
                new StringEntity(JSON.toJSONString(param), "utf-8"));
        HttpResponse response
                = httpClient.execute(httpPut, new HttpClientContext());
        HttpEntity entity = response.getEntity();
        byte[] data = EntityUtils.toByteArray(entity);
        return readTree(data);
    }

    public static JsonNode httpDeleteExecutor(String url, Authentication authentication) throws IOException {
        HttpDelete httpDelete = new HttpDelete(url);
        settingRequest(httpDelete, authentication);
        HttpClient httpClient = HttpClients.createDefault();
        HttpResponse response = httpClient.execute(httpDelete, new HttpClientContext());
        HttpEntity entity = response.getEntity();
        byte[] data = EntityUtils.toByteArray(entity);
        return readTree(data);
    }

}
