package com.baiyi.opscloud.zabbix.v5.request;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/23 2:01 下午
 * @Since 1.0
 */
public class ZabbixFilterBuilder {

    private final ZabbixFilter filter = new ZabbixFilter();

    private ZabbixFilterBuilder() {
    }

    public static ZabbixFilterBuilder builder() {
        return new ZabbixFilterBuilder();
    }

    public ZabbixFilterBuilder putEntry(String key, Object value) {
        filter.put(key, value);
        return this;
    }

    public ZabbixFilter build() {
        return filter;
    }
}
