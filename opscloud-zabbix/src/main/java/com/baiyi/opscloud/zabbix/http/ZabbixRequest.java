package com.baiyi.opscloud.zabbix.http;


import lombok.Data;


/**
 * Created by liangjian on 2016/12/19.
 */
@Data
public class ZabbixRequest {

    private String jsonrpc = "2.0";
    private String method;
    private String auth;
    private Integer id;

}
