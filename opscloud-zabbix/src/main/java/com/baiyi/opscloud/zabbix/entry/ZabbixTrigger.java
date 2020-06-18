package com.baiyi.opscloud.zabbix.entry;


import com.baiyi.opscloud.zabbix.mapper.ZabbixHostMapper;
import lombok.Data;

import java.util.List;

@Data
public class ZabbixTrigger {


    private String triggerid;
    private String description;
    private String priority;
    private String lastchange;
    private String value;
    private List<ZabbixHostMapper> hosts;

}
