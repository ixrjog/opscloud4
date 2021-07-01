package com.baiyi.opscloud.zabbix.http;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/25 11:13 上午
 * @Since 1.0
 */
public class ZabbixRequestBuilder {

    private static final AtomicInteger nextId = new AtomicInteger(1);

    private ZabbixRequest request = new ZabbixRequest();

    private ZabbixRequestBuilder() {
    }

    public static ZabbixRequestBuilder builder() {
        return new ZabbixRequestBuilder();
    }

    public ZabbixRequest build() {
        if (request.getId() == null)
            request.setId(nextId.getAndIncrement());
        return request;
    }

    public ZabbixRequestBuilder version(String version) {
        request.setJsonrpc(version);
        return this;
    }

    public ZabbixRequestBuilder paramEntry(String key, Object value) {
        request.putParam(key, value);
        return this;
    }

    public ZabbixRequestBuilder auth(String auth) {
        request.setAuth(auth);
        return this;
    }

    public ZabbixRequestBuilder method(String method) {
        request.setMethod(method);
        return this;
    }

    public ZabbixRequestBuilder id(Integer id) {
        request.setId(id);
        return this;
    }
}
