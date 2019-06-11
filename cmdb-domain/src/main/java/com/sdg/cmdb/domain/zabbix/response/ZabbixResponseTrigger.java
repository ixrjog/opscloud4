package com.sdg.cmdb.domain.zabbix.response;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ZabbixResponseTrigger implements Serializable {
    private static final long serialVersionUID = 1398923757896097508L;

    private String triggerid;
    private String description;
    private String priority;
    private String lastchange;
    private String value;
    private List<ZabbixResponseHost> hosts;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
