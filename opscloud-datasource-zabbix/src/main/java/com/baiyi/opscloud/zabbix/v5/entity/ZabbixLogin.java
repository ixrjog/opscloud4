package com.baiyi.opscloud.zabbix.v5.entity;

import lombok.Data;

/**
 * @Author baiyi
 * @Date 2021/11/17 10:42 上午
 * @Version 1.0
 */
public class ZabbixLogin {

    @Data
    public static class LoginAuth {
        private String jsonrpc;
        private Integer id;
        private String result;
    }

}