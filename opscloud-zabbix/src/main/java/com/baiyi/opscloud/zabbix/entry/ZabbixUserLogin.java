package com.baiyi.opscloud.zabbix.entry;

import lombok.Data;


@Data
public class ZabbixUserLogin {

    private int id = 0;
    private String jsonrpc;
    private String result;

    public String getAuth() {
        return result;
    }
}
