package com.baiyi.opscloud.zabbix.v5.request.builder;

import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.zabbix.v5.request.ZabbixRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author baiyi
 * @Date 2021/11/17 11:23 上午
 * @Version 1.0
 */
public class ZabbixRequestBuilder {

    private static final AtomicInteger NEXT_ID = new AtomicInteger(1);

    private final ZabbixRequest.DefaultRequest request = ZabbixRequest.DefaultRequest.builder().build();

    //private final ZabbixRequest.Filter filter = ZabbixRequest.Filter.builder().build();

    private ZabbixRequestBuilder() {
    }

    public static ZabbixRequestBuilder builder() {
        return new ZabbixRequestBuilder();
    }

    public ZabbixRequest.DefaultRequest build() {
        if (request.getId() == null) {
            request.setId(NEXT_ID.getAndIncrement());
        }
        return request;
    }

    public ZabbixRequestBuilder putParam(String key, Object value) {
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
    public ZabbixRequestBuilder putParamSkipEmpty(String key, Object value) {
        if (value != null && !org.springframework.util.ObjectUtils.isEmpty(value) && !StringUtils.isEmpty(key)) {
            String str = JSONUtil.writeValueAsString(value);
            if (str.equals("{}") || str.equals("[]") || str.equals("\"\"")) {
                return this;
            }
            request.putParam(key, value);
        }
        return this;
    }

    public ZabbixRequestBuilder putParam(Map<String, Object> map) {
        map.forEach((k, v) -> {
            if (!ObjectUtils.isNotEmpty(v)) {
                return;
            }
            request.putParam(k, v);
        });
        return this;
    }

    public ZabbixRequestBuilder filter(ZabbixRequest.Filter zabbixFilter) {
        request.putParam("filter", zabbixFilter.getFilter());
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

    public ZabbixRequestBuilder setId(Integer id) {
        request.setId(id);
        return this;
    }

}