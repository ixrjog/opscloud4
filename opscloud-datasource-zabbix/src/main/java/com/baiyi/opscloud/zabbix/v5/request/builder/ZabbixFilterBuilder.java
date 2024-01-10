package com.baiyi.opscloud.zabbix.v5.request.builder;

import com.baiyi.opscloud.zabbix.v5.request.ZabbixRequest;

/**
 * @Author 修远
 * @Date 2021/7/23 2:01 下午
 * @Since 1.0
 */
public class ZabbixFilterBuilder {

    private final ZabbixRequest.Filter filter = ZabbixRequest.Filter.builder().build();

    private ZabbixFilterBuilder() {
    }

    public static ZabbixFilterBuilder builder() {
        return new ZabbixFilterBuilder();
    }

    public ZabbixFilterBuilder putEntry(String key, Object value) {
        filter.put(key, value);
        return this;
    }

    public ZabbixRequest.Filter build() {
        return filter;
    }

}