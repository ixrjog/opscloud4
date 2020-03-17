package com.baiyi.opscloud.zabbix.entry;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class ZabbixAction {

    private String actionid;

    private String name;
    private String eventsource;
    private String status;
    @JsonIgnore
    private String esc_period;
    @JsonIgnore
    private String def_shortdata;
    @JsonIgnore
    private String def_longdata;
    @JsonIgnore
    private String r_shortdata;
    @JsonIgnore
    private String r_longdata;
    @JsonIgnore
    private String pause_suppressed;

}
