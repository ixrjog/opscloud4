package com.baiyi.opscloud.zabbix.entry;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serializable;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultMapper implements Serializable {

    private static final long serialVersionUID = -1580501996512626570L;
    @JsonProperty("ipmi_privilege")
    private String ipmiPrivilege; // "2",
    @JsonProperty("maintenance_status")
    private String maintenanceStatus; // "0",
    @JsonProperty("jmx_available")
    private String jmxAvailable; //0",
    @JsonProperty("errors_from")
    private String errorsFrom; //0",
    @JsonProperty("tls_psk_identity")
    private String tlsPskIdentity; //

    /**
     * (readonly) Availability of Zabbix agent.
     *
     * Possible values are:
     * 0 - (default) unknown;
     * 1 - available;
     * 2 - unavailable.
     */
    private String available; //"1",
    @JsonIgnore
    private String snmp_errors_from; //"0",
    private String flags; //"0",
    private String hostid; //"10084",

    @JsonIgnore
    private String discover; //"",

    @JsonIgnore
    private String description; //"",
    @JsonIgnore
    private String tls_issuer; //"",
    private String error; //"",
    @JsonIgnore
    private String jmx_errors_from; //"0",
    @JsonIgnore
    private String auto_compress; //"1",
    @JsonProperty("proxy_hostid")
    private String proxyHostid; //"0",
    private String maintenanceid; //"0",
    @JsonIgnore
    private String maintenance_from; //"0",
    @JsonIgnore
    private String ipmi_authtype; //"-1",
    @JsonIgnore
    private String ipmi_username; //"",
    @JsonIgnore
    private String snmp_disable_until; //"0",
    private String host; //"Zabbix server",
    @JsonIgnore
    private String tls_psk; //"",
    @JsonIgnore
    private String jmx_error; //"",
    @JsonIgnore
    private String jmx_disable_until; //"0",
    @JsonIgnore
    private String disable_until; //"0",
    @JsonIgnore
    private String ipmi_errors_from; //"0",
    @JsonIgnore
    private String snmp_error; //"",
    @JsonProperty("proxy_address")
    private String proxyAddress; //"",
    @JsonIgnore
    private String maintenance_type; //"0",
    @JsonIgnore
    private String tls_accept; //"1",
    @JsonIgnore
    private String snmp_available; //"0",
    private String templateid; //"0",
    @JsonIgnore
    private String ipmi_available; //"0",
    private String lastaccess; //"0",
    @JsonIgnore
    private String ipmi_password; //"",
    @JsonIgnore
    private String ipmi_error; //"",
    private String name; //"Zabbix server",
    @JsonIgnore
    private String tls_connect; //"1",
    @JsonIgnore
    private String ipmi_disable_until; //"0",
    @JsonIgnore
    private String tls_subject; //"",
    private String status; //"0"

    public boolean isEmpty(){
        return StringUtils.isEmpty(this.hostid);
    }


}
