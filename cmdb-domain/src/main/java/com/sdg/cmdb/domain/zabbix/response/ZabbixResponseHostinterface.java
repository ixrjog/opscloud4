package com.sdg.cmdb.domain.zabbix.response;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

@Data
public class ZabbixResponseHostinterface implements Serializable {
    private static final long serialVersionUID = 4907560933082516545L;

    private String interfaceid;
    private String hostid;
    private String main;
    private String type;
    private String useip;
    private String ip;
    private String dns;
    private String port;
    private String bulk;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
