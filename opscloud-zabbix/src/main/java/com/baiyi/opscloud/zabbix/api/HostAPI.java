package com.baiyi.opscloud.zabbix.api;

/**
 * @Author baiyi
 * @Date 2021/2/2 10:12 上午
 * @Version 1.0
 */
public enum  HostAPI implements IZabbixAPI{

    GET("host.get"),
    CREATE("host.create"),
    INTERFACE_GET("hostinterface.get"),
    UPDATE("host.update"),
    MASSUPDATE("host.massupdate"),
    DELETE("host.delete");

    private String method;

    HostAPI(String  method) {
        this.method = method;
    }

    @Override
    public String getMethod() {
        return this.method;
    }
}
