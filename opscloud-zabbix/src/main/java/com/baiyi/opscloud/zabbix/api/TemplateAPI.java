package com.baiyi.opscloud.zabbix.api;

/**
 * @Author baiyi
 * @Date 2021/2/2 10:20 上午
 * @Version 1.0
 */
public enum  TemplateAPI implements IZabbixAPI{

    GET("template.get");

    private String method;

    TemplateAPI(String method) {
        this.method = method;
    }

    @Override
    public String getMethod() {
        return this.method;
    }
}
