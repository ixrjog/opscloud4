package com.baiyi.opscloud.common.base;

/**
 * @Author baiyi
 * @Date 2020/5/28 2:35 下午
 * @Version 1.0
 */
public enum  ServerChangeFlow {

    SERVER_UNACTIVE("SERVER_UNACTIVE"),
    ZABBIX_HOST_UNACTIVE("ZABBIX_HOST_UNACTIVE"), // 主机监控禁用
    JUMPSERVER_ASSET_UNACTIVE("JUMPSERVER_ASSET_UNACTIVE"), // 堡垒机禁用
    APP_STOP("APP_STOP"),
    OFFLINE("OFFLINE");

    private String name;

    ServerChangeFlow(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
