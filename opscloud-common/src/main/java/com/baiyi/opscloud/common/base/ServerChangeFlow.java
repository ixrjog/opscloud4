package com.baiyi.opscloud.common.base;

/**
 * @Author baiyi
 * @Date 2020/5/28 2:35 下午
 * @Version 1.0
 */
public enum ServerChangeFlow {

    SERVER_UNACTIVE("SERVER_UNACTIVE"),
    SERVER_FACTORY_UNACTIVE("SERVER_FACTORY_UNACTIVE"),
    // ZABBIX_HOST_UNACTIVE("ZABBIX_HOST_UNACTIVE"), // 主机监控禁用
    // JUMPSERVER_ASSET_UNACTIVE("JUMPSERVER_ASSET_UNACTIVE"), // 堡垒机禁用
    APPLICATION_STOP("APPLICATION_STOP"),
    SERVER_OFFLINE("SERVER_OFFLINE"),
    FINALIZED("FINALIZED");

    private String name;

    ServerChangeFlow(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
