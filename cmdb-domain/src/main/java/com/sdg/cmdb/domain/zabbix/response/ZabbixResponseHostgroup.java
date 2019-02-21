package com.sdg.cmdb.domain.zabbix.response;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

@Data
public class ZabbixResponseHostgroup implements Serializable {
    private static final long serialVersionUID = 6165720988371812404L;

    private String groupid;
    private String name;
    private String internal;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
