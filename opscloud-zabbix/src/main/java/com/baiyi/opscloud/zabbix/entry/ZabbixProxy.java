package com.baiyi.opscloud.zabbix.entry;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;


@Data
public class ZabbixProxy {
    //     "interface": [],
    private String host;
    private String status;
    private String lastaccess;
    private String proxyid;
    private String description;
    @JsonIgnore
    private String tls_connect;
    @JsonIgnore
    private String tls_accept;
    @JsonIgnore
    private String tls_issuer;
    @JsonIgnore
    private String tls_subject;
    @JsonIgnore
    private String tls_psk_identity;
    @JsonIgnore
    private String tls_psk;



}

