package com.baiyi.opscloud.common.util;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Map;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/29 11:04 上午
 * @Since 1.0
 */
public class HttpUtils {

    private static final String UTF_8 = "utf-8";

    private static JsonNode readTree(byte[] data) throws JsonProcessingException {
        JSONMapper mapper = new JSONMapper();
        return mapper.readTree(new String(data));
    }

    private static HttpRequestBase setHttpHeader(HttpRequestBase httpRequest, RequestConfig requestConfig, Map<String, String> headers) {
        httpRequest.setConfig(requestConfig);
        httpRequest.setHeader("Content-Type", "application/json;charset=utf-8");
        headers.forEach(httpRequest::setHeader);
        return httpRequest;
    }

    public static JsonNode httpGetExecutor(String url, RequestConfig requestConfig, Map<String, String> headers) throws IOException {
        HttpGet httpGet = (HttpGet) setHttpHeader(new HttpGet(url), requestConfig, headers);
        HttpClient httpClient = HttpClients.createDefault();
        HttpResponse response = httpClient.execute(httpGet, new HttpClientContext());
        HttpEntity entity = response.getEntity();
        byte[] data = EntityUtils.toByteArray(entity);
        return readTree(data);
    }

    public static JsonNode httpPostExecutor(String url, Object param, RequestConfig requestConfig, Map<String, String> headers) throws IOException {
        HttpPost httpPost = (HttpPost) setHttpHeader(new HttpPost(url), requestConfig, headers);
        httpPost.setEntity(new StringEntity(JSON.toJSONString(param), UTF_8));
        HttpClient httpClient = HttpClients.createDefault();
        HttpResponse response = httpClient.execute(httpPost, new HttpClientContext());
        HttpEntity entity = response.getEntity();
        byte[] data = EntityUtils.toByteArray(entity);
        return readTree(data);
    }

    public static JsonNode httpPutExecutor(String url, Object param, RequestConfig requestConfig, Map<String, String> headers) throws IOException {
        HttpPut httpPut = (HttpPut) setHttpHeader(new HttpPut(url), requestConfig, headers);
        httpPut.setEntity(new StringEntity(JSON.toJSONString(param), UTF_8));
        HttpClient httpClient = HttpClients.createDefault();
        HttpResponse response = httpClient.execute(httpPut, new HttpClientContext());
        HttpEntity entity = response.getEntity();
        byte[] data = EntityUtils.toByteArray(entity);
        return readTree(data);
    }

    public static JsonNode httpDeleteExecutor(String url, RequestConfig requestConfig, Map<String, String> headers) throws IOException {
        HttpDelete httpDelete = (HttpDelete) setHttpHeader(new HttpDelete(url), requestConfig, headers);
        HttpClient httpClient = HttpClients.createDefault();
        HttpResponse response = httpClient.execute(httpDelete, new HttpClientContext());
        HttpEntity entity = response.getEntity();
        byte[] data = EntityUtils.toByteArray(entity);
        return readTree(data);
    }


}
