package com.baiyi.opscloud.zabbix.api;

/**
 * @Author baiyi
 * @Date 2021/2/2 10:03 上午
 * @Version 1.0
 */
public enum HostgroupAPI implements IZabbixAPI{

    GET("hostgroup.get"),
    CREATE("hostgroup.create");

    private String method;

    HostgroupAPI(String  method) {
        this.method = method;
    }

    @Override
    public String getMethod() {
        return this.method;
    }
}
