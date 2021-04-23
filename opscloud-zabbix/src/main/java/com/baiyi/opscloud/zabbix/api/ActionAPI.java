package com.baiyi.opscloud.zabbix.api;

/**
 * @Author baiyi
 * @Date 2021/2/2 10:19 上午
 * @Version 1.0
 */
public enum ActionAPI implements IZabbixAPI {

    GET("action.get"),

    DELETE("action.delete"),

    CREATE("action.create");

    private String method;

    ActionAPI(String method) {
        this.method = method;
    }

    @Override
    public String getMethod() {
        return this.method;
    }
}
