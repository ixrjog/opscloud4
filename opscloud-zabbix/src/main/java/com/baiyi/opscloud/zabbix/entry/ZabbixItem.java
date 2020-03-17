package com.baiyi.opscloud.zabbix.entry;


import lombok.Data;

@Data
public class ZabbixItem {


    // TODO HELP https://www.zabbix.com/documentation/4.0/manual/api/reference/item/get

    private String itemid;
    private String type;
    private String snmp_community;
    private String snmp_oid;
    private String hostid;
    private String name;
    private String key_;
    private String delay;
    private String history;
    private String trends;
    private String lastvalue;
    private String lastclock;
    private String prevvalue;
    private String state;
    private String status;
    private String value_type;
    private String trapper_hosts;
    private String units;
    private String snmpv3_securityname;
    private String snmpv3_securitylevel;
    private String snmpv3_authpassphrase;
    private String snmpv3_privpassphrase;
    private String snmpv3_authprotocol;
    private String snmpv3_privprotocol;
    private String snmpv3_contextname;
    private String error;
    private String lastlogsize;
    private String logtimefmt;
    private String templateid;
    private String valuemapid;
    private String params;
    private String ipmi_sensor;
    private String authtype;
    private String username;
    private String password;
    private String publickey;
    private String privatekey;
    private String mtime;
    private String lastns;
    private String flags;
    private String interfaceid;
    private String port;
    private String description;
    private String inventory_link;
    private String lifetime;
    private String evaltype;
    private String jmx_endpoint;
    private String master_itemid;
    private String timeout;
    private String url;
   // private String query_fields": [],
    private String posts;
    private String status_codes;
    private String follow_redirects;
    private String post_type;
    private String http_proxy;
   // private String headers": [],
    private String retrieve_mode;
    private String request_method;
    private String output_format;
    private String ssl_cert_file;
    private String ssl_key_file;
    private String ssl_key_password;
    private String verify_peer;
    private String verify_host;
    private String allow_traps;


}
