package com.baiyi.opscloud.zabbix.http;


import lombok.Data;


/**
 * 用于校验ZabbixAPI配置是否生效
 */
@Data
public class ZabbixVersion  {

    private String version;
    // "name": "Zabbix server"
    private String name;
    private String hostid;

}
