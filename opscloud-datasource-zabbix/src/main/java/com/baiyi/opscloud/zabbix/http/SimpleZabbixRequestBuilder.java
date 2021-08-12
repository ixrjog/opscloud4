package com.baiyi.opscloud.zabbix.http;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/25 11:13 上午
 * @Since 1.0
 */
public class SimpleZabbixRequestBuilder {

    private static final AtomicInteger nextId = new AtomicInteger(1);

    private SimpleZabbixRequest request = new SimpleZabbixRequest();

    private SimpleZabbixRequestBuilder() {
    }

    public static SimpleZabbixRequestBuilder builder() {
        return new SimpleZabbixRequestBuilder();
    }

    public SimpleZabbixRequest build() {
        if (request.getId() == null) {
            request.setId(nextId.getAndIncrement());
        }
        return request;
    }

    public SimpleZabbixRequestBuilder version(String version) {
        request.setJsonrpc(version);
        return this;
    }

    public SimpleZabbixRequestBuilder paramEntry(String key, Object value) {
        request.putParam(key, value);
        return this;
    }

    /**
     * 跳过Null
     *
     * @param key
     * @param value
     * @return
     */
    public SimpleZabbixRequestBuilder paramEntrySkipEmpty(String key, Object value) {
        if (!StringUtils.isEmpty(key) && !org.springframework.util.ObjectUtils.isEmpty(value)) {
            String str = JSON.toJSONString(value);
            if (str.equals("{}") || str.equals("[]") || str.equals("\"\""))
                return this;
            request.putParam(key, value);
        }
        return this;
    }

    public SimpleZabbixRequestBuilder paramEntry(Map<String, Object> map) {
        map.forEach((k, v) -> {
            if (!ObjectUtils.isNotEmpty(v)) {
                return;
            }
            request.putParam(k, v);
        });
        return this;
    }

    public SimpleZabbixRequestBuilder filter(ZabbixFilter zabbixFilter) {
        request.putParam("filter", zabbixFilter.getFilter());
        return this;
    }

    public SimpleZabbixRequestBuilder auth(String auth) {
        request.setAuth(auth);
        return this;
    }

    public SimpleZabbixRequestBuilder method(String method) {
        request.setMethod(method);
        return this;
    }

    public SimpleZabbixRequestBuilder id(Integer id) {
        request.setId(id);
        return this;
    }
}
