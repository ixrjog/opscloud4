package com.sdg.cmdb.util;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zxxiao on 2016/11/4.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class HttpClientTest {

    private HttpClient httpClient;

    @Test
    public void testGet() throws IOException {
        HttpGet httpGet = new HttpGet("http://www.baidu.com");

        httpClient = HttpClientBuilder.create().build();
        HttpResponse response = httpClient.execute(httpGet);

        String responseBody = EntityUtils.toString(response.getEntity());

        System.err.println(responseBody);
    }

    @Test
    public void testPost() throws Exception {
        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (chain, authType) -> true).build();

        httpClient = HttpClientBuilder.create().setRedirectStrategy(new RedirectStrategy() {
            @Override
            public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
                return false;
            }

            @Override
            public HttpUriRequest getRedirect(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
                return null;
            }
        }).setSSLContext(sslContext).build();

        HttpPost httpPost = new HttpPost("https://zabbix.net/api_jsonrpc.php");

        Map<String, Object> map = new HashMap<>();
        map.put("jsonrpc", "2.0");
        map.put("method", "user.login");

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("user", "zabbix");
        paramMap.put("password", "18d38");
        map.put("params", paramMap);
        map.put("id", 0);
        System.err.println(JSON.toJSONString(map));

        StringEntity stringEntity = new StringEntity(JSON.toJSONString(map));
        stringEntity.setContentEncoding("UTF-8");
        stringEntity.setContentType(ContentType.APPLICATION_JSON.toString());
        httpPost.setEntity(stringEntity);

        HttpResponse response = httpClient.execute(httpPost);

        String responseBody = EntityUtils.toString(response.getEntity());

        Map<String, Object> result = JSON.parseObject(responseBody, Map.class);
        System.err.println(result.get("result"));
    }
}
