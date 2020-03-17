package com.baiyi.opscloud.zabbix.http;




import com.alibaba.fastjson.JSON;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by liangjian on 2016/12/19.
 */
public class ZabbixRequestBuilder {
    private static final AtomicInteger nextId = new AtomicInteger(1);

    private ZabbixRequestParamsMap request = new ZabbixRequestParamsMap();

    private ZabbixRequestBuilder() {}
    static public ZabbixRequestBuilder newBuilder() {
        return new ZabbixRequestBuilder();
    }

    public ZabbixRequestParamsMap build() {
        if (request.getId() == null) {
            request.setId(nextId.getAndIncrement());
        }
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

    /**
     * 跳过Null
     *
     * @param key
     * @param value
     * @return
     */
    public ZabbixRequestBuilder paramEntrySkipEmpty(String key, Object value) {
        if (!StringUtils.isEmpty(key) && !ObjectUtils.isEmpty(value)) {
            String str = JSON.toJSONString(value);
            if (str.equals("{}") || str.equals("[]") || str.equals("\"\""))
                return this;
            request.putParam(key, value);
        }
        return this;
    }

    /**
     * Do not necessary to call this method.If don not set id, ZabbixApi will auto set request auth..
     *
     * @param auth
     * @return
     */
    public ZabbixRequestBuilder auth(String auth) {
        request.setAuth(auth);
        return this;
    }

    public ZabbixRequestBuilder method(String method) {
        request.setMethod(method);
        return this;
    }

    /**
     * Do not necessary to call this method.If don not set id, RequestBuilder will auto generate.
     *
     * @param id
     * @return
     */
    public ZabbixRequestBuilder id(Integer id) {
        request.setId(id);
        return this;
    }

}
