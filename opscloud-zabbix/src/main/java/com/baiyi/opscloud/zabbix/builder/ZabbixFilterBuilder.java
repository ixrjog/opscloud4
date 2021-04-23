package com.baiyi.opscloud.zabbix.builder;

import com.baiyi.opscloud.zabbix.param.ZabbixFilter;

/**
 * @Author baiyi
 * @Date 2021/2/1 5:40 下午
 * @Version 1.0
 */
public class ZabbixFilterBuilder {

    private ZabbixFilter filter = new ZabbixFilter();

    private ZabbixFilterBuilder() {
    }

    static public ZabbixFilterBuilder newBuilder() {
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
