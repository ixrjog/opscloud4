package com.sdg.cmdb.domain.zabbix.response;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

@Data
public class ZabbixResponseUsergroup implements Serializable {
    private static final long serialVersionUID = 6165720988371812404L;

    // [{"debug_mode":"0","usrgrpid":"16","name":"users_nginx","users_status":"0","gui_access":"0"}]
    private String debug_mode;
    private String usrgrpid;
    private String name;
    private String users_status;
    private String gui_access;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
