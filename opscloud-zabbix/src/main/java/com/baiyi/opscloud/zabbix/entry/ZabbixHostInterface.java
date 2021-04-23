package com.baiyi.opscloud.zabbix.entry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZabbixHostInterface {

    private String interfaceid;
    private String hostid;
    private String main;
    private String type;
    private String useip;
    private String ip;
    private String dns;
    private String port;

}
