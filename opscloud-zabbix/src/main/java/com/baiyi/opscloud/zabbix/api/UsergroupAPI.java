package com.baiyi.opscloud.zabbix.api;

/**
 * @Author baiyi
 * @Date 2021/2/2 10:34 上午
 * @Version 1.0
 */
public enum  UsergroupAPI implements IZabbixAPI{

    CREATE("usergroup.create"),
    UPDATE("usergroup.update"),
    GET("usergroup.get");

    private String method;

    UsergroupAPI(String method) {
        this.method = method;
    }

    @Override
    public String getMethod() {
        return this.method;
    }
}
