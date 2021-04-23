package com.baiyi.opscloud.zabbix.builder;

import com.baiyi.opscloud.zabbix.param.ZabbixDefaultParam;

/**
 * @Author baiyi
 * @Date 2021/2/2 10:57 上午
 * @Version 1.0
 */
public class ZabbixDefaultParamBuilder {

    private ZabbixDefaultParam params = new ZabbixDefaultParam();

    private ZabbixDefaultParamBuilder() {
    }

    static public ZabbixDefaultParamBuilder newBuilder() {
        return new ZabbixDefaultParamBuilder();
    }

    public ZabbixDefaultParamBuilder putEntry(String key, String value) {
        params.putParam(key, value);
        return this;
    }


    public ZabbixDefaultParam build() {
        return params;
    }
}
