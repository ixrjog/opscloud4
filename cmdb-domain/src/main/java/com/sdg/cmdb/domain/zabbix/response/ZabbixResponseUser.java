package com.sdg.cmdb.domain.zabbix.response;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

@Data
public class ZabbixResponseUser implements Serializable {
    private static final long serialVersionUID = 6623150106993176928L;
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
    private String attempt_failed;
    private String attempt_ip;
    private String attempt_clock;
    private String rows_per_page;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
