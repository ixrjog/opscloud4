package com.baiyi.opscloud.zabbix.v5.entity;

import com.baiyi.opscloud.zabbix.v5.entity.base.ZabbixResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/12/27 2:29 PM
 * @Version 1.0
 */
public class ZabbixProxy {

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class QueryProxyResponse extends ZabbixResponse.Response {
        private List<Proxy> result;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class UpdateProxyResponse extends ZabbixResponse.Response {
        private Proxyids result;
    }

    @Data
    public static class Proxyids {
        private List<String> proxyids;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Proxy implements Serializable {
        @Serial
        private static final long serialVersionUID = 4169371114358814825L;
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

}