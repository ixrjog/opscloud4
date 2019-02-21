package com.sdg.cmdb.domain.zabbix.response;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

@Data
public class ZabbixResponseAction implements Serializable {
    private static final long serialVersionUID = -4269275252486828993L;

    private String actionid;
    private String name;
    private String eventsource;
    private String status;
    private String esc_period;
    private String def_shortdata;
    private String def_longdata;
    private String r_shortdata;
    private String r_longdata;
    private String pause_suppressed;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
