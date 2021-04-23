package com.baiyi.opscloud.zabbix.handler;

import com.baiyi.opscloud.common.util.JSONUtils;
import com.baiyi.opscloud.zabbix.config.ZabbixConfig;
import com.baiyi.opscloud.zabbix.entry.ZabbixUserLogin;
import com.baiyi.opscloud.zabbix.http.ZabbixBaseRequest;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @Author baiyi
 * @Date 2019/12/31 10:13 上午
 * @Version 1.0
 */
@Slf4j
@Component("ZabbixHandler")
public class ZabbixHandler implements InitializingBean {

    @Resource
    private ZabbixConfig zabbixConfig;

    private volatile String auth; // token

    public static final String ZABBIX_SERVER_DEFAULT_NAME = "Zabbix server";

    public static final String ZABBIX_KEY_APIINFO = "apiinfo.version";

    private CloseableHttpClient zabbixClient;

    private URI uri;

    public JsonNode api(ZabbixBaseRequest request, String nodeName) throws Exception {
        JsonNode node = call(request);
        return node.findValue(nodeName);
    }

    public JsonNode api(ZabbixBaseRequest request) throws Exception {
        return call(request);
    }

    private void assembleRequestAuth(ZabbixBaseRequest request) throws Exception {
        if (StringUtils.isEmpty(request.getAuth()) && !request.getMethod().equalsIgnoreCase(ZABBIX_KEY_APIINFO) && !request.getMethod().equalsIgnoreCase("user.login")) {
            if (StringUtils.isEmpty(auth)) throw new Exception();
            request.setAuth(auth);
        }
    }

    private JsonNode call(ZabbixBaseRequest request) throws Exception {
        assembleRequestAuth(request);
        byte[] data = httpCall(request);
        JsonNode rootNode = JSONUtils.readTree(data);
        return rootNode;
    }

    private byte[] httpCall(ZabbixBaseRequest request) throws Exception {
        HttpUriRequest httpRequest = org.apache.http.client.methods.RequestBuilder.post()
                .setUri(uri)
                .addHeader("Content-Type", "application/json")
                .setEntity(new StringEntity(request.toString(), ContentType.APPLICATION_JSON)).build();
        CloseableHttpResponse response = zabbixClient.execute(httpRequest);
        HttpEntity entity = response.getEntity();
        byte[] data = EntityUtils.toByteArray(entity);
        return data;
    }

    public void afterPropertiesSet() throws Exception {
        log.info("ZabbixClient初始化 : {}");
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5 * 1000)
                .setConnectionRequestTimeout(5 * 1000)
                .setSocketTimeout(5 * 1000)
                .build();
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        CloseableHttpClient httpclient = HttpClients.custom()
                .setConnectionManager(connManager)
                .setDefaultRequestConfig(requestConfig)
                .build();
        try {
            uri = zabbixConfig.buildURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException("zabbix url invalid", e);
        }
        this.zabbixClient = httpclient;
        login();
    }

    public boolean login() {
        try {
            ZabbixBaseRequest request = zabbixConfig.buildLoginRequest();

            byte[] data = httpCall(request);
            ZabbixUserLogin userLogin = JSONUtils.readValue(new String(data), ZabbixUserLogin.class);
            if (!StringUtils.isEmpty(userLogin.getAuth())) {
                log.info("zabbix succeeded in getting auth");
                this.auth = userLogin.getAuth();
                //cacheKeyService.set(this.getClass(), ZABBIX_AUTH, auth, 60 * 24 * 365);
                return true;
            }
        } catch (Exception e) {
            log.error("zabbix failed to get auth");
        }
        return false;
    }


    /**
     * 关闭连接
     */
    private void destroy() {
        if (zabbixClient != null) {
            try {
                zabbixClient.close();
            } catch (Exception e) {
                log.error("zabbix close httpclient error!", e);
            }
        }
    }

}
