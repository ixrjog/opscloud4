package com.sdg.cmdb.domain.zabbix.response;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

@Data
public class ZabbixResponseHostgroupCreate implements Serializable {
    private static final long serialVersionUID = -604696372565192655L;
    private String[] groupids;
    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }


}
