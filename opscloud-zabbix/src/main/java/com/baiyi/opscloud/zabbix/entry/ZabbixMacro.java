package com.baiyi.opscloud.zabbix.entry;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZabbixMacro {

    /**
     * {"hostmacroid":"1434","hostid":"11410","macro":"{$PORT}","value":"8080","description":"","type":"0"}
     *
     */
    private String macro;
    private String value;


}
