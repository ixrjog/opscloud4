package com.baiyi.opscloud.leo.driver.util;

import com.baiyi.opscloud.domain.model.Authorization;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
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

    private static void setRequest(HttpRequestBase httpRequestBase, Authorization.Credential auth) {
        RequestConfig rc = RequestConfig.custom()
                .setSocketTimeout(SOCKET_TIMEOUT)
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                .build();
        httpRequestBase.setConfig(rc);
        httpRequestBase.setHeader("Content-Type", "application/json;charset=utf-8");
        httpRequestBase.setHeader("Authentication", auth.toBasic());
    }

    private static String buildGetParam(Map<String, String> param) {
        if (CollectionUtils.isEmpty(param))
            return "";
        List<String> s = Lists.newArrayListWithCapacity(param.size());
        param.forEach((k, v) -> s.add(Joiner.on("=").join(k, v)));
        return "?" + Joiner.on("&").join(s);
    }

    public static String httpGetExecutorCommon(String url, Map<String, String> param, Authorization.Credential auth) throws IOException {
        HttpGet httpGet = new HttpGet(url + buildGetParam(param));
        setRequest(httpGet, auth);
        HttpClient httpClient = HttpClients.createDefault();
        HttpResponse response = httpClient.execute(httpGet, new HttpClientContext());
        HttpEntity entity = response.getEntity();
        byte[] data = EntityUtils.toByteArray(entity);
        return new String(data);
    }

//    public static JsonNode httpGetExecutor(String url, Map<String, String> param, Authentication authentication) throws IOException {
//        HttpGet httpGet = new HttpGet(url + buildGetParam(param));
//        settingRequest(httpGet, authentication);
//        HttpClient httpClient = HttpClients.createDefault();
//        HttpResponse response = httpClient.execute(httpGet, new HttpClientContext());
//        HttpEntity entity = response.getEntity();
//        byte[] data = EntityUtils.toByteArray(entity);
//        return readTree(data);
//    }


}
