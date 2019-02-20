package com.sdg.cmdb.domain.zabbix.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class ZabbixResponseUserLogin implements Serializable {
    private static final long serialVersionUID = 8069912761800379789L;

    private int id = 0;
    private String jsonrpc;
    private String result;

    public String getAuth() {
        return result;
    }
}
