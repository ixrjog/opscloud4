package com.baiyi.opscloud.zabbix.entry;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZabbixProxy {
    //     "interface": [],
    @JsonIgnore
    private String proxy_hostid;
    private String host;
    private String status;
    @JsonIgnore
    private String disable_until;
    @JsonIgnore
    private String error;
    @JsonIgnore
    private String available;
    @JsonIgnore
    private String errors_from;
    @JsonIgnore
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

