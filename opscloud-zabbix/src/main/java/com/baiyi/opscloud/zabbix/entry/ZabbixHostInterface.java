package com.baiyi.opscloud.zabbix.entry;

import lombok.Data;


@Data
public class ZabbixHostInterface {

    private String interfaceid;
    private String hostid;
    private String main;
    private String type;
    private String useip;
    private String ip;
    private String dns;
    private String port;
    private String bulk;


}
