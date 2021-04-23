package com.baiyi.opscloud.zabbix.api;

/**
 * @Author baiyi
 * @Date 2021/2/2 10:21 上午
 * @Version 1.0
 */
public enum ProxyAPI implements IZabbixAPI{

    GET("proxy.get");

    private String method;

    ProxyAPI(String method) {
        this.method = method;
    }

    @Override
    public String getMethod() {
        return this.method;
    }
}
