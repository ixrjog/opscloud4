package com.baiyi.opscloud.oc3test;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.util.IOUtils;
import com.baiyi.opscloud.common.util.JSONMapper;
import com.baiyi.opscloud.service.helpdesk.OcHelpdeskReportService;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Joiner;
import lombok.Data;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @Author baiyi
 * @Date 2020/5/19 3:30 下午
 * @Version 1.0
 */
public class Oc3Test extends BaseUnit {

    private static final String TOKEN_KEY = "x-token";

    // API-TOKEN 在个人详情中申请
    private static final String X_TOKEN = "AAAAAAAAAAA";

    private static final String OC3_URL = "https://oc3.ops.yangege.cn";

    private static final String queryServer = "/oc3/server/page/fuzzy/query";


    @Resource
    private OcHelpdeskReportService ocHelpdeskReportService;

    private HttpPost getHttpPostClient(String url) {
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(20000)
                .setConnectTimeout(20000)
                .setConnectionRequestTimeout(20000)
                .build();
        HttpPost httpPost = new HttpPost(Joiner.on("").join(OC3_URL, url));
        httpPost.setConfig(requestConfig);
        httpPost.setHeader(TOKEN_KEY, X_TOKEN);
        httpPost.setHeader("Content-Type", "application/json;charset=utf-8");
        return httpPost;
    }

    private HttpPut getHttpPutClient(String url) {
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(20000)
                .setConnectTimeout(20000)
                .setConnectionRequestTimeout(20000)
                .build();
        HttpPut httpPut = new HttpPut(Joiner.on("").join(OC3_URL, url));
        httpPut.setConfig(requestConfig);
        httpPut.setHeader(TOKEN_KEY, X_TOKEN);
        httpPut.setHeader("Content-Type", "application/json;charset=utf-8");
        return httpPut;
    }

    private HttpGet getHttpGetClient(String url) {
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(2000)
                .setConnectTimeout(2000)
                .setConnectionRequestTimeout(2000)
                .build();
        HttpGet httpGet = new HttpGet(Joiner.on("").join(OC3_URL, url));
        httpGet.setConfig(requestConfig);
        httpGet.setHeader(TOKEN_KEY, X_TOKEN);
        httpGet.setHeader("Content-Type", "application/json;charset=utf-8");
        return httpGet;
    }

    @Data
    public class PageQueryServerParam {
        private Integer serverGroupId; // 服务器组id
        private Integer envType; // 环境
        private Integer tagId;
        private Integer extend = 0; // 不显示详情
        private Integer page = 1;
        private Integer length = 100;
    }

    @Test
    void testQueryServer() {
        queryServer(null, null, null);
    }

    private void queryServer(Integer serverGroupId, Integer envType, Integer tagId) {
        HttpPost httpPost = getHttpPostClient(queryServer);
        PageQueryServerParam pageQueryServerParam = new PageQueryServerParam();
        pageQueryServerParam.setServerGroupId(serverGroupId);
        pageQueryServerParam.setEnvType(envType);
        pageQueryServerParam.setTagId(tagId);

        HttpClient httpClient = HttpClients.createDefault();
        try {
            httpPost.setEntity(new StringEntity(JSON.toJSONString(pageQueryServerParam), "utf-8"));
            HttpResponse response = httpClient.execute(httpPost, new HttpClientContext());

            HttpEntity entity = response.getEntity();
            byte[] data = EntityUtils.toByteArray(entity);
            JsonNode jsonNode = readTree(data);
            // server list
            System.err.println(jsonNode.get("body").get("data"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static JsonNode readTree(byte[] data) {
        try {
            JSONMapper mapper = new JSONMapper();
            return mapper.readTree(new String(data));
        } catch (Exception e) {
            return null;
        }
    }

    @Test
    void ioTest(){
        IOUtils.appendFile("22222","/Users/liangjian/Documents/workspace/opscloud-data/xterm/audit/7179ef7d-20a2-451a-91fb-bb4c62c08cc2/test.log");
    }

}
