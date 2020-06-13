package com.baiyi.opscloud.zabbix.entry;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ZabbixUser {

    private String userid;
    private String alias;
    private String name;
    private String surname;
    private String url;
    private String autologin;
    private String autologout;
    private String lang;
    private String refresh;
    private String type;
    private String theme;
    @JsonProperty("attempt_failed")
    private String attemptFailed;
    @JsonProperty("attempt_ip")
    private String attemptIp;
    @JsonProperty("attempt_clock")
    private String attemptClock;
    @JsonProperty("rows_per_page")
    private String rowsPerPage;

}
