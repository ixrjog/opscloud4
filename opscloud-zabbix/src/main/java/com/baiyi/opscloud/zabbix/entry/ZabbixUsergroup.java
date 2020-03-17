package com.baiyi.opscloud.zabbix.entry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ZabbixUsergroup {

    // [{"debug_mode":"0","usrgrpid":"16","name":"users_nginx","users_status":"0","gui_access":"0"}]

    @JsonProperty("debug_mode;")
    private String debugMode;

    private String usrgrpid;

    private String name;

    @JsonProperty("users_status")
    private String usersStatus;
    @JsonProperty("gui_access")
    private String guiAccess;


}
