package com.baiyi.opscloud.zabbix.http;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/25 11:13 上午
 * @Since 1.0
 */
public class ZabbixCommonRequestBuilder {

    private static final AtomicInteger nextId = new AtomicInteger(1);

    private ZabbixCommonRequest request = new ZabbixCommonRequest();

    private ZabbixCommonRequestBuilder() {
    }

    public static ZabbixCommonRequestBuilder builder() {
        return new ZabbixCommonRequestBuilder();
    }

    public ZabbixCommonRequest build() {
        if (request.getId() == null) {
            request.setId(nextId.getAndIncrement());
        }
        return request;
    }

    public ZabbixCommonRequestBuilder version(String version) {
        request.setJsonrpc(version);
        return this;
    }

    public ZabbixCommonRequestBuilder paramEntry(String key, Object value) {
        request.putParam(key, value);
        return this;
    }

    public ZabbixCommonRequestBuilder paramEntry(Map<String, Object> map) {
        map.forEach((k, v) -> {
            if (!ObjectUtils.isNotEmpty(v)) {
                return;
            }
            request.putParam(k, v);
        });
        return this;
    }

    public ZabbixCommonRequestBuilder filter(ZabbixFilter zabbixFilter) {
        request.putParam("filter", zabbixFilter.getFilter());
        return this;
    }

    public ZabbixCommonRequestBuilder auth(String auth) {
        request.setAuth(auth);
        return this;
    }

    public ZabbixCommonRequestBuilder method(String method) {
        request.setMethod(method);
        return this;
    }

    public ZabbixCommonRequestBuilder id(Integer id) {
        request.setId(id);
        return this;
    }
}
