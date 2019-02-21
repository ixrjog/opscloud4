package com.sdg.cmdb.domain.zabbix.response;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

@Data
public class ZabbixResponseResult implements Serializable {
    private static final long serialVersionUID = -3738615451029447622L;

    private String ipmi_privilege; // "2",
    private String maintenance_status; // "0",
    private String jmx_available; //0",
    private String errors_from; //0",
    private String tls_psk_identity; //
    private String available; //"1",
    private String snmp_errors_from; //"0",
    private String flags; //"0",
    private String hostid; //"10084",
    private String description; //"",
    private String tls_issuer; //"",
    private String error; //"",
    private String jmx_errors_from; //"0",
    private String auto_compress; //"1",
    private String proxy_hostid; //"0",
    private String maintenanceid; //"0",
    private String maintenance_from; //"0",
    private String ipmi_authtype; //"-1",
    private String ipmi_username; //"",
    private String snmp_disable_until; //"0",
    private String host; //"Zabbix server",
    private String tls_psk; //"",
    private String jmx_error; //"",
    private String jmx_disable_until; //"0",
    private String disable_until; //"0",
    private String ipmi_errors_from; //"0",
    private String snmp_error; //"",
    private String proxy_address; //"",
    private String maintenance_type; //"0",
    private String tls_accept; //"1",
    private String snmp_available; //"0",
    private String templateid; //"0",
    private String ipmi_available; //"0",
    private String lastaccess; //"0",
    private String ipmi_password; //"",
    private String ipmi_error; //"",
    private String name; //"Zabbix server",
    private String tls_connect; //"1",
    private String ipmi_disable_until; //"0",
    private String tls_subject; //"",
    private String status; //"0"

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
