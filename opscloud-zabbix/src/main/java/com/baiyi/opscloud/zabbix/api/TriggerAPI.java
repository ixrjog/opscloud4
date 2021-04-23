package com.baiyi.opscloud.zabbix.api;

/**
 * @Author baiyi
 * @Date 2021/2/18 10:20 上午
 * @Version 1.0
 */
public enum  TriggerAPI implements IZabbixAPI {

    GET("trigger.get");

    private String method;

    TriggerAPI(String method) {
        this.method = method;
    }

    @Override
    public String getMethod() {
        return this.method;
    }
}

