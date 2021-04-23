package com.baiyi.opscloud.caesar.util;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.common.config.CaesarConfig;
import com.baiyi.opscloud.common.util.JSONMapper;
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
import org.springframework.beans.factory.annotation.Autowired;
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
public class CaesarHttpUtils {

    private static final int SOCKET_TIMEOUT = 10 * 1000;
    private static final int CONNECT_TIMEOUT = 10 * 1000;
    private static final int CONNECTION_REQUEST_TIMEOUT = 10 * 1000;

    private static CaesarConfig caesarConfig;

    private static final String X_TOKEN = "X-Token";

    private static final String API_PATH = "/cs";

    @Autowired
    private void setCaesarConfig(CaesarConfig caesarConfig) {
        CaesarHttpUtils.caesarConfig = caesarConfig;
    }

    private static String buildUrl(String url) {
        return Joiner.on("").join(caesarConfig.getUrl(), API_PATH, url);
    }

    private static RequestConfig buildRequestConfig() {
        return RequestConfig.custom()
                .setSocketTimeout(SOCKET_TIMEOUT)
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                .build();
    }

    private static void invokeRequest(HttpRequestBase httpRequestBase) {
        httpRequestBase.setConfig(buildRequestConfig());
        httpRequestBase.setHeader("Content-Type", "application/json;charset=utf-8");
        httpRequestBase.setHeader(X_TOKEN, caesarConfig.getApiToken());
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

    public static JsonNode httpGetExecutor(String url, Map<String, String> param) throws IOException {
        HttpGet httpGet = new HttpGet(buildUrl(url) + buildGetParam(param));
        invokeRequest(httpGet);
        HttpClient httpClient = HttpClients.createDefault();
        HttpResponse response = httpClient.execute(httpGet, new HttpClientContext());
        HttpEntity entity = response.getEntity();
        byte[] data = EntityUtils.toByteArray(entity);
        return readTree(data);
    }

    public static JsonNode httpPostExecutor(String url, Object param) throws IOException {
        HttpPost httpPost = new HttpPost(buildUrl(url));
        invokeRequest(httpPost);
        HttpClient httpClient = HttpClients.createDefault();
        httpPost.setEntity(
                new StringEntity(JSON.toJSONString(param), "utf-8"));
        HttpResponse response
                = httpClient.execute(httpPost, new HttpClientContext());
        HttpEntity entity = response.getEntity();
        byte[] data = EntityUtils.toByteArray(entity);
        return readTree(data);
    }

    public static JsonNode httpPutExecutor(String url, Object param) throws IOException {
        HttpPut httpPut = new HttpPut(buildUrl(url));
        invokeRequest(httpPut);
        HttpClient httpClient = HttpClients.createDefault();
        httpPut.setEntity(
                new StringEntity(JSON.toJSONString(param), "utf-8"));
        HttpResponse response
                = httpClient.execute(httpPut, new HttpClientContext());
        HttpEntity entity = response.getEntity();
        byte[] data = EntityUtils.toByteArray(entity);
        return readTree(data);
    }

    public static JsonNode httpDeleteExecutor(String url) throws IOException {
        HttpDelete httpDelete = new HttpDelete(buildUrl(url));
        invokeRequest(httpDelete);
        HttpClient httpClient = HttpClients.createDefault();
        HttpResponse response = httpClient.execute(httpDelete, new HttpClientContext());
        HttpEntity entity = response.getEntity();
        byte[] data = EntityUtils.toByteArray(entity);
        return readTree(data);
    }

}
