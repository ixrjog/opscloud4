package com.baiyi.opscloud.zabbix.api;

/**
 * @Author baiyi
 * @Date 2021/2/2 10:29 上午
 * @Version 1.0
 */
public enum UserAPI implements IZabbixAPI {

    GET("user.get"),
    CREATE("user.create"),
    UPDATE("user.update"),
    DELETE("user.delete"),
    LOGIN("user.login");

    private String method;

    UserAPI(String method) {
        this.method = method;
    }

    @Override
    public String getMethod() {
        return this.method;
    }
}
