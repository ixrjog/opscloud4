package com.baiyi.opscloud.zabbix.entry;


import lombok.Data;


@Data
public class ZabbixHostgroup {

    private String groupid;
    private String name;
    private String internal;
    private String flags;

}
