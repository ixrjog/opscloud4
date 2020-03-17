package com.baiyi.opscloud.zabbix.http;

import lombok.Data;

@Data
public class ZabbixResult  {

    private String itemid;
    private String ns;
    private String clock;
    private String value;

}
