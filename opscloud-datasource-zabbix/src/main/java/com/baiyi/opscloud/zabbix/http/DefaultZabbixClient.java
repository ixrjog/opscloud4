package com.baiyi.opscloud.zabbix.http;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.common.datasource.config.DsZabbixConfig;
import com.baiyi.opscloud.common.exception.common.CommonRuntimeException;
import com.baiyi.opscloud.common.util.JSONMapper;
import com.baiyi.opscloud.zabbix.entry.ZabbixUser;
import com.baiyi.opscloud.zabbix.mapper.ZabbixMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static com.baiyi.opscloud.zabbix.handler.base.ZabbixServer.ApiConstant.RESULT;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/25 11:26 上午
 * @Since 1.0
 */
public class DefaultZabbixClient implements IZabbixClient {

    private CloseableHttpClient httpClient;
    private URI uri;
    private DsZabbixConfig.Zabbix zabbix;
    private volatile String auth;

    public DefaultZabbixClient(DsZabbixConfig.Zabbix zabbix) {
        this.zabbix = zabbix;
        try {
            this.uri = new URI(zabbix.getUrl().trim());
            init();
        } catch (URISyntaxException e) {
            throw new CommonRuntimeException("zabbix url exception", e);
        }
    }

    public DefaultZabbixClient(DsZabbixConfig.Zabbix zabbix, String auth) {
        this.zabbix = zabbix;
        try {
            this.uri = new URI(zabbix.getUrl().trim());
            this.auth = auth;
            init();
        } catch (URISyntaxException e) {
            throw new CommonRuntimeException("zabbix url exception", e);
        }
    }

    private void init() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5 * 1000)
                .setConnectionRequestTimeout(5 * 1000)
                .setSocketTimeout(5 * 1000)
                .build();
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        this.httpClient = HttpClients.custom()
                .setConnectionManager(connManager)
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    @Override
    public void destroy() {
        if (httpClient != null) {
            try {
                httpClient.close();
            } catch (Exception e) {
                throw new CommonRuntimeException("DefaultZabbixClient destroy exception", e);
            }
        }
    }

    @Override
    public JsonNode call(ZabbixRequest request) {
        if (Strings.isBlank(request.getAuth())) {
            request.setAuth(this.auth);
        }
        try {
            HttpUriRequest httpRequest = RequestBuilder.post().setUri(uri)
                    .addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                    .setEntity(new StringEntity(JSON.toJSONString(request), ContentType.APPLICATION_JSON))
                    .build();
            CloseableHttpResponse response = httpClient.execute(httpRequest);
            HttpEntity entity = response.getEntity();
            byte[] data = EntityUtils.toByteArray(entity);
            return readTree(data);
        } catch (IOException e) {
            throw new CommonRuntimeException("DefaultZabbixApi call exception", e);
        }
    }

    private static JsonNode readTree(byte[] data) throws JsonProcessingException {
        JSONMapper mapper = new JSONMapper();
        return mapper.readTree(new String(data));
    }

    @Override
    public ZabbixUser login() {
        this.auth = null;
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .paramEntry("user", zabbix.getUser())
                .paramEntry("password", zabbix.getPassword())
                .paramEntry("userData", true)
                .method("user.login")
                .build();
        JsonNode response = call(request);
        ZabbixUser user = ZabbixMapper.mapper(response.get(RESULT), ZabbixUser.class);
        this.auth = user.getSessionId();
        return user;
    }

}
