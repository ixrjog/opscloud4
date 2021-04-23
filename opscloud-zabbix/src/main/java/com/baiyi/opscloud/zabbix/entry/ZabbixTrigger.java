package com.baiyi.opscloud.zabbix.entry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZabbixTrigger {

    private String triggerid;
    private String description;
    private String priority;
    private String lastchange;
    private String value;
    private List<ZabbixHost> hosts;

}
