package com.sdg.cmdb.domain.zabbix.response;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

@Data
public class ZabbixResponseProxy implements Serializable {

    private static final long serialVersionUID = -8882945431811730563L;
    //     "interface": [],
    private String host;
    private String status;
    private String lastaccess;
    private String proxyid;
    private String description;
    private String tls_connect;
    private String tls_accept;
    private String tls_issuer;
    private String tls_subject;
    private String tls_psk_identity;
    private String tls_psk;


    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}

